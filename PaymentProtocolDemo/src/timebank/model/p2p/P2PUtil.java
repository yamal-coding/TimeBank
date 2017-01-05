package timebank.model.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import rice.environment.Environment;
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

/**
 * Reusable P2P methods (Past, Connections, Storage, etc)
 * @author yamal
 *
 */
public class P2PUtil {
	/**
	 * This method returns an instance of a InetSocketAddress object. It is used to create a PastryNode
	 * @param bootAddress
	 * @param bootport
	 * @return InetSocketAddress
	 * @throws UnknownHostException
	 */
	public static InetSocketAddress createInetSocketAddress(String bootAddress, int bootport) throws UnknownHostException {
		//It is necessary an InetSocketAddress instance to connect the node to the layer that FreePastry uses
		InetAddress bootInetAddress = InetAddress.getByName(bootAddress);
		InetSocketAddress bootInetSocketAddress = new InetSocketAddress(bootInetAddress, bootport);
		return bootInetSocketAddress;
	}
	
	/**
	 * This method creates a PastryNode. It use a random factory to generate node ids
	 * @param env
	 * @param bindport
	 * @return PastryNode
	 * @throws IOException
	 */
	public static PastryNode createPastryNode(Environment env, int bindport) throws IOException {
		//A node id factory is instantiated. In this case a random factory is used 
		NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
		
		//The node factory is instatiated using the previous node id factory
		PastryNodeFactory nodeFactory = new SocketPastryNodeFactory(nidFactory, bindport, env);
		
		//The pastry node that will be connected to the FreePastry network is created
		return nodeFactory.newNode();
	}

	/**
	 * This method creates an instance of the Storage which will be used in Past
	 * @param storageDirectory
	 * @param idFactory
	 * @param node
	 * @return Storage
	 * @throws IOException
	 */
	public static Storage createPastStorage(String storageDirectory, PastryIdFactory idFactory, PastryNode node, boolean disk) throws IOException {
		if (disk)//disk based storage is used to implement the storage interface
			return new PersistentStorage(idFactory, storageDirectory, 4*1024*1024, node.getEnvironment());
		else //MemoryStorage implementation uses RAM based storage
			return new MemoryStorage(idFactory);
	}
	
	/**
	 * This method returns a Past implementation with the specified parameters
	 * @param storage
	 * @param idFactory
	 * @param node
	 * @return Past
	 */
	public static Past createPast(Storage storage, PastryIdFactory idFactory, PastryNode node) {
		//The Past interface is implemented using the previous storage object and a cache is specified too.
		//This Past instance will be used to perform the "put" and "lookup" operations on the DHT.
		return new PastImpl(node, new StorageManagerImpl(idFactory, storage, new LRUCache(new MemoryStorage(idFactory), 512*1024, node.getEnvironment())), 3, "");
	}
	
	/**
	 * This method joins a node the FreePastry network
	 * @param node
	 * @param bootInetSocketAddress
	 * @throws IOException
	 */
	public static void connectNode(PastryNode node, InetSocketAddress bootInetSocketAddress) throws IOException {
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
	}
}
