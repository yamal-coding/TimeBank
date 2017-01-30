package timebank.model.p2p;

import java.io.IOException;
import java.net.InetSocketAddress;

import rice.Continuation;
import rice.environment.Environment;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.past.Past;
import rice.p2p.past.PastContent;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.persistence.Storage;
import timebank.model.ConnectionReturnCode;
import timebank.model.exception.NodeNotInitializedException;
import timebank.model.files.network.persistent.FileType;
import timebank.model.messaging.Notification;
import timebank.observer.CoreObserver;

/**
 * This class is used to disengage P2P operations from application core
 * @author yamal
 *
 */
public class P2PLayer {
	private Environment env;
	private PastryIdFactory idFactory;
	private Past past;
	private PastryNode node;
	private boolean endpointCreated;
	
	//Observer to communicate with application core
	private CoreObserver coreObserver;
	
	//boolean set to true if the current node is connected to the FreePastry network
	private boolean connected;
	
	/**
	 * Class constructor
	 * @param env
	 * @param coreObserver Observer to communicate with Core
	 */
	public P2PLayer(Environment env){
		this.env = env;
		this.connected = false;
		this.endpointCreated = false;
	}
	
	/**
	 * This method returns the node assigned to the connection. If it is not created yet, an exception is thrown.
	 * @return
	 * @throws NodeNotInitializedException
	 */
	public PastryNode getNode() throws NodeNotInitializedException{
		if (!connected)
			throw new NodeNotInitializedException();
		
		return this.node;
	}
	
	/**
	 * Returns the PastryIdFactory instance
	 * @return idFactory
	 */
	public PastryIdFactory getPastryIdFactory(){
		return idFactory;
	}
	
	/**
	 * This method add the CoreObserver to this class to communicate with core application
	 * @param coreObserver
	 */
	public void addObserver(CoreObserver coreObserver){
		this.coreObserver = coreObserver;
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAdrress
	 * @param bootport
	 * @param coreObserver
	 * @throws IOException
	 */
	public ConnectionReturnCode join(int bindport, String bootAddress, int bootport, CoreObserver coreObserver) {
		if (!connected) {
			try {
				InetSocketAddress bootInetSocketAddress = P2PUtil.createInetSocketAddress(bootAddress, bootport);
				
				node = P2PUtil.createPastryNode(env, bindport);
				
				//It is created a factory to generate the keys of the values stored into the DHT. The algorithm used is SHA-1
				idFactory = new PastryIdFactory(env);
				
				//The next step is create a Past instance for the self node
				
				//The storage directory where the Past instance will be created must be specified
				String storageDirectory = "./storage" + node.getId().hashCode();
				//The storage to use in Past is initialized
				Storage storage = P2PUtil.createPastStorage(storageDirectory, idFactory, node, true);
				
				//The Past implementation is instantiated
				past = P2PUtil.createPast(storage, idFactory, node);
				
				//At this moment the PastryNode can join the network 
				P2PUtil.connectNode(node, bootInetSocketAddress);
				
				//Now we can notify to the observers the connection success
				connected = true;
				
				return ConnectionReturnCode.CONNECTION_SUCCESFUL;
			}
			catch (IOException e){
				return ConnectionReturnCode.FAILED_CONNECTION;
			}
		}
		
		return ConnectionReturnCode.NODE_ALREADY_CONNECTED;
	}
	
	/**
	 * Method called to store some content into the DHT
	 * @param content
	 * @param contentID
	 * @param fileType
	 */
	public void put(PastContent content, String contentID, FileType fileType){
		past.insert(content, new InsertContinuationImpl(contentID, fileType));
	}
	
	/**
	 * Private class used to insert some content into the DHT and received the notification
	 * of failure or successful because this process is not instantaneous
	 * @author yamal
	 *
	 */
	private class InsertContinuationImpl implements Continuation<Boolean[], Exception> {
		private String contentID;
		private FileType fileType;
		
		public InsertContinuationImpl(String contentID, FileType fileType) {
			this.contentID = contentID;
			this.fileType = fileType;
		}
		
		//Method called when there has been an error during insert call
		@Override
		public void receiveException(Exception arg0) {
			coreObserver.onFinishedStorage("Failed content " + fileType + "." + contentID + " storage.", false);
		}

		//Method called when insert call has successfully finish. It receibes a Boolean array
		//to now how many replicas have been stored (True -> stored; False -> not stored)
		//Metodo que es llamado cuando se ha finalizado el almacenamiento del contenido. Recibe
		//un array de Boolean para saber cuantas de las replicas se han almacenado y cuantas no
		//true -> almacenado; false -> no almacenado
		@Override
		public void receiveResult(Boolean[] arg0) {
			//The number of successfull stores is counted and printed
			int successfullStores = 0;
			for (int i = 0; i < arg0.length; i++)
				if (arg0[i].booleanValue())
					successfullStores++;
			
			coreObserver.onFinishedStorage("Content "+ fileType + "." + contentID + " has been stored " + successfullStores + " times.", false);
		}		
	}
	
	/**
	 * Methd called to get some content from the DHT
	 * @param key
	 * @param contentID
	 * @param fileType
	 */
	public void get(Id key, String contentID, FileType fileType ){
		past.lookup(key, new LookupContinuationImpl(contentID, fileType));
	}
	
	/**
	 * Private class used to lookup some content from the DHT and received the notification
	 * of failure or the requested object because this process is not instantaneous
	 * @author yamal
	 *
	 */
	private class LookupContinuationImpl implements Continuation<PastContent, Exception>{
		private String contentID;
		private FileType fileType;
		
		public LookupContinuationImpl(String contentID, FileType fileType){
			this.contentID = contentID;
			this.fileType = fileType;
		}
		
		//Method called when there has been an error during lookup call
		@Override
		public void receiveException(Exception arg0) {
			String errorMsg = "Failed content " + fileType + "." + contentID + " lookup.";
			
			switch(fileType){
			case BILL_ENTRY: coreObserver.onLookupBill(null, true, errorMsg); break;
			case ACCOUNT_LEDGER_ENTRY: coreObserver.onLookupAccountLedger(null, true, errorMsg); break;
			case FAM_ENTRY: coreObserver.onLookupFAMEntry(null, true, errorMsg); break;
			case FBM_ENTRY: coreObserver.onLookupFBMEntry(null, true, errorMsg); break;
			default: //PUBLIC_PROFILE_ENTRY
				coreObserver.onLookupPublicProfile(null, true, errorMsg);
			}
		}
		
		//Method called when lookup has finished successfully. It receives the requested object
		//as a PastContent instance
		@Override
		public void receiveResult(PastContent arg0) {
			String successfullMsg = "Successful content " + fileType + "." + contentID + " lookup";
			
			switch(fileType){
			case BILL_ENTRY: coreObserver.onLookupBill(arg0, false, successfullMsg); break;
			case ACCOUNT_LEDGER_ENTRY: coreObserver.onLookupAccountLedger(arg0, false, successfullMsg); break;
			case FAM_ENTRY: coreObserver.onLookupFAMEntry(arg0, false, successfullMsg); break;
			case FBM_ENTRY: coreObserver.onLookupFBMEntry(arg0, false, successfullMsg); break;
			default: //PUBLIC_PROFILE_ENTRY
				coreObserver.onLookupPublicProfile(arg0, false, successfullMsg);
			}
		}
	}
}
