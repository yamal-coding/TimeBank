package paymentprotocol.model.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

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
	public void connect(int bindport, String bootAdrress, int bootport, CoreObserver coreObserver) throws IOException {
		if (!connected) {
			//It is necessary an InetSocketAddress instance to connect the node to the layer that FreePastry uses
			InetAddress bootInetAddress = InetAddress.getByName(bootAdrress);
			InetSocketAddress bootInetSocketAddress = new InetSocketAddress(bootInetAddress, bootport);
			
			//A node id factory is instantiated. In this case a random factory is used 
			NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
			
			//The node factory is instatiated using the previous node id factory
			PastryNodeFactory nodeFactory = new SocketPastryNodeFactory(nidFactory, bindport, env);
			
			//The pastry node that will be connected to the FreePastry network is created
			PastryNode node = nodeFactory.newNode();
			
			//The notification handler must be instantiated to handle the messages that the node receives and sends
			notificationHandler = new NotificationHandler(node, coreObserver);
			
			//It is created a factory to generate the keys of the values stored into the DHT. The algorithm used is SHA-1
			idFactory = new PastryIdFactory(env);
			
			//The next step is create a Past instance for the self node
			
			//The storage directory where the Past instance will be created must be specified
			String storageDirectory = "./storage" + node.getId().hashCode();
			
			//In this case a disk based storage is used to implement the storage interface
			Storage storage = new PersistentStorage(idFactory, storageDirectory, 4*1024*1024, node.getEnvironment());
			//MemoryStorage implementation uses RAM based storage
			//Storage storage = new MemoryStorage(idf);
			
			//The Past interface is implemented using the previous storage object and a cache is specified too.
			//This Past instance will be used to perform the "put" and "lookup" operations on the DHT.
			past = new PastImpl(node, new StorageManagerImpl(idFactory, storage, new LRUCache(new MemoryStorage(idFactory), 512*1024, node.getEnvironment())), 3, "");
			
			//We conect our pastry node with the boot address
			node.boot(bootInetSocketAddress);
			
			//Several attempts are made to connect the node to the network
			synchronized(node){
				while(!node.isReady() && !node.joinFailed()){
					//Delay to not collapse connection process
					try {
						node.wait(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//If an error occurred an exception is thrown
					if (node.joinFailed())
						throw new IOException("Unable to connect the node to the network. Reason: " + node.joinFailedReason());
				}
			}
			
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
