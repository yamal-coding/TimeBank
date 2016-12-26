package demo;

/**
 * Esta clase es la que se encargara de preparar la simulacion del protocolo de pago.
 * 
 * Se crearan dos nodos en la red de FreePastry (Creditor y Debitor).
 * Se creara un perfil publico por cada nodo y se almacenara en la DHT.
 * Se generara una factura (bill) inventada con informacion de los perfiles
 * y se guardara en la DHT.
 * Se lanzara una GUI por cada nodo simulando dos sesiones de usuario iniciadas.
 * Ventana de Creditor: opcion de iniciar pago disponible.
 * Ventana de debitor: inicialmente no hay opciones disponible.
 * Ya se puede iniciar el protocolo de pago.
 *  
 * @author yamal
 *
 */
public class Demo {
	
	public Demo(){
		createFreePastryNode();
		createFreePastryNode();
		
		createPublicProfile();
		createPublicProfile();
		
		storePublicProfile();
		storePublicProfile();
		
		createBill();
		
		storeBill();
		
		runGUI();
		runGUI();
	}


	private void createFreePastryNode(){
		
	}
	
	private void createPublicProfile(){
		
	}
	
	private void storePublicProfile(){
		
	}
	
	private void createBill(){
		
	}
	
	private void storeBill(){
		
	}
	
	private void runGUI(){
		
	}
}
