package paymentprotocol.model.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import paymentprotocol.model.messaging.NotificationHandler;
import paymentprotocol.observer.CoreObserver;
import rice.environment.Environment;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.past.Past;
import rice.p2p.past.PastImpl;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;
import rice.persistence.LRUCache;
import rice.persistence.MemoryStorage;
import rice.persistence.PersistentStorage;
import rice.persistence.Storage;
import rice.persistence.StorageManagerImpl;

public class P2PLayer {
	private Environment env;
	private PastryIdFactory idFactory;
	private Past past;
	private PastryNode node;
	
	private NotificationHandler notificationHandler;
	
	private boolean connected;
	
	public P2PLayer(Environment env){
		this.env = env;
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
	
	public void put(){
		
	}
	
	
	public void get(){
		
	}


}
