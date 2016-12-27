package demo;

/**
 * This class will do the necessary operations to test and simulate the payment protocol
 * 
 * Two FreePastry nodes will be created and joined to the network (a Creditor and a Debitor). 
 * A PublicProfile will be created and stored into the DHT for each node (each user).
 * A fictitious Bill will be created and stored into the DHT with information from previous PublicProfiles.
 * A GUI will be launched per each node, simulating two open user sessions.
 * Debitor GUI: Initially "initiate payment" option will be available.
 * Creditor GUI: Initially there are not any available options.
 * At this moment the payment protocol can be started.
 *  
 * The instance of this class must be singleton and it will be the boot node that will be used to connect
 * the rest of the nodes to the network
 * @author yamal
 *
 */
public class Demo {
	
	public Demo(){
		createBootNode();
		
		createStorage();
		
		createFreePastryNode();
		createFreePastryNode();
		
		createPublicProfile();
		createPublicProfile();
		
		storePublicProfile();
		storePublicProfile();
		
		createBill();
		
		storeBill();
		
		run();
		run();
	}
	
	/**
	 * This function create a freepastry node which will be the boot node
	 */
	private void createBootNode(){
		
	}
	
	/**
	 * This function creates a Past storage necessary to store the bill and the public profiles
	 * before launching application for both users (debitor and creditor)
	 */
	private void createStorage(){
		
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
	
	private void run(){
		
	}
}
