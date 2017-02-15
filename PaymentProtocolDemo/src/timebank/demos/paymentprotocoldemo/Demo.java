package timebank.demos.paymentprotocoldemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.UUID;

import rice.Continuation;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.past.Past;
import rice.p2p.past.PastContent;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.persistence.Storage;
import timebank.control.Controller;
import timebank.gui.MainView;
import timebank.gui.TerminalGUI;
import timebank.model.Core;
import timebank.model.files.local.PrivateProfile;
import timebank.model.files.network.persistent.Bill;
import timebank.model.files.network.persistent.EntryType;
import timebank.model.files.network.persistent.FileType;
import timebank.model.files.network.persistent.PublicProfile;
import timebank.model.p2p.P2PLayer;
import timebank.model.p2p.P2PUtil;
import timebank.model.util.Util;

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
	Environment env;
	Past past;
	PastryIdFactory idFactory;
	PastryNode node;
	
	public static void main(String[] args){
		Demo d = new Demo();
		try {
			d.runDemo(9003, "192.168.1.44", 9003);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Demo(){
		env = new Environment();
		env.getParameters().setString("nat_search_policy","never");
	}
	
	public void runDemo(int bindport, String bootAddress, int bootport) throws IOException {
		createBootNode(bindport, bootAddress, bootport);
		
		PrivateProfile debitorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		PrivateProfile creditorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		
		PastContent debitorPublicProfile = createPublicProfile(debitorPrivateProfile.getUUID(), false);
		PastContent creditorPublicProfile = createPublicProfile(creditorPrivateProfile.getUUID(), true);

		PastContent bill = createBill(creditorPrivateProfile.getUUID(), creditorPublicProfile.getId(), debitorPublicProfile.getId());
		
		debitorPrivateProfile.setSelf_publicProfile_DHTHash(debitorPublicProfile.getId());
		debitorPrivateProfile.addTransaction(bill.getId());
		
		creditorPrivateProfile.setSelf_publicProfile_DHTHash(creditorPublicProfile.getId());
		creditorPrivateProfile.addTransaction(bill.getId());
		
		storePublicProfilesAndBill(debitorPublicProfile, creditorPublicProfile, bill);
		
		P2PLayer debitorP2PLayer = new P2PLayer(new Environment());
		P2PLayer creditorP2PLayer = new P2PLayer(new Environment());
		Core debitorCore = new Core(debitorP2PLayer, debitorPrivateProfile);
		Core creditorCore = new Core(creditorP2PLayer, creditorPrivateProfile);
		
		Controller debitorController = new Controller(debitorCore);
		Controller creditorController = new Controller(creditorCore);
		
		
		//Console gui
		TerminalGUI creditorConsoleGUI = new TerminalGUI(creditorController, 9004, "192.168.1.44", 9003);
		TerminalGUI debitorConsoleGUI = new TerminalGUI(debitorController, 9005, "192.168.1.44", 9003);
		
		//System.exit(0);
		
		
		//Graphic GUI
		//MainView debitorGraphicGUI = new MainView(debitorController, 9005, "192.168.1.37", 9003);
		//MainView creditorGraphicGUI = new MainView(creditorController, 9004, "192.168.1.37", 9003);
		
	}
	
	/**
	 * This function create a FreePastry node which will be the boot node
	 */
	private void createBootNode(int bindport, String bootAddress, int bootport) throws IOException {
		InetSocketAddress bootInetSocketAddress = P2PUtil.createInetSocketAddress(bootAddress, bootport);
		
		node = P2PUtil.createPastryNode(env, bindport);
		
		//It is created a factory to generate the keys of the values stored into the DHT. The algorithm used is SHA-1
		idFactory = new PastryIdFactory(env);
		
		//The next step is create a Past instance for the self node
		
		//The storage directory where the Past instance will be created must be specified
		String storageDirectory = "./storage" + node.getId().hashCode();
		
		Storage storage = P2PUtil.createPastStorage(storageDirectory, idFactory, node, true);
		
		past = P2PUtil.createPast(storage, idFactory, node);
		
		P2PUtil.connectNode(node, bootInetSocketAddress);
	}
	
	/**
	 * This method stores the profiles created by this class before running the simulation
	 * @param debitorPublicProfile
	 * @param creditorPublicProfile
	 * @param bill
	 */
	private void storePublicProfilesAndBill(PastContent debitorPublicProfile, PastContent creditorPublicProfile, PastContent bill){
		past.insert(debitorPublicProfile, new InsertContinuationImpl(FileType.PUBLIC_PROFILE_ENTRY.toString() + ".debitorPublicProfile"));
		
		past.insert(creditorPublicProfile, new InsertContinuationImpl(FileType.PUBLIC_PROFILE_ENTRY.toString() + ".creditorPublicProfile"));
		
		past.insert(bill, new InsertContinuationImpl(FileType.BILL_ENTRY.toString() + ".Bill"));
		
	}
	
	/**
	 * Private class used in the insertion of the public profiles and bill. It receives the notification
	 * of successful or failed storage after a time because this process is not instantaneous
	 * @author yamal
	 *
	 */
	private class InsertContinuationImpl implements Continuation<Boolean[], Exception> {
		private String contentID;
		
		public InsertContinuationImpl(String contentID) {
			this.contentID = contentID;
		}
		
		//Method called when there has been an error during insert call
		@Override
		public void receiveException(Exception arg0) {
			System.err.println("Failed content " + contentID + " storage.");
		}

		//Method called when insert call has successfully finish. It receibes a Boolean array
		//to now how many replicas have been stored (True -> stored; False -> not stored)
		//Metodo que es llamado cuando se ha finalizado el almacenamiento del contenido. Recibe
		//un array de Boolean para saber cuantas de las replicas se han almacenado y cuantas no
		//true -> almacenado; false -> no almacenado
		@Override
		public void receiveResult(Boolean[] arg0) {
			//The number of successful stores is counted and printed
			int successfullStores = 0;
			for (int i = 0; i < arg0.length; i++){
				if (arg0[i].booleanValue())
					successfullStores++;
			}
			
			System.out.println("Content "+ contentID + " has been stored " + successfullStores + " times.");
		}		
	}
	
	/**
	 * Method used to create a PublicProfile instance
	 * @param userUUID
	 * @param isCreditor
	 * @return PublicProfile
	 */
	private PublicProfile createPublicProfile(UUID userUUID, boolean isCreditor){	
		String self_firstName, self_surnames, self_email, self_address;
		int self_telephone;
		Timestamp timestamp_creation;
		String self_digitalSignature_creation;
		
		if (isCreditor){
			self_firstName = "Juan (Creditor)";
			self_surnames = "Sanchez";
			self_telephone = 555888;
			self_email = "creditor@timebankucm.es";
			self_address = "Elm Street";
			timestamp_creation = new Timestamp(System.currentTimeMillis());
			self_digitalSignature_creation = "FirmaCreditor (pendiente de cambiar tipo)";
		}
		else {
			self_firstName = "Pepe (Debitor)";
			self_surnames = "Garcia";
			self_telephone = 555777;
			self_email = "debitor@timebankucm.es";
			self_address = "Sesame Street";
			timestamp_creation = new Timestamp(System.currentTimeMillis());
			self_digitalSignature_creation = "FirmaDebitor (pendiente de cambiar tipo)";
		}
		
		//A lo mejor se puede hacer un patron factoria para este tipo de cosas
		Id self_last_LedgerEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
		Id self_last_FAMEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.FAM_ENTRY, EntryType.FINAL_ENTRY);
		Id self_last_FBMEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.FBM_ENTRY, EntryType.FINAL_ENTRY);
		
		Id debitorPublicProfile_DHTHash = Util.makeDHTHash(idFactory, userUUID, 0, null, FileType.PUBLIC_PROFILE_ENTRY, EntryType.FINAL_ENTRY);
		
		return new PublicProfile(debitorPublicProfile_DHTHash, 
				self_firstName, self_surnames, self_telephone, self_email, self_address, 
				timestamp_creation, self_digitalSignature_creation, self_last_LedgerEntryDHTHash, 
				self_last_FAMEntryDHTHash, self_last_FBMEntryDHTHash);
	}
	
	/**
	 * This method creates a PrivateProfile for an user with a given UUID 
	 * @param uuid
	 * @return PrivateProfile
	 */
	private PrivateProfile createPrivateProfile(UUID uuid){
		return new PrivateProfile(uuid);
	}
	
	/**
	 * This method creates a Bill with given information from public profile
	 * @param uuid
	 * @param creditorPublicProfile_DHTHash
	 * @param debitorPublicProfile_DHTHash
	 * @return Bill
	 */
	private Bill createBill(UUID uuid, Id creditorPublicProfile_DHTHash, Id debitorPublicProfile_DHTHash){
		String self_transRef = "creditor sample bill";
		String other_transRef = "debitor sample bill";
		Id self_profile_DHTHash = creditorPublicProfile_DHTHash;
		Id other_profile_DHTHash = debitorPublicProfile_DHTHash;
		double actualServiceHours = 4;
		String other_digitalSignature = "FirmaDebitor (pendiente de cambiar tipo)";
		Timestamp timestamp_creation = new Timestamp(System.currentTimeMillis());
		String self_digitalSignature_creation = "FirmaCreditor (pendiente de cambiar tipo)";
		
		Id bill_DHTHash = Util.makeDHTHash(idFactory, uuid, 0, self_transRef, FileType.BILL_ENTRY, null);
		
		return new Bill(bill_DHTHash, self_transRef, other_transRef,
				self_profile_DHTHash, other_profile_DHTHash, actualServiceHours,
				other_digitalSignature, timestamp_creation, self_digitalSignature_creation);
	}
}
