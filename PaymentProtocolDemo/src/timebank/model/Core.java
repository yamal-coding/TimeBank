package timebank.model;

import java.util.HashMap;
import java.util.Map;

import rice.p2p.commonapi.Id;
import rice.p2p.past.PastContent;
import timebank.factory.PastEntryFactory;
import timebank.model.files.local.PrivateProfile;
import timebank.model.files.network.persistent.AccountLedgerEntry;
import timebank.model.files.network.persistent.Bill;
import timebank.model.files.network.persistent.EntryType;
import timebank.model.files.network.persistent.FAMEntry;
import timebank.model.files.network.persistent.FBMEntry;
import timebank.model.files.network.persistent.FileType;
import timebank.model.files.network.persistent.PublicProfile;
import timebank.model.messaging.NotificationPair;
import timebank.model.p2p.P2PLayer;
import timebank.model.util.Util;
import timebank.observer.CoreObserver;
import timebank.observer.GUIObserver;

/**
 * Application core. All the main operations are called in this class.
 * 
 * @author yamal
 *
 */
public class Core implements CoreObserver {
	//Object used to call P2P methods and delegate these tasks
	private P2PLayer p2pLayer;
	
	//Observer to communicate with GUI
	private GUIObserver guiObserver;
	
	//Information of the current user
	private PrivateProfile privateProfile;
	private PublicProfile publicProfile;
	
	//User's bills
	private volatile Map<String, Bill> loadedBills;
	private volatile int billsToload;
	private volatile int loadBillTrials;
	
	//Notifications associated to each Payment. The transaction reference is used
	private volatile Map<String, NotificationPair> notificationsReceived;
	
	
	/**
	 * Core constructor
	 * @param p2pLayer
	 */
	public Core(P2PLayer p2pLayer, PrivateProfile privateProfile){
		this.loadedBills = new HashMap<String, Bill>();
		this.notificationsReceived = new HashMap<String, NotificationPair>();
		this.billsToload = privateProfile.getTransactionsDHTHashes().size();
		this.loadBillTrials = 0;
		this.p2pLayer = p2pLayer;
		this.privateProfile = privateProfile;
		this.p2pLayer.addObserver(this);
	}
	
	/**
	 * Method to add the GUIObserver to this class
	 * @param obs
	 */
	public synchronized void addObserver(GUIObserver obs){
		this.guiObserver = obs;
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAddress
	 * @param bootport
	 */
	public synchronized void connect(int bindport, String bootAddress, int bootport){
		switch(p2pLayer.join(bindport, bootAddress, bootport, this)){
			case NODE_ALREADY_CONNECTED:
				guiObserver.onNodeAlreadyConnected();
				break;
			case FAILED_CONNECTION:
				guiObserver.failedConnection();
				break;
			default: //CONNECTION_SUCCESSFUL
				//User public profile is loaded from DHT
				//The implemented observer method "onLookupPublicProfile" notifies the error
				//or successful to the GUI
				loadPublicProfile();

				try{
					//It is not necessary a successfully public profile load to load transactions
					//but the response has to be received firstly. The implemented observer method
					//"onLookupPublicProfile" wakes up this waiting thread
					wait();
					
					//User transactions are loaded from DHT
					if (privateProfile.getTransactionsDHTHashes().isEmpty())
						//notify to the GUI that there are  not any pending transaction
						guiObserver.onNoPendingTransactions();
					else {
						loadBillTrials = 0;
						loadTransactions();
						//It is necessary to wait for having all bills loaded
						//There may be bills that are not loaded but an error is returned in that case
						//The implemented observer method onLookupBill wakes up this waiting thread
						//when the last bill is loaded from DHT
						wait();
						
						for (Map.Entry<String, Bill> entry : loadedBills.entrySet())
							guiObserver.onTransactionLoaded(entry.getValue().getSelf_transRef());
						
					}
				} catch (InterruptedException e) {
					guiObserver.failedConnection();
				}
		}
	}
	
	public synchronized void paymentProtocolDebitorPhase1(String transRef, String comment, int degreeOfStisfaction){
		//With the transaction reference given, the corresponding bill is loaded
		Bill bill = loadedBills.get(transRef);
		
		//The last accountLedgerEntry must be loaded from DHT to calculate the next parameters:
		//ledgerEntryNum
		//pre-balance
		//self_previous_ledgerEntry_DHTHash
		AccountLedgerEntry lastLedger = loadLastAccountLedgerEntry();
		
		//The last FAMEntry must be loaded from DHT to know the next parameters:
		//FAMEntryNum
		//self_previous_FAMEntry_DHTHash
		FAMEntry lastFAM = loadLastFAMEntry();
		
		//The last FBMEntry must be loaded from DHT to know the next parameters:
		//FBMEntryNum
		//self_previous_FAMEntry_DHTHash
		FBMEntry lastFBM = loadLastFBMEntry();
		
		//With the bill information the next partial entries are created
		
		//debitorLedgerEntryPE1
		AccountLedgerEntry debitorLedgerEntryPE1 = PastEntryFactory.createAccountLedgerEntryPE1(bill, 
				lastLedger.getLedgerEntryNum(), lastLedger.getBalance(), lastLedger.getId(), 
				lastFBM.getFBMEntryNum(), false, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//Common field to creditorFADebitorPE1 and debitorFBMEntryPE1
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_ledgerEntry_DHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), privateProfile.getUUID(), 
				debitorLedgerEntryPE1.getLedgerEntryNum(), null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
				
		//creditorFADebitorPE1
		FAMEntry creditorFADebitorPE1 = PastEntryFactory.createFAMEntryPE1(lastFAM.getFAMEntryNum(), 
				lastFAM.getId(), self_ledgerEntry_DHTHash, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//debitorFBMEntryPE1
		FBMEntry debitorFBMEntryPE1 = PastEntryFactory.createFBMEntryPE1(lastFBM.getFBMEntryNum(), 
				lastFBM.getId(), self_ledgerEntry_DHTHash, comment, degreeOfStisfaction, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//store the corresponding entries into DHT
		storeFilesDebitorPaymentProtocolPhase1(debitorLedgerEntryPE1, creditorFADebitorPE1, debitorFBMEntryPE1);
		
		//the hashes and ids of the entries are sent as NotificationPairs objects to the creditor
		//Notify when p2pLayer notifies to this class the successful storage
	}

	private void storeFilesDebitorPaymentProtocolPhase1(AccountLedgerEntry debitorLedgerEntryPE1,
			FAMEntry creditorFADebitorPE1, FBMEntry debitorFBMEntryPE1) {
		p2pLayer.put(debitorLedgerEntryPE1, "debitorLedgerEntryPE1", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFADebitorPE1, "creditorFADebitorPE1", FileType.FAM_ENTRY);
		p2pLayer.put(debitorFBMEntryPE1, "debitorFBMEntryPE1", FileType.FBM_ENTRY);
	}


	
	public void paymentProtocolCreditorPhase1(){
		//With the NotificationPairs (id and hash) received from debitor, the creditor loads
		//the corresponding files
		
		p2pLayer.get(debitorLedgerEntryPE1Notification.getHash(), debitorLedgerEntryPE1Notification.getId(), FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.get(creditorFADebitorPE1Notification.getHash(), creditorFADebitorPE1Notification.getId(), FileType.FAM_ENTRY);
		p2pLayer.get(debitorFBMEntryPE1Notification.getHash(), debitorFBMEntryPE1Notification.getId(), FileType.FBM_ENTRY);
		
		
		//Wait until the trhee files are successfully loaded
		//wait()
		
		//Creditor validates these files. An error is returned if one or more files are not well formed
		if (validateLedgerEntryPE1(debitorLedgerEntryPE1)){
			if (validateFAMEntryPE1(creditorFADebitorPE1)){
				if (validateFBMEntryPE1(debitorFBMEntryPE1))
					//Succesful validation
				else
					//Validation FBMEntryPE1 error
			}
			else
				//Validation FAMEntryPE1 error
		}
		else
			//Validation ledgerEntryPE1
	}
	
	public void paymentProtocolCreditorPhase2(String transRef, String comment, int degreeOfSatisfaction){
		//Creditor loads from DHT the bill associated to the current payment
		Bill bill = loadedBills.get(transRef);
		
		//The last accountLedgerEntry must be loaded from DHT to calculate the next parameters:
		//ledgerEntryNum
		//pre-balance
		//self_previous_ledgerEntry_DHTHash
		AccountLedgerEntry lastLedger = loadLastAccountLedgerEntry();
		
		//The last FAMEntry must be loaded from DHT to know the next parameters:
		//FAMEntryNum
		//self_previous_FAMEntry_DHTHash
		FAMEntry lastFAM = loadLastFAMEntry();
		
		//The last FBMEntry must be loaded from DHT to know the next parameters:
		//FBMEntryNum
		//self_previous_FAMEntry_DHTHash
		FBMEntry lastFBM = loadLastFBMEntry();
		
		//With bill information creates the next partial entries
		//creditorLedgerPE1
		AccountLedgerEntry creditorLedgerEntryPE1 = PastEntryFactory.createAccountLedgerEntryPE1(bill, 
				lastLedger.getLedgerEntryNum(), lastLedger.getBalance(), lastLedger.getId(), 
				lastFBM.getFBMEntryNum(), true, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//Common field to debitorFACReditorPE1 and creditorFBMEntryPE1
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_ledgerEntry_DHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), privateProfile.getUUID(), 
				creditorLedgerEntryPE1.getLedgerEntryNum(), null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
		
		//debitorFACreditorPE1
		FAMEntry debitorFACreditorPE1 = PastEntryFactory.createFAMEntryPE1(lastFAM.getFAMEntryNum(), 
				lastFAM.getId(), self_ledgerEntry_DHTHash, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//creditorFBMPE1
		FBMEntry creditorFBMEntryPE1 = PastEntryFactory.createFBMEntryPE1(lastFBM.getFBMEntryNum(), 
				lastFBM.getId(), self_ledgerEntry_DHTHash, comment, degreeOfSatisfaction, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
				
		//With the three files loaded previously the next partial entries are created
		//debitorLedgerPE2
		AccountLedgerEntry debitorLedgerEntryPE2 = PastEntryFactory.createAccountLedgerEntryPE2();
		//creditorFADebitorPE2
		FAMEntry creditorFADebitorPE2 = PastEntryFactory.createFAMEntryPE2();
		//debitorFBEntryPE2
		FBMEntry debitorFBMEntryPE2 = PastEntryFactory.createFBMEntryPE2();
		
		//store the corresponding entries into DHT
		storeFilesCreditorPaymentProtocolPhase2(creditorLedgerEntryPE1, debitorFACreditorPE1, creditorFBMEntryPE1,
				debitorLedgerEntryPE2, creditorFADebitorPE2, debitorFBMEntryPE2);
		
		//Notify when p2pLayer notifies to this class the successful storage
		wait();
		//the hashes and ids of the entries are sent as NotificationPairs objects to the debitor
		p2pLayer.sendNotification(nh, new NotificationPair(from, to, id, hash, ref));
		p2pLayer.sendNotification(nh, new NotificationPair(from, to, id, hash, ref));
		p2pLayer.sendNotification(nh, new NotificationPair(from, to, id, hash, ref));
	}

	private void storeFilesCreditorPaymentProtocolPhase2(AccountLedgerEntry creditorLedgerEntryPE1,
			FAMEntry debitorFACreditorPE1, FBMEntry creditorFBMEntryPE1, AccountLedgerEntry debitorLedgerEntryPE2,
			FAMEntry creditorFADebitorPE2, FBMEntry debitorFBMEntryPE2) {
		p2pLayer.put(creditorLedgerEntryPE1, "creditorLedgerEntryPE1", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(debitorFACreditorPE1, "debitorFACreditorPE1", FileType.FAM_ENTRY);
		p2pLayer.put(creditorFBMEntryPE1, "creditorFBMEntryPE1", FileType.FBM_ENTRY);
		p2pLayer.put(debitorLedgerEntryPE2, "debitorLedgerEntryPE2", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFADebitorPE2, "creditorFADebitorPE2", FileType.FAM_ENTRY);
		p2pLayer.put(debitorFBMEntryPE2, "debitorFBMEntryPE2", FileType.FBM_ENTRY);
	}
	
	public void paymentProtocolDebitorPhase2(){
		//With the NotificationPairs (id and hash) received from creditor, the debitor loads
		//the corresponding files
		p2pLayer.get(creditorLedgerEntryPE1Notification.getHash(), creditorLedgerEntryPE1Notification.getId(), FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.get(debitorFACreditorPE1Notification.getHash(), debitorFACreditorPE1Notification.getId(), FileType.FAM_ENTRY);
		p2pLayer.get(creditorFBMEntryNotification.getHash(), creditorFBMEntryNotification.getId(), FileType.FBM_ENTRY);
		p2pLayer.get(debitorLedgerEntryPE2Notification.getHash(), debitorLedgerEntryPE2Notification.getId(), FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.get(creditorFADebitorPE2Notification.getHash(), creditorFADebitorPE2Notification.getId(), FileType.FAM_ENTRY);
		p2pLayer.get(debitorFBMEntryPE2Notification.getHash(), debitorFBMEntryPE2Notification.getId(), FileType.FBM_ENTRY);
	
		
		//Debitor validates these files. An error is returned if one or more files are not well formed

		//Wait until the trhee files are successfully loaded
		//wait()
		
		//Creditor validates these files. An error is returned if one or more files are not well formed
		if (validateLedgerEntryPE1(creditorLedgerEntryPE1)){
			if (validateFAMEntryPE1(debitorFACreditorPE1)){
				if (validateFBMEntryPE1(creditorFBMEntryPE1))
					if (validateLedgerEntryPE2(debitorLedgerEntryPE2))
						if (validateFAMEntryPE2(creditorFADebitorPE2)){
							if (validateFBMEntryPE2(debitorFBMEntryPE2))
								//successful validation
							else
								//Validation FBMEntryPE2 error
						}
						else
							//Validation FAMEntryPE2 error
					}
					else
						//Validation ledgerEntryPE2 error
				}
				else
					//Validation FBMEntryPE1 error
			}else
				//Validation FAMEntryPE1 error
		}else
			//Validation ledgerEntryPE1
		
	}
	
	public void paymentProtocolDebitorPhase3(){
		//With the debitor corresponding three partial files the next final entries are created:
		
		//debitorLedger
		AccountLedgerEntry debitorLedgerEntry = PastEntryFactory.createFinalAccountLedgerEntry();
		//creditorFADebitor
		FAMEntry creditorFADebitor = PastEntryFactory.createFinalFAMEntry();
		//debitorFBEntry
		FBMEntry debitorFBMEntry = PastEntryFactory.createFinalFBMEntry();
		
		//The previous files are stored into the DHT
		
		//The debitor create the next partial entries with the other three partial entries loaded
		//creditorLedgerEntryPE2
		AccountLedgerEntry creditorLedgerEntryPE2 = PastEntryFactory.createAccountLedgerEntryPE2();
		//debitorFACreditorPE2
		FAMEntry debitorFACreditorPE2 = PastEntryFactory.createFAMEntryPE2();
		//creditorFBMEntryPE2
		FBMEntry creditorFBMEntryPE2 = PastEntryFactory.createFBMEntryPE2();
		
		//The previous entries are stored into the DHT
		storeFIlesDebitorPaymentProtocolPhase3(debitorLedgerEntry, creditorFADebitor, debitorFBMEntry,
				creditorLedgerEntryPE2, debitorFACreditorPE2, creditorFBMEntryPE2);
		
		
		//the hashes and ids of the entries are sent as NotificationPairs objects to the creditor
	}

	private void storeFIlesDebitorPaymentProtocolPhase3(AccountLedgerEntry debitorLedgerEntry,
			FAMEntry creditorFADebitor, FBMEntry debitorFBMEntry, AccountLedgerEntry creditorLedgerEntryPE2,
			FAMEntry debitorFACreditorPE2, FBMEntry creditorFBMEntryPE2) {
		p2pLayer.put(debitorLedgerEntry, "debitorLedgerEntryPE2", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFADebitor, "creditorFADebitorPE2", FileType.FAM_ENTRY);
		p2pLayer.put(debitorFBMEntry, "debitorFBMEntryPE2", FileType.FBM_ENTRY);
		p2pLayer.put(creditorLedgerEntryPE2, "creditorLedgerEntryPE1", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(debitorFACreditorPE2, "debitorFACreditorPE1", FileType.FAM_ENTRY);
		p2pLayer.put(creditorFBMEntryPE2, "creditorFBMEntryPE1", FileType.FBM_ENTRY);
	}
	
	public void paymentProtocolCreditorPhase3(String transRef){
		//With the NotificationPairs (id and hash) received from debitor, the creditor loads
		//the corresponding files
		NotificationPair creditorLedgerEntryPE2Notification = notificationsReceived.get("");
		NotificationPair debitorFACreditorPE2Notification = notificationsReceived.get("");
		NotificationPair creditorFBMEntryPE2Notification = notificationsReceived.get("");
		
		p2pLayer.get(creditorLedgerEntryPE2Notification.getHash(), creditorLedgerEntryPE2Notification.getId(), FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.get(debitorFACreditorPE2Notification.getHash(), debitorFACreditorPE2Notification.getId(), FileType.FAM_ENTRY);
		p2pLayer.get(creditorFBMEntryPE2Notification.getHash(), creditorFBMEntryPE2Notification.getId(), FileType.FBM_ENTRY);
		
		//Wait until the trhee files are successfully loaded
		//wait()
		
		//Creditor validates these files. An error is returned if one or more files are not well formed
		if (validateLedgerEntryPE1(creditorLedgerEntryPE2)){
			if (validateFAMEntryPE1(debitorFACreditorPE2)){
				if (validateFBMEntryPE1(creditorFBMEntryPE2))
					//Succesful validation
				else
					//Validation FBMEntryPE2 error
			}
			else
				//Validation FAMEntryPE2 error
		}
		else
			//Validation ledgerEntryPE2
	}
	
	public void paymentProtocolCreditorPhase4(){
		//With the creditor corresponding three partial files the next final entries are created:

		//creditorLedger
		AccountLedgerEntry creditorLedgerEntry = PastEntryFactory.createFinalAccountLedgerEntry();
		//debitorFACreditor
		FAMEntry debitorFACreditor = PastEntryFactory.createFinalFAMEntry();
		//creditorFBEntry
		FBMEntry creditorFBMEntry = PastEntryFactory.createFinalFBMEntry();
	}
	
	private AccountLedgerEntry loadLastAccountLedgerEntry(){
		return null;
	}

	private FAMEntry loadLastFAMEntry(){
		return null;
	}
	
	private FBMEntry loadLastFBMEntry(){
		
		return null;
	}
	
	
	/**
	 * This method calls p2p layer to look up the user public profile in DHT
	 */
	private void loadPublicProfile(){
		p2pLayer.get(privateProfile.getSelf_publicProfile_DHTHash(), "UserPublicProfile", FileType.PUBLIC_PROFILE_ENTRY);
	}
	
	/**
	 * This method loads the transactions of the current user. It does not returns any object because
	 * the lookup method over the DHT is not instantaneous, p2pLayer will notify this class when bills reception
	 * is completed
	 */
	private void loadTransactions(){
		for (Id key : privateProfile.getTransactionsDHTHashes())
			//queda pendiente aniadir la llamada con el id de la transaccion. o quitarlo de los parametros
			p2pLayer.get(key, "", FileType.BILL_ENTRY);
	}	 
	
	/**
	 * Method called when a notification is received from another node
	 */
	@Override
	public synchronized void onReceiveNotification(NotificationPair notificationPair) {
		notificationsReceived.put(notificationPair.getRef(), notificationPair);
		guiObserver.onReceiveNotification("");
	}

	/**
	 * Finished storage into the DHT.
	 * If an error has occurred, the boolean error variable is set to true
	 * @param msg
	 * @param error
	 */
	@Override
	public synchronized void onFinishedStorage(String msg, boolean error) {
		if (error){//there has been errors during the "insert" call into the DHT
			
		}
		else{//Successful insertion into the DHT
			
		}
	}

	/**
	 * Successfully Bill request
	 * If an error has occurred, the boolean error variable is set to true
	 * @param bill
	 * @param error
	 * @param msg
	 */
	@Override
	public synchronized void onLookupBill(PastContent bill, boolean error, String msg) {
		if (error){
			//Handle error
		}
		else{
			try {
				Bill billEntry = (Bill) bill;
				//to do
				if (!loadedBills.containsKey(billEntry.getSelf_transRef()))
					loadedBills.put(billEntry.getSelf_transRef(), billEntry);
				else {
					//Bill already loaded
				}
			}
			catch (ClassCastException e){
				//Handle error
			}
		}
		
		loadBillTrials++;
		
		if (loadBillTrials == billsToload){
			//notify to this slept thread waiting
			this.notify();
		}
	}

	/**
	 * successfully AccountLedgerRequest
	 * If an error has occurred, the boolean error variable is set to true
	 * @param accountLedger
	 * @param error
	 * @param msg
	 */
	@Override
	public synchronized void onLookupAccountLedger(PastContent accountLedger, boolean error, String msg) {
		if (error){
			//Handle error
		}
		else{
			try {
				AccountLedgerEntry ledger = (AccountLedgerEntry) accountLedger;
				//to do
			}
			catch (ClassCastException e){
				//Handle error
			}
		}
	}

	/**
	 * Successfully FAMEntry request
	 * If an error has occurred, the boolean error variable is set to true
	 * @param famEntry
	 * @param error
	 * @param msg
	 */
	@Override
	public synchronized void onLookupFAMEntry(PastContent famEntry, boolean error, String msg) {
		if (error){
			//Handle error
		}
		else{
			try {
				FAMEntry fam = (FAMEntry) famEntry;
				//to do
			}
			catch (ClassCastException e){
				//Handle error
			}
		}
	}

	/**
	 * Successfully FBMEntry request
	 * If an error has occurred, the boolean error variable is set to true
	 * @param fbmEntry
	 * @param error
	 * @param msg
	 */
	@Override
	public synchronized void onLookupFBMEntry(PastContent fbmEntry, boolean error, String msg) {
		if (error){
			//Handle error
		}
		else{
			try {
				FBMEntry fbm = (FBMEntry) fbmEntry;
				//to do
			}
			catch (ClassCastException e){
				//Handle error
			}
		}
	}
	
	/**
	 * Successfully PublicProfile request
	 * If an error has occurred, the boolean error variable is set to true
	 * @param publicProfile
	 * @param error
	 * @param msg
	 */
	@Override
	public synchronized void onLookupPublicProfile(PastContent publicProfile, boolean error, String msg) {
		if (error){
			guiObserver.onFailedPublicProfileLoad();
		}
		else{
			try{
				this.publicProfile = (PublicProfile) publicProfile;
				guiObserver.onPublicProfileLoaded(this.publicProfile.getSelf_firstName(), this.publicProfile.getSelf_surnames());
			}
			catch (ClassCastException e) {
				guiObserver.onFailedPublicProfileLoad();
			}
		}
		notify();
	}
}