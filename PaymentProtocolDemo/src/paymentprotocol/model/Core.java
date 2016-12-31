package paymentprotocol.model;

import java.io.IOException;

import paymentprotocol.model.p2p.P2PLayer;
import paymentprotocol.observer.CoreObserver;
import paymentprotocol.observer.GUIObserver;

/**
 * Application core. All the main operations are called in this class.
 * 
 * @author yamal
 *
 */
public class Core implements CoreObserver {
	
	private P2PLayer p2pLayer;
	
	private GUIObserver guiObserver;
	
	public Core(P2PLayer p2pLayer){
		//hay que hacer que el id de cada nodo se genere con un hash de un UUUID
		//para que no se repitan
		this.p2pLayer = p2pLayer;
	}
	
	public void addObserver(GUIObserver obs){
		this.guiObserver = obs;
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAddress
	 * @param bootport
	 */
	public void connect(int bindport, String bootAddress, int bootport){
		try {
			p2pLayer.join(bindport, bootAddress, bootport, this);
			
			//cargar de la DHT el perfil publico
			
			//cargar de la DHT las facturas y comprobar si hay pagos pendientes
			/*
			 * if hay bills then notificar a la GUI
			 * else se notifica que no hay pagos pendientes
			 * 
			 */
		}
		catch (IOException e){
			e.printStackTrace();
			//notificar al observador
			guiObserver.failedConnection();
		}
	}
	
	@Override
	public void onReceiveNotification() {
		guiObserver.onReceiveNotification();
	}
}
