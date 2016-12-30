package demo;

import java.sql.Timestamp;
import java.util.UUID;

import paymentprotocol.model.files.local.PrivateProfile;
import paymentprotocol.model.files.network.persistent.Bill;
import paymentprotocol.model.files.network.persistent.FileType;
import paymentprotocol.model.files.network.persistent.PublicProfile;
import paymentprotocol.model.util.Util;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.pastry.commonapi.PastryIdFactory;

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
	
	PastryIdFactory idFactory;
	
	public Demo(){
		env = new Environment();
		env.getParameters().setString("nat_search_policy","never");
		
		idFactory = new PastryIdFactory(env);
	}
	
	public void runDemo(){
		
		PrivateProfile debitorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		PrivateProfile creditorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		
		PublicProfile debitorPublicProfile = createPublicProfile(debitorPrivateProfile.getUUID(), false);
		PublicProfile creditorPublicProfile = createPublicProfile(creditorPrivateProfile.getUUID(), true);

		Bill bill = createBill(creditorPrivateProfile.getUUID(), creditorPublicProfile.getId(), debitorPublicProfile.getId());
		
		debitorPrivateProfile.setSelf_publicProfile_DHTHash(debitorPublicProfile.getId());
		debitorPrivateProfile.addTransaction(bill.getId());
		creditorPrivateProfile.setSelf_publicProfile_DHTHash(creditorPublicProfile.getId());
		creditorPrivateProfile.addTransaction(bill.getId());
		
		createBootNode();
		
		createStorage();
		
		createFreePastryNode();
		createFreePastryNode();
		
		storePublicProfile();
		storePublicProfile();
		
		
		
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
	
	private PublicProfile createPublicProfile(UUID userUUID, boolean isCreditor){	
		String self_firstName, self_surnames, self_email, self_address;
		int self_telephone;
		Timestamp timestamp_creation;
		String self_digitalSignature_creation;
		
		if (isCreditor){
			self_firstName = "Creditor";
			self_surnames = "Ledebenhoras";
			self_telephone = 555888;
			self_email = "creditor@timebankucm.es";
			self_address = "Elm Street";
			timestamp_creation = new Timestamp(System.currentTimeMillis());
			self_digitalSignature_creation = "FirmaCreditor (pendiente de cambiar tipo)";
		}
		else {
			self_firstName = "Debitor";
			self_surnames = "Debehoras";
			self_telephone = 555777;
			self_email = "debitor@timebankucm.es";
			self_address = "Sesame Street";
			timestamp_creation = new Timestamp(System.currentTimeMillis());
			self_digitalSignature_creation = "FirmaDebitor (pendiente de cambiar tipo)";
		}
		
		//A lo mejor se puede hacer un patron factoria para este tipo de cosas
		Id self_last_LedgerEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.ACCOUNT_LEDGER_ENTRY);
		Id self_last_FAMEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.FAM_ENTRY);
		Id self_last_FBMEntryDHTHash = Util.makeDHTHash(idFactory, userUUID, 1, null, FileType.FBM_ENTRY);
		
		Id debitorPublicProfile_DHTHash = Util.makeDHTHash(idFactory, userUUID, 0, null, FileType.PUBLIC_PROFILE);
		
		return new PublicProfile(debitorPublicProfile_DHTHash, 
				self_firstName, self_surnames, self_telephone, self_email, self_address, 
				timestamp_creation, self_digitalSignature_creation, self_last_LedgerEntryDHTHash, 
				self_last_FAMEntryDHTHash, self_last_FBMEntryDHTHash);
	}
	
	private PrivateProfile createPrivateProfile(UUID uuid){
		return new PrivateProfile(uuid);
	}
	
	private Bill createBill(UUID uuid, Id creditorPublicProfile_DHTHash, Id debitorPublicProfile_DHTHash){
		String self_transRef = "creditor sample bill";
		String other_transRef = "debitor sample bill";
		Id self_profile_DHTHash = creditorPublicProfile_DHTHash;
		Id other_profile_DHTHash = debitorPublicProfile_DHTHash;
		double actualServiceHours = 4;
		String other_digitalSignature = "FirmaDebitor (pendiente de cambiar tipo)";
		Timestamp timestamp_creation = new Timestamp(System.currentTimeMillis());
		String self_digitalSignature_creation = "FirmaCreditor (pendiente de cambiar tipo)";
		
		Id bill_DHTHash = Util.makeDHTHash(idFactory, uuid, 0, self_transRef, FileType.BILL_ENTRY);
		
		return new Bill(bill_DHTHash, self_transRef, other_transRef,
				self_profile_DHTHash, other_profile_DHTHash, actualServiceHours,
				other_digitalSignature, timestamp_creation, self_digitalSignature_creation);
	}
	
	private void storePublicProfile(){
		
	}
	
	private void storeBill(){
		
	}
	
	private void run(){
		
	}
}
