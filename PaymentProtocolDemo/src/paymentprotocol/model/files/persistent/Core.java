package paymentprotocol.model.files.persistent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import paymentprotocol.model.command.Command;
import paymentprotocol.model.messaging.NotificationHandler;
import rice.environment.Environment;
import rice.p2p.commonapi.Node;
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
 * Application core. All the main operations are called in this class.
 * 
 * @author yamal
 *
 */
public class Core {
	
	private Environment env;
	private PastryIdFactory idFactory;
	private Past past;
	
	private NotificationHandler notificationHandler;
	
	public Core(Environment env){
		//hay que hacer que el id de cada nodo se genere con un hash de un UUUID
		//para que no se repitan
		this.env = env;
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAddress
	 * @param bootport
	 */
	public void connect(int bindport, String bootAdrress, int bootport){
		try {
			//Comprobamos que la direccion de conexion existe y la usamos
			InetAddress bootInetAddress = InetAddress.getByName(bootAdrress);
			InetSocketAddress bootInetSocketAddress = new InetSocketAddress(bootInetAddress, bootport);
			
			//Instanciamos la factoria de Ids de nodos
			NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
			
			//Instanciamos la factoría de nodos
			PastryNodeFactory nodeFactory = new SocketPastryNodeFactory(nidFactory, bindport, env);
			
			//Instanciamos nuestro nodo sobre el que trabajara el programa y que conectaremos a la red FreePastry
			PastryNode node = nodeFactory.newNode();
			
			//Instanciamos el manejador de notificaciones entre nodos (NoificationHandler)
			notificationHandler = new NotificationHandler(node);
			
			//Instanciamos la factoria de ids que se usaran como claves en la DHT (se usa SHA-1 para generar dichps ids)
			idFactory = new PastryIdFactory(env);
			
			//Ahora procedemos a crear una instancia de almacenamiento Past para nuestro nodo
			
			//En primer lugar especificamos el tipo de almacenamiento y la ruta de este
			String storageDirectory = "./storage" + node.getId().hashCode();
			
			Storage storage = new PersistentStorage(idFactory, storageDirectory, 4*1024*1024, node.getEnvironment());
			//si queremos almacenamiento en RAM se usaria MemoryStorage en lugar de PersistentStorage
			//Storage storage = new MemoryStorage(idf);
			
			//Instanciamos la iterfaz Past como PastImpl. Este objeto nos servira para realizar las operaciones de put y get de la DHT
			//Recibe el nodo sobre el que trabaja, el almacenamiento, el numero de replicas de cada almacenamiento entre otros.
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
			
		} catch (Exception e) {
			//If an exception is thrown during connection process, the error must be notified to the observers
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to let the user execute an action encapsulated in a Command object
	 * @param c
	 */
	public void executeCommand(Command c){
		c.execute();
	}
}
