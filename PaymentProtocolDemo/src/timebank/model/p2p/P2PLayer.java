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
import timebank.model.files.network.persistent.FileType;
import timebank.model.messaging.NotificationPair;
import timebank.observer.CoreObserver;

/**
 * This class is used to disengage P2P operations from application core
 * @author yamal
 *
 */
public class P2PLayer implements Application {
	private Environment env;
	private PastryIdFactory idFactory;
	private Past past;
	private PastryNode node;
	private Endpoint endpoint;
	
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
				
				//The endpoint is initialized to allow receiving and sending messages from others and to other nodes
				this.endpoint = node.buildEndpoint(this, "myinstance");
				this.endpoint.register();
				
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

	/**
	 * Method used to send a message to a concrete node through his NodeHandle
	 * @param nh
	 * @param msg
	 */
	public void sendNotification(NodeHandle nh, Message msg){
		endpoint.route(null, msg, nh);
	}
	
	/**
	 * This method is called when a Message object is received from other PastryNode
	 */
	@Override
	public void deliver(Id arg0, Message arg1) {
		try {
			NotificationPair notificationPair = (NotificationPair) arg1;
			coreObserver.onReceiveNotification(notificationPair);
		}
		catch (ClassCastException e){
			
		}
	}

	@Override
	public boolean forward(RouteMessage arg0) {
		return true;
	}

	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
