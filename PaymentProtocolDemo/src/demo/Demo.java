package demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.UUID;

import paymentprotocol.model.files.local.PrivateProfile;
import paymentprotocol.model.files.network.persistent.Bill;
import paymentprotocol.model.files.network.persistent.FileType;
import paymentprotocol.model.files.network.persistent.PublicProfile;
import paymentprotocol.model.messaging.NotificationHandler;
import paymentprotocol.model.p2p.P2PUtil;
import paymentprotocol.model.util.Util;
import rice.Continuation;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;
import rice.p2p.past.Past;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.persistence.Storage;

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
	
	public Demo(){
		env = new Environment();
		env.getParameters().setString("nat_search_policy","never");
	}
	
	public void runDemo(int bindport, String bootAddress, int bootport) throws IOException {
		
		PrivateProfile debitorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		PrivateProfile creditorPrivateProfile = createPrivateProfile(java.util.UUID.randomUUID());
		
		ContentHashPastContent debitorPublicProfile = createPublicProfile(debitorPrivateProfile.getUUID(), false);
		ContentHashPastContent creditorPublicProfile = createPublicProfile(creditorPrivateProfile.getUUID(), true);

		ContentHashPastContent bill = createBill(creditorPrivateProfile.getUUID(), creditorPublicProfile.getId(), debitorPublicProfile.getId());
		
		debitorPrivateProfile.setSelf_publicProfile_DHTHash(debitorPublicProfile.getId());
		debitorPrivateProfile.addTransaction(bill.getId());
		creditorPrivateProfile.setSelf_publicProfile_DHTHash(creditorPublicProfile.getId());
		creditorPrivateProfile.addTransaction(bill.getId());
		
		createBootNode(bindport, bootAddress, bootport);
		
		storePublicProfilesAndBill(debitorPublicProfile, creditorPublicProfile, bill);
		
		
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

	private void storePublicProfilesAndBill(ContentHashPastContent debitorPublicProfile, ContentHashPastContent creditorPublicProfile, ContentHashPastContent bill){
		past.insert(debitorPublicProfile, new Continuation<Boolean[], Exception>() {
			
			@Override
			public void receiveResult(Boolean[] arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receiveException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		past.insert(creditorPublicProfile, new Continuation<Boolean[], Exception>() {
			
			@Override
			public void receiveResult(Boolean[] arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receiveException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		past.insert(bill, new Continuation<Boolean[], Exception>() {
			
			@Override
			public void receiveResult(Boolean[] arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receiveException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
