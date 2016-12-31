package paymentprotocol.model.p2p;

import java.io.IOException;
import java.net.InetSocketAddress;

import paymentprotocol.model.messaging.NotificationHandler;
import paymentprotocol.observer.CoreObserver;
import rice.Continuation;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.past.Past;
import rice.p2p.past.PastContent;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.persistence.Storage;

public class P2PLayer {
	private Environment env;
	private PastryIdFactory idFactory;
	private Past past;
	private PastryNode node;
	
	private NotificationHandler notificationHandler;
	
	private CoreObserver coreObserver;
	
	private boolean connected;
	
	public P2PLayer(Environment env, CoreObserver coreObserver){
		this.env = env;
		this.coreObserver = coreObserver;
		this.connected = false;
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAdrress
	 * @param bootport
	 * @param coreObserver
	 * @throws IOException
	 */
	public void join(int bindport, String bootAddress, int bootport, CoreObserver coreObserver) throws IOException {
		if (!connected) {
			InetSocketAddress bootInetSocketAddress = P2PUtil.createInetSocketAddress(bootAddress, bootport);
			
			node = P2PUtil.createPastryNode(env, bindport);
			
			//It is created a factory to generate the keys of the values stored into the DHT. The algorithm used is SHA-1
			idFactory = new PastryIdFactory(env);
			
			//The notification handler must be instantiated to handle the messages that the node receives and sends
			notificationHandler = new NotificationHandler(node, coreObserver);
			
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
		}
	}
	
	public void sendMessage(NodeHandle nh, Message msg){
		notificationHandler.sendNotification(nh, msg);
	}
	
	public void put(PastContent content, String contentID){
		past.insert(content, new InsertContinuationImpl(contentID));
	}
	
	private class InsertContinuationImpl implements Continuation<Boolean[], Exception> {
		private String content;
		
		public InsertContinuationImpl(String content) {
			this.content = content;
		}
		
		//Method called when there has been an error during insert call
		@Override
		public void receiveException(Exception arg0) {
			coreObserver.onFinishedStorage("Failed content " + content + " storage.", false);
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
			for (int i = 0; i < arg0.length; i++){
				if (arg0[i].booleanValue())
					successfullStores++;
			}
			
			coreObserver.onFinishedStorage("Content "+ content + " has been stored " + successfullStores + " times.", false);
		}		
	}
	
	
	public void get(Id key){
		past.lookup(key, new Continuation<PastContent, Exception>(){
			
			//Metodo que se llama cuando ha habido algun error en el lookup de un contenido en la DHT
			@Override
			public void receiveException(Exception arg0) {
				//Notify to Core
			}
			//Metodo que se llama cuando el resultado a sido devuelto por la DHT
			@Override
			public void receiveResult(PastContent arg0) {
				//Notify to Core
			}
			
		});
	}
}
