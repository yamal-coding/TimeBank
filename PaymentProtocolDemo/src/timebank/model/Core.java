package timebank.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.past.PastContent;
import rice.pastry.leafset.LeafSet;
import timebank.factory.PastEntryFactory;
import timebank.model.exception.NodeNotInitializedException;
import timebank.model.exception.NonExistingNotificationException;
import timebank.model.files.local.PrivateProfile;
import timebank.model.files.network.persistent.AccountLedgerEntry;
import timebank.model.files.network.persistent.Bill;
import timebank.model.files.network.persistent.DHTEntry;
import timebank.model.files.network.persistent.EntryType;
import timebank.model.files.network.persistent.FAMEntry;
import timebank.model.files.network.persistent.FBMEntry;
import timebank.model.files.network.persistent.FileType;
import timebank.model.files.network.persistent.PublicProfile;
import timebank.model.messaging.Messenger;
import timebank.model.messaging.Notification;
import timebank.model.messaging.NotificationPaymentPhase1;
import timebank.model.messaging.NotificationPaymentPhase2;
import timebank.model.messaging.NotificationPaymentPhase3;
import timebank.model.messaging.NotificationPaymentPhase4;
import timebank.model.p2p.P2PLayer;
import timebank.model.util.Util;
import timebank.model.validation.EntryValidator;
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
	
	//Object used to validate each partial entry during payment process 
	private EntryValidator entryValidator;
	
	//Object used to send, received and store messages from/to others nodes
	private Messenger messenger;
		
	//Observer to communicate with GUI
	private GUIObserver guiObserver;
	
	//Information of the current user
	private PrivateProfile privateProfile;
	private PublicProfile publicProfile;
	
	//User's bills
	private Map<String, Bill> loadedBills;
	private int billsToload;
	private int loadBillTrials;
	
	//Entries loaded from DHT. They will be removed from the hash map when used
	private Map<Id, DHTEntry> loadedDHTEntries;
	
	//these boolean values indicate if it is necessary to load one more DHT entry
	private AccountLedgerEntry lastLedger;
	private boolean loadMoreLedger;
	
	private FAMEntry lastFAM;
	private boolean loadMoreFAM;
	
	private FBMEntry lastFBM;
	private boolean loadMoreFBM;
	
	//This map is required to store the own FAMEntries DHTHashes because we need them in the last payment phase as creditor.
	//This hash is previously calculated in another phase and we need to store it to get it back later in the last payment phase.
	private Map<String, Id> storedFAMEntryDHTHashes;
	
	//Semaphores to suspend the execution when a DHT Content is looked for. These calls to the P2PLayer
	//are handle in a new Thread because FreePastry is implemented by this way
	private Semaphore loadPublicProfileSemaphore;
	private Semaphore loadTransactionsSemaphore;
	private Semaphore loadAccountLedgerSemaphore;
	private Semaphore loadFAMSemaphore;
	private Semaphore loadFBMSemaphore;
	
	/**
	 * Core constructor
	 * @param p2pLayer
	 * @param privateProfile
	 */
	public Core(P2PLayer p2pLayer, PrivateProfile privateProfile){
		this.loadMoreLedger = false;
		this.loadMoreFAM = false;
		this.loadMoreFBM = false;
		this.loadedBills = new HashMap<String, Bill>();
		this.loadedDHTEntries = new HashMap<Id, DHTEntry>();
		this.storedFAMEntryDHTHashes = new HashMap<String, Id>();
		this.billsToload = privateProfile.getTransactionsDHTHashes().size();
		this.loadBillTrials = 0;
		this.privateProfile = privateProfile;
		this.p2pLayer = p2pLayer;
		this.entryValidator = new EntryValidator();	
		this.loadPublicProfileSemaphore = new Semaphore(0);
		this.loadTransactionsSemaphore = new Semaphore(0);
		this.loadAccountLedgerSemaphore = new Semaphore(0);
		this.loadFAMSemaphore = new Semaphore(0);
		this.loadFBMSemaphore = new Semaphore(0);
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
	 * Method called when a transaction is selected in the GUI to view its details
	 * @param ref Transaction reference
	 */
	public void viewTransaction(String ref){
		Bill bill = loadedBills.get(ref);
		boolean isCreditor = bill.getSelf_profile_DHTHash().equals(publicProfile.getId()); 
		guiObserver.onViewTransaction(ref, bill.getActualServiceHours(), isCreditor);
	}
	
	/**
	 * Method called when the user wants to view his public profile information
	 */
	public void viewPublicProfile(){
		//TODO Hay que comprobar primero que el nodo esta conectado para no enviar un publicProfile nulo
		//y notificar en caso de error
		
		if (publicProfile == null)
			guiObserver.onFailedPublicProfileLoad();
		else
			guiObserver.onViewPublicProfile(publicProfile.getSelf_firstName(), publicProfile.getSelf_surnames(),
					publicProfile.getSelf_telephone(), publicProfile.getSelf_email());
	}
	
	/**
	 * When a notification is selected in the GUI, each type of notification calls its own handleNotification method
	 * @param ref
	 */
	public void handleNotification(String ref){
		try {
			messenger.getNotification(ref).handleNotification(guiObserver);
		} catch (NonExistingNotificationException e) {
			// TODO Auto-generated catch block
			
		}
	}
	
	/**
	 * Method used to connect a node to the FreePastry network
	 * @param bindport
	 * @param bootAddress
	 * @param bootport
	 */
	public void connect(int bindport, String bootAddress, int bootport){
		switch(p2pLayer.join(bindport, bootAddress, bootport, this)){
			case NODE_ALREADY_CONNECTED:
				guiObserver.onNodeAlreadyConnected();
				break;
			case FAILED_CONNECTION:
				guiObserver.failedConnection();
				break;
			default: //CONNECTION_SUCCESSFUL
				try {
					this.messenger = new Messenger(p2pLayer.getNode(), this);
					
					//User public profile is loaded from DHT
					//The implemented observer method "onLookupPublicProfile" notifies the error
					//or successful to the GUI
					loadPublicProfile();
					
					//It is not necessary a successfully public profile load to load transactions
					//but the response has to be received first. The implemented observer method
					//"onLookupPublicProfile" release the semaphore and the execution in this point
					//can continue
					loadPublicProfileSemaphore.acquire();
					
					//User transactions are loaded from DHT
					if (privateProfile.getTransactionsDHTHashes().isEmpty())
						//notify to the GUI that there are  not any pending transaction
						guiObserver.onNoPendingTransactions();
					else {
						loadBillTrials = 0;
						loadTransactions();
						//It is necessary to wait for having all bills loaded
						//There may be bills that are not loaded but an error is returned in that case
						//The implemented observer method onLookupBill release the semaphore
						//when the last bill is loaded from DHT
						loadTransactionsSemaphore.acquire();
						
						for (Map.Entry<String, Bill> entry : loadedBills.entrySet()){
							//if (entry.getValue().getSelf_profile_DHTHash().equals(publicProfile.getId()))
								guiObserver.onTransactionLoaded(entry.getValue().getSelf_transRef());
							//else
								//guiObserver.onTransactionLoaded(entry.getValue().getOther_transRef());
						}
					}
					
					guiObserver.onSuccesfulConnection();
				} catch (InterruptedException e) {
					guiObserver.failedConnection();
				}
				catch (NodeNotInitializedException e) {
					guiObserver.failedConnection();
				}
		}
	}
	
	/**
	 * This method is called when a debtor of a payment starts the payment
	 * @param transRef
	 * @param comment
	 * @param degreeOfStisfaction
	 */
	public void paymentProtocolDebtorPhase1(String transRef, String comment, int degreeOfStisfaction){
		//With the transaction reference given, the corresponding bill is loaded
		Bill bill = loadedBills.get(transRef);
		
		//The last accountLedgerEntry must be loaded from DHT to calculate the next parameters:
		//ledgerEntryNum
		//pre-balance
		//self_previous_ledgerEntry_DHTHash
		loadLastAccountLedgerEntry();
		
		//The last FAMEntry must be loaded from DHT to know the next parameters:
		//FAMEntryNum
		//self_previous_FAMEntry_DHTHash
		loadLastFAMEntry();
		
		//The last FBMEntry must be loaded from DHT to know the next parameters:
		//FBMEntryNum
		//self_previous_FAMEntry_DHTHash
		loadLastFBMEntry();
		
		//With the bill information the next partial entries are created
		
		//debitorLedgerEntryPE1
		AccountLedgerEntry debitorLedgerEntryPE1 = PastEntryFactory.createAccountLedgerEntryPE1(bill, 
				(lastLedger == null) ? 1 : lastLedger.getLedgerEntryNum(), (lastLedger == null) ? 0 : lastLedger.getBalance(), (lastLedger == null) ? null : lastLedger.getId(), 
				(lastFBM == null) ? 1 : lastFBM.getFBMEntryNum(), false, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//Common field to creditorFADebitorPE1 and debitorFBMEntryPE1
		//This hash is directly calculated at this point because files in FreePastry are not mutable
		Id self_ledgerEntry_DHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), privateProfile.getUUID(), 
				debitorLedgerEntryPE1.getLedgerEntryNum(), null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);
				
		//creditorFADebitorPE1
		FAMEntry creditorFADebitorPE1 = PastEntryFactory.createFAMEntryPE1((lastFAM == null) ? 1 : lastFAM.getFAMEntryNum(), 
				(lastFAM == null) ? null : lastFAM.getId(), self_ledgerEntry_DHTHash, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//debitorFBMEntryPE1
		FBMEntry debitorFBMEntryPE1 = PastEntryFactory.createFBMEntryPE1((lastFBM == null) ? 1 : lastFBM.getFBMEntryNum(), 
				(lastFBM == null) ? null : lastFBM.getId(), self_ledgerEntry_DHTHash, comment, degreeOfStisfaction, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
		
		//store the corresponding entries into DHT
		//Notify when p2pLayer notifies to this class the successful storage
		storeFilesDebitorPaymentProtocolPhase1(debitorLedgerEntryPE1, creditorFADebitorPE1, debitorFBMEntryPE1);
		
		if (lastLedger != null)
			loadedDHTEntries.remove(lastLedger.getId());
		if (lastFAM != null)
			loadedDHTEntries.remove(lastFAM.getId());
		if (lastFBM != null)
			loadedDHTEntries.remove(lastFBM.getId());
		
		//the hashes and ids of the entries are sent as Notification objects to the creditor
		try {
			Notification notificationPhase1 = new NotificationPaymentPhase1(p2pLayer.getNode().getId(), transRef,
					debitorLedgerEntryPE1.getId(), creditorFADebitorPE1.getId(), debitorFBMEntryPE1.getId());
			

			guiObserver.onLogMessage("First stage of Payment Phase 1 finished.");
			
			sendNotification(notificationPhase1);
		} catch (NodeNotInitializedException e) {
			// TODO
		}
	}
	
	private void storeFilesDebitorPaymentProtocolPhase1(AccountLedgerEntry debitorLedgerEntryPE1,
			FAMEntry creditorFADebitorPE1, FBMEntry debitorFBMEntryPE1) {
		p2pLayer.put(debitorLedgerEntryPE1, "debitorLedgerEntryPE1", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFADebitorPE1, "creditorFADebitorPE1", FileType.FAM_ENTRY);
		p2pLayer.put(debitorFBMEntryPE1, "debitorFBMEntryPE1", FileType.FBM_ENTRY);
	}

	/**
	 * The creditor receives a notification from debtor with the DHTHashes of the three first partial entries and now the creditor
	 * must validate them
	 * @param notificationRef
	 */
	public void paymentProtocolCreditorPhase1(String notificationRef){
		try {
			NotificationPaymentPhase1 notificationPhase1 = (NotificationPaymentPhase1) messenger.getNotification(notificationRef);
			
			//With the NotificationPairs (id and hash) received from debtor, the creditor loads
			//the corresponding files
			p2pLayer.get(notificationPhase1.getDebitorLedgerPE1Hash(), "debitorLedgerPE1", FileType.ACCOUNT_LEDGER_ENTRY);
			p2pLayer.get(notificationPhase1.getCreditorFADebitorPE1Hash(), "creditorFADebitorPE1", FileType.FAM_ENTRY);
			p2pLayer.get(notificationPhase1.getDebitorFBMPE1Hash(), "debitorFBMPE1", FileType.FBM_ENTRY);
			
			//Wait until the three files are successfully loaded
			loadAccountLedgerSemaphore.acquire();
			loadFAMSemaphore.acquire();
			loadFBMSemaphore.acquire();
			
			AccountLedgerEntry debitorLedgerEntryPE1 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase1.getDebitorLedgerPE1Hash());
			FAMEntry creditorFADebitorPE1 = (FAMEntry) loadedDHTEntries.get(notificationPhase1.getCreditorFADebitorPE1Hash());
			FBMEntry debitorFBMEntryPE1 = (FBMEntry) loadedDHTEntries.get(notificationPhase1.getDebitorFBMPE1Hash());
			
			//Creditor validates these files. An error is returned if one or more files are not well formed
			//TODO
			entryValidator.validatePaymentPhase1();
		
			guiObserver.onPaymentPhase1ValidationSuccess(notificationRef);
		
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Once the three first partial entries have been validated the creditor has to continue with the second payment phase
	 * @param notificationRef
	 * @param comment
	 * @param degreeOfSatisfaction
	 */
	public void paymentProtocolCreditorPhase2(String notificationRef, String comment, int degreeOfSatisfaction){
		try {
			NotificationPaymentPhase1 notificationPhase1 = (NotificationPaymentPhase1) messenger.getNotification(notificationRef);
			String transRef = notificationPhase1.getTransactionReference();
			
			//Creditor loads from DHT the bill associated to the current payment
			Bill bill = loadedBills.get(transRef);
			
			//The last accountLedgerEntry must be loaded from DHT to calculate the next parameters:
			//ledgerEntryNum
			//pre-balance
			//self_previous_ledgerEntry_DHTHash
			loadLastAccountLedgerEntry();
			
			//The last FAMEntry must be loaded from DHT to know the next parameters:
			//FAMEntryNum
			//self_previous_FAMEntry_DHTHash
			loadLastFAMEntry();
			
			//The last FBMEntry must be loaded from DHT to know the next parameters:
			//FBMEntryNum
			//self_previous_FAMEntry_DHTHash
			loadLastFBMEntry();
			
			//With bill information, the creditor creates the next partial entries
			AccountLedgerEntry creditorLedgerEntryPE1 = PastEntryFactory.createAccountLedgerEntryPE1(bill, 
					(lastLedger == null) ? 1 : lastLedger.getLedgerEntryNum(), (lastLedger == null) ? 0 : lastLedger.getBalance(), (lastLedger == null) ? null :lastLedger.getId(), 
					(lastFBM == null) ? 1 :lastFBM.getFBMEntryNum(), true, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			//The creditorLedgerEntry hash is a common field to debitorFACReditorPE1 and creditorFBMEntryPE1
			//and is directly calculated at this point because files in FreePastry are not mutable
			Id creditor_ledgerEntry_DHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), privateProfile.getUUID(), 
					creditorLedgerEntryPE1.getLedgerEntryNum(), null, FileType.ACCOUNT_LEDGER_ENTRY, EntryType.FINAL_ENTRY);

			FAMEntry debitorFACreditorPE1 = PastEntryFactory.createFAMEntryPE1((lastFAM == null) ? 1 :lastFAM.getFAMEntryNum(), 
					(lastFAM == null) ? null :lastFAM.getId(), creditor_ledgerEntry_DHTHash, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			FBMEntry creditorFBMEntryPE1 = PastEntryFactory.createFBMEntryPE1((lastFBM == null) ? 1 :lastFBM.getFBMEntryNum(), 
					(lastFBM == null) ? null :lastFBM.getId(), creditor_ledgerEntry_DHTHash, comment, degreeOfSatisfaction, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			//With the three files loaded previously the next partial entries are created
			AccountLedgerEntry debitorLedgerEntryPE1 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase1.getDebitorLedgerPE1Hash());
			AccountLedgerEntry debitorLedgerEntryPE2 = PastEntryFactory.createAccountLedgerEntryPE2(debitorLedgerEntryPE1, 
							creditorLedgerEntryPE1.getSelf_FBMEntry_DHTHash(), creditor_ledgerEntry_DHTHash, 
							"Creditor digital signature", p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			FAMEntry creditorFADebitorPE1 = (FAMEntry) loadedDHTEntries.get(notificationPhase1.getCreditorFADebitorPE1Hash());
			FAMEntry creditorFADebitorPE2 = PastEntryFactory.createFAMEntryPE2(creditorFADebitorPE1, 
					comment, degreeOfSatisfaction, creditorLedgerEntryPE1.getSelf_FBMEntry_DHTHash(), 
					"Creditor digital signature", p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			
			//The creditor must create at this moment the hash of his FAMEntry
			Id debitorFACreditor_FinalDHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), privateProfile.getUUID(), debitorFACreditorPE1.getFAMEntryNum(), 
					transRef, FileType.FAM_ENTRY, EntryType.FINAL_ENTRY);
			storedFAMEntryDHTHashes.put(transRef, debitorFACreditor_FinalDHTHash);
			FBMEntry debitorFBMEntryPE1 = (FBMEntry) loadedDHTEntries.get(notificationPhase1.getDebitorFBMPE1Hash());
			FBMEntry debitorFBMEntryPE2 = PastEntryFactory.createFBMEntryPE2(debitorFBMEntryPE1, 
					debitorFACreditor_FinalDHTHash, p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			//Now we can remove the three previous partial entries and the notification of phase 1
			messenger.deleteNotification(notificationRef);
			loadedDHTEntries.remove(notificationPhase1.getDebitorLedgerPE1Hash());
			loadedDHTEntries.remove(notificationPhase1.getCreditorFADebitorPE1Hash());
			loadedDHTEntries.remove(notificationPhase1.getDebitorFBMPE1Hash());
			
			//store the corresponding entries into DHT
			storeFilesCreditorPaymentProtocolPhase2(creditorLedgerEntryPE1, debitorFACreditorPE1, creditorFBMEntryPE1,
					debitorLedgerEntryPE2, creditorFADebitorPE2, debitorFBMEntryPE2);
			
			if (lastLedger != null)
				loadedDHTEntries.remove(lastLedger.getId());
			if (lastFAM != null)
				loadedDHTEntries.remove(lastFAM.getId());
			if (lastFBM != null)
				loadedDHTEntries.remove(lastFBM.getId());
			
			//Notify when p2pLayer notifies to this class the successful storage
			//wait();
			
			//the hashes and ids of the entries are sent as NotificationPairs objects to the debtor
			Notification notificationPhase2 = new NotificationPaymentPhase2(p2pLayer.getNode().getId(), transRef, 
					creditorLedgerEntryPE1.getId(), debitorFACreditorPE1.getId(), creditorFBMEntryPE1.getId(),
					debitorLedgerEntryPE2.getId(), creditorFADebitorPE2.getId(), debitorFBMEntryPE2.getId());
			
			sendNotification(notificationPhase2);
			
			guiObserver.onLogMessage("First stage of Payment Phase 2 finished.");
			guiObserver.onDeleteNotification(notificationRef);
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		}
		catch (NodeNotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	/**
	 * The debtor receives a notification from creditor with the DHTHashes of the six second partial entries and now the debtor
	 * must validate them
	 * @param notificationRef
	 */
	public void paymentProtocolDebitorPhase2(String notificationRef){
		try {
			NotificationPaymentPhase2 notificationPhase2 = (NotificationPaymentPhase2) messenger.getNotification(notificationRef);
			
			//With the Notification received from creditor, the debtor loads the corresponding files
			p2pLayer.get(notificationPhase2.getCreditorLedgerPE1Hash(), "creditorLedgerPE1", FileType.ACCOUNT_LEDGER_ENTRY);
			p2pLayer.get(notificationPhase2.getDebitorFACreditorPE1Hash(), "debitorFACreditorPE1", FileType.FAM_ENTRY);
			p2pLayer.get(notificationPhase2.getCreditorFBMPE1Hash(), "creditorFBMPE1", FileType.FBM_ENTRY);
			p2pLayer.get(notificationPhase2.getDebitorLedgerPE2Hash(), "debitorLedgerPE2", FileType.ACCOUNT_LEDGER_ENTRY);
			p2pLayer.get(notificationPhase2.getCreditorFADebitorPE2Hash(), "creditorFADebitorPE2", FileType.FAM_ENTRY);
			p2pLayer.get(notificationPhase2.getDebitorFBMPE2Hash(), "debitorFBMPE2", FileType.FBM_ENTRY);
			
			//Wait until the three files are successfully loaded
			loadAccountLedgerSemaphore.acquire();
			loadFAMSemaphore.acquire();
			loadFBMSemaphore.acquire();
			loadAccountLedgerSemaphore.acquire();
			loadFAMSemaphore.acquire();
			loadFBMSemaphore.acquire();
			
			AccountLedgerEntry debitorLedgerEntryPE2 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase2.getDebitorLedgerPE2Hash());
			FBMEntry debitorFBMEntryPE2 = (FBMEntry) loadedDHTEntries.get(notificationPhase2.getDebitorFBMPE2Hash());
			FAMEntry creditorFADebitorPE2 = (FAMEntry) loadedDHTEntries.get(notificationPhase2.getCreditorFADebitorPE2Hash());
			AccountLedgerEntry creditorLedgerEntryPE1 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase2.getCreditorLedgerPE1Hash());
			FBMEntry creditorFBMEntryPE1 = (FBMEntry) loadedDHTEntries.get(notificationPhase2.getCreditorFBMPE1Hash());
			FAMEntry debitorFACreditorPE1 = (FAMEntry) loadedDHTEntries.get(notificationPhase2.getDebitorFACreditorPE1Hash());
			
			//Creditor validates these files. An error is returned if one or more files are not well formed
			//TODO
			entryValidator.validatePaymentPhase2();
			
			guiObserver.onPaymentPhase2ValidationSuccess(notificationRef);
		
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Once the six second partial entries have been validated the debtor has to continue with the third payment phase
	 * @param notificationRef
	 */
	public void paymentProtocolDebitorPhase3(String notificationRef){
		try {
			NotificationPaymentPhase2 notificationPhase2 = (NotificationPaymentPhase2) messenger.getNotification(notificationRef);
		
			String transRef= notificationPhase2.getTransactionReference();
			
			AccountLedgerEntry debitorLedgerEntryPE2 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase2.getDebitorLedgerPE2Hash());
			FBMEntry debitorFBMEntryPE2 = (FBMEntry) loadedDHTEntries.get(notificationPhase2.getDebitorFBMPE2Hash());
			FAMEntry creditorFADebitorPE2 = (FAMEntry) loadedDHTEntries.get(notificationPhase2.getCreditorFADebitorPE2Hash());
			
			AccountLedgerEntry creditorLedgerEntryPE1 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase2.getCreditorLedgerPE1Hash());
			FBMEntry creditorFBMEntryPE1 = (FBMEntry) loadedDHTEntries.get(notificationPhase2.getCreditorFBMPE1Hash());
			FAMEntry debitorFACreditorPE1 = (FAMEntry) loadedDHTEntries.get(notificationPhase2.getDebitorFACreditorPE1Hash());
			
			//With the three debtor corresponding partial entries, the next three final entries are created:
			
			//The hash of this final entry has been calculated previously and it is stored in the FBMENtry of the debtor
			AccountLedgerEntry debitorLedgerEntry = PastEntryFactory.createFinalAccountLedgerEntry(
					debitorLedgerEntryPE2, debitorFBMEntryPE2.getSelf_ledgerEntry_DHTHash(), "Debtor digital signature");
			
			Id creditorFADebitor_FinalDHTHash = Util.makeDHTHash(p2pLayer.getPastryIdFactory(), 
					privateProfile.getUUID(), creditorFADebitorPE2.getFAMEntryNum(), transRef, FileType.FAM_ENTRY, EntryType.FINAL_ENTRY);
			FAMEntry creditorFADebitor = PastEntryFactory.createFinalFAMEntry(creditorFADebitorPE2, 
					creditorFADebitor_FinalDHTHash, "Debtor digital signature");
			
			FBMEntry debitorFBMEntry = PastEntryFactory.createFinalFBMEntry(debitorFBMEntryPE2, debitorLedgerEntry.getSelf_FBMEntry_DHTHash(), "Debtor digital signature");
			
			//The debtor create the next partial entries with the other three partial entries loaded
			AccountLedgerEntry creditorLedgerEntryPE2 = PastEntryFactory.createAccountLedgerEntryPE2(creditorLedgerEntryPE1,
					debitorFBMEntry.getId(), debitorLedgerEntry.getId(), "Debitor digital signature", p2pLayer.getPastryIdFactory(), privateProfile.getUUID());	
			
			FAMEntry debitorFACreditorPE2 = PastEntryFactory.createFAMEntryPE2(debitorFACreditorPE1, debitorFBMEntry.getComment(), 
					debitorFBMEntry.getNumericalDegreeOfSatisfactionWithService(), debitorFBMEntry.getId(), "Debitor digital signature", p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			FBMEntry creditorFBMEntryPE2 = PastEntryFactory.createFBMEntryPE2(creditorFBMEntryPE1, 
					creditorFADebitor.getId(), p2pLayer.getPastryIdFactory(), privateProfile.getUUID());
			
			messenger.deleteNotification(notificationRef);
			loadedDHTEntries.remove(notificationPhase2.getDebitorLedgerPE2Hash());
			loadedDHTEntries.remove(notificationPhase2.getDebitorFBMPE2Hash());
			loadedDHTEntries.remove(notificationPhase2.getCreditorFADebitorPE2Hash());
			loadedDHTEntries.remove(notificationPhase2.getCreditorLedgerPE1Hash());
			loadedDHTEntries.remove(notificationPhase2.getCreditorFBMPE1Hash());
			loadedDHTEntries.remove(notificationPhase2.getDebitorFACreditorPE1Hash());
			
			//The previous entries are stored into the DHT
			storeFilesDebitorPaymentProtocolPhase3(debitorLedgerEntry, creditorFADebitor, debitorFBMEntry,
					creditorLedgerEntryPE2, debitorFACreditorPE2, creditorFBMEntryPE2);
			
			Notification notificationPhase3 = new NotificationPaymentPhase3(p2pLayer.getNode().getId(), transRef, creditorLedgerEntryPE2.getId(), debitorFACreditorPE2.getId(), creditorFBMEntryPE2.getId());
			
			//The entries hashes are sent as Notification objects to the creditor
			sendNotification(notificationPhase3);

			guiObserver.onLogMessage("First stage of Payment Phase 3 finished.");
			guiObserver.onDeleteNotification(notificationRef);
			
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		} catch (NodeNotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void storeFilesDebitorPaymentProtocolPhase3(AccountLedgerEntry debitorLedgerEntry,
			FAMEntry creditorFADebitor, FBMEntry debitorFBMEntry, AccountLedgerEntry creditorLedgerEntryPE2,
			FAMEntry debitorFACreditorPE2, FBMEntry creditorFBMEntryPE2) {
		p2pLayer.put(debitorLedgerEntry, "debitorLedgerEntry", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFADebitor, "creditorFADebitor", FileType.FAM_ENTRY);
		p2pLayer.put(debitorFBMEntry, "debitorFBMEntry", FileType.FBM_ENTRY);
		p2pLayer.put(creditorLedgerEntryPE2, "creditorLedgerEntryPE2", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(debitorFACreditorPE2, "debitorFACreditorPE2", FileType.FAM_ENTRY);
		p2pLayer.put(creditorFBMEntryPE2, "creditorFBMEntryPE2", FileType.FBM_ENTRY);
	}
	
	/**
	 * The creditor receives a notification from debtor with the DHTHashes of his three last partial entries and now the creditor
	 * must validate them
	 * @param notificationRef
	 */
	public void paymentProtocolCreditorPhase3(String notificationRef){
		try {
			NotificationPaymentPhase3 notificationPhase3 = (NotificationPaymentPhase3) messenger.getNotification(notificationRef);
			
			//With the Notification received from debtor, the creditor loads the corresponding files
			p2pLayer.get(notificationPhase3.getCreditorLedgerPE2(), "creditorLedgerPE2", FileType.ACCOUNT_LEDGER_ENTRY);
			p2pLayer.get(notificationPhase3.getDebitorFACreditorPE2(), "debitorFACreditorPE2", FileType.FAM_ENTRY);
			p2pLayer.get(notificationPhase3.getCreditorFBMPE2(), "creditorFBMPE2", FileType.FBM_ENTRY);
						
			//Wait until the three files are successfully loaded
			loadAccountLedgerSemaphore.acquire();
			loadFAMSemaphore.acquire();
			loadFBMSemaphore.acquire();
			
			AccountLedgerEntry creditorLedgerEntryPE2 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase3.getCreditorLedgerPE2());
			FBMEntry creditorFBMEntryPE2 = (FBMEntry) loadedDHTEntries.get(notificationPhase3.getCreditorFBMPE2());
			FAMEntry debitorFACreditorPE2 = (FAMEntry) loadedDHTEntries.get(notificationPhase3.getDebitorFACreditorPE2());
			
			//Creditor validates these files. An error is returned if one or more files are not well formed
			entryValidator.validatePaymentPhase3();
			
			guiObserver.onPaymentPhase3ValidationSuccess(notificationRef);
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Once the three last partial entries have been validated the creditor has to continue with the last payment phase
	 * @param notificationRef
	 */
	public void paymentProtocolCreditorPhase4(String notificationRef){
		try {
			NotificationPaymentPhase3 notificationPhase3 = (NotificationPaymentPhase3) messenger.getNotification(notificationRef);
			
			String transRef = notificationPhase3.getTransactionReference();
			
			AccountLedgerEntry creditorLedgerEntryPE2 = (AccountLedgerEntry) loadedDHTEntries.get(notificationPhase3.getCreditorLedgerPE2());
			FBMEntry creditorFBMEntryPE2 = (FBMEntry) loadedDHTEntries.get(notificationPhase3.getCreditorFBMPE2());
			FAMEntry debitorFACreditorPE2 = (FAMEntry) loadedDHTEntries.get(notificationPhase3.getDebitorFACreditorPE2());
					
			//With the creditor corresponding three partial files the next final entries are created:
			AccountLedgerEntry creditorLedgerEntry = PastEntryFactory.createFinalAccountLedgerEntry(
					creditorLedgerEntryPE2, creditorFBMEntryPE2.getSelf_ledgerEntry_DHTHash(), "Creditor digital signature");
			
			Id debitorFACreditor_DHTHash = storedFAMEntryDHTHashes.get(transRef);
			FAMEntry debitorFACreditor = PastEntryFactory.createFinalFAMEntry(debitorFACreditorPE2, debitorFACreditor_DHTHash, "Creditor digital signature");
			storedFAMEntryDHTHashes.remove(transRef);
			
			FBMEntry creditorFBMEntry = PastEntryFactory.createFinalFBMEntry(creditorFBMEntryPE2, 
					creditorLedgerEntry.getSelf_FBMEntry_DHTHash(), "Creditor digital signature");
			
			messenger.deleteNotification(notificationRef);
			loadedDHTEntries.remove(notificationPhase3.getCreditorLedgerPE2());
			loadedDHTEntries.remove(notificationPhase3.getCreditorFBMPE2());
			loadedDHTEntries.remove(notificationPhase3.getDebitorFACreditorPE2());
			
			storeFilesCreditorPaymentProtocolPhase4(creditorLedgerEntry, creditorFBMEntry, debitorFACreditor);
			
			Notification notificationPhase4 = new NotificationPaymentPhase4(p2pLayer.getNode().getId(), transRef);
			sendNotification(notificationPhase4);
			
			guiObserver.onLogMessage("Payment Phase 4 finished.");
			guiObserver.onLogMessage("Payment finished.");
			
			guiObserver.onDeleteNotification(notificationRef);
		} catch (NonExistingNotificationException e) {
			guiObserver.onFailedNotificationLoad();
		} catch (NodeNotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void storeFilesCreditorPaymentProtocolPhase4(AccountLedgerEntry creditorLedger, FBMEntry creditorFBM, FAMEntry creditorFAM){
		p2pLayer.put(creditorLedger, "creditorLedgerEntry", FileType.ACCOUNT_LEDGER_ENTRY);
		p2pLayer.put(creditorFBM, "creditorFBMEntry", FileType.FBM_ENTRY);
		p2pLayer.put(creditorFAM, "debitorFADebitorEntry", FileType.FAM_ENTRY);
	}
	
	/*
	 * This private method sends a notification to a node
	 */
	private void sendNotification(Notification not) throws NodeNotInitializedException{
		LeafSet leafSet = p2pLayer.getNode().getLeafSet();
		
		for (int i = 0; i <= leafSet.ccwSize(); i++){
	      if (i != 0) {
	        NodeHandle nh = leafSet.get(i);
	        
	        messenger.sendNotification(nh, not);
	        
	        try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	      }
	    }
	}
	
	/*
	 * This private method start the search of the last AccountLedgerEntry of an user
	 * A boolean is set to true to indicate that the program has to keep looking for the next entry
	 */
	private void loadLastAccountLedgerEntry(){
		this.loadMoreLedger = true;
		
		p2pLayer.get(publicProfile.getSelf_first_LedgerEntryDHTHash(), "firstLedgerEntry", FileType.ACCOUNT_LEDGER_ENTRY);
		
		try {
			loadAccountLedgerSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This private method start the search of the last FAMEntry of an user
	 * A boolean is set to true to indicate that the program has to keep looking for the next entry
	 */
	private void loadLastFAMEntry(){
		this.loadMoreFAM = true;
		
		p2pLayer.get(publicProfile.getSelf_first_FAMEntryDHTHash(), "firstFAMEntry", FileType.FAM_ENTRY);
		
		try {
			loadFAMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This private method start the search of the last FBMEntry of an user
	 * A boolean is set to true to indicate that the program has to keep looking for the next entry
	 */
	private void loadLastFBMEntry(){
		loadMoreFBM = true;
		p2pLayer.get(publicProfile.getSelf_first_FBMEntryDHTHash(), "firstFBMEntry", FileType.FBM_ENTRY);
		
		try {
			loadFBMSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This method calls p2p layer to look up the user's public profile on the DHT
	 */
	private void loadPublicProfile(){
		p2pLayer.get(privateProfile.getSelf_publicProfile_DHTHash(), "UserPublicProfile", FileType.PUBLIC_PROFILE_ENTRY);
	}
	
	/*
	 * This method loads the transactions of the current user. It does not return any object because
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
	public void onReceiveNotification(Notification notification) {
		guiObserver.onReceiveNotification(notification.getNotificationReference());
	}

	/**
	 * Finished storage into the DHT. If an error has occurred, the boolean error variable is set to true
	 * @param msg
	 * @param error
	 */
	@Override
	public void onFinishedStorage(String msg, boolean error) {
		guiObserver.onLogMessage(msg);
		
		//TODO
		//tratar el error
		if (error){//there has been errors during the "insert" call into the DHT
			
		}
		else{//Successful insertion into the DHT
			
		}
	}

	/**
	 * Successfully Bill request. If an error has occurred, the boolean error variable is set to true
	 * @param bill
	 * @param error
	 * @param msg
	 */
	@Override
	public void onLookupBill(PastContent bill, boolean error, String msg) {
		if (error){
			//TODO
		}
		else{
			try {
				Bill billEntry = (Bill) bill;
				
				if (!loadedBills.containsKey(billEntry.getSelf_transRef()))
					loadedBills.put(billEntry.getSelf_transRef(), billEntry);
			}
			catch (ClassCastException e){
				//TODO
			}
		}
		
		loadBillTrials++;
		
		if (loadBillTrials == billsToload){
			loadTransactionsSemaphore.release();
		}
	}

	/**
	 * Successfully AccountLedgerRequest. If an error has occurred, the boolean error variable is set to true
	 * @param accountLedger
	 * @param error
	 * @param msg
	 */
	@Override
	public void onLookupAccountLedger(PastContent accountLedger, boolean error, String msg) {
		guiObserver.onLogMessage(msg);
		if (error || accountLedger == null){
			//If an error occurred then it is supposed that there is no next ledger to load
			if (loadMoreLedger)
				loadMoreLedger = false;
			loadAccountLedgerSemaphore.release();
		}
		else{
			try {
				lastLedger = (AccountLedgerEntry) accountLedger;

				loadedDHTEntries.put(lastLedger.getId(), lastLedger);
				
				if (loadMoreLedger){
					loadedDHTEntries.remove(lastLedger.getSelf_previous_ledgerEntry_DHTHash());
					
					int num = lastLedger.getLedgerEntryNum() + 1;
					p2pLayer.get(lastLedger.getSelf_next_ledgerEntry_DHTHash(), 
							"AccountLedgerEntry " + num, FileType.ACCOUNT_LEDGER_ENTRY);
				}
				else
					loadAccountLedgerSemaphore.release();
			}
			catch (ClassCastException e){
				//TODO
			}
		}
	}

	/**
	 * Successfully FAMEntry request. If an error has occurred, the boolean error variable is set to true
	 * @param famEntry
	 * @param error
	 * @param msg
	 */
	@Override
	public void onLookupFAMEntry(PastContent famEntry, boolean error, String msg) {
		guiObserver.onLogMessage(msg);
		if (error || famEntry == null){
			loadMoreFAM = false;
			loadFAMSemaphore.release();
		}
		else{
			try {
				lastFAM = (FAMEntry) famEntry;
				loadedDHTEntries.put(lastFAM.getId(), lastFAM);
				
				if (loadMoreFAM){
					loadedDHTEntries.remove(lastFAM.getSelf_previous_FAMEntry_DHTHash());
					
					int num = lastFAM.getFAMEntryNum() + 1;
					p2pLayer.get(lastFAM.getSelf_next_FAMEntry_DHTHash(), "FAMEntry " + num, FileType.FAM_ENTRY);
				}
				else
					loadFAMSemaphore.release();
			}
			catch (ClassCastException e){
				//TODO
			}
		}
	}

	/**
	 * Successfully FBMEntry request. If an error has occurred, the boolean error variable is set to true
	 * @param fbmEntry
	 * @param error
	 * @param msg
	 */
	@Override
	public void onLookupFBMEntry(PastContent fbmEntry, boolean error, String msg) {
		guiObserver.onLogMessage(msg);
		if (error || fbmEntry == null){
			loadMoreFBM = false;
			loadFBMSemaphore.release();
		}
		else{
			try {
				lastFBM = (FBMEntry) fbmEntry;
				
				loadedDHTEntries.put(lastFBM.getId(), lastFBM);
				
				if (loadMoreFBM){
					loadedDHTEntries.remove(lastFBM.getSelf_previous_FBMEntry_DHTHash());
					
					int num = lastFBM.getFBMEntryNum() + 1;
					p2pLayer.get(lastFBM.getSelf_next_FBMEntry_DHTHash(), "FBMEntry " + num, FileType.FBM_ENTRY);
				}
				else
					loadFBMSemaphore.release();
			}
			catch (ClassCastException e){
				//TODO
			}
		}
	}
	
	/**
	 * Successfully PublicProfile request. If an error has occurred, the boolean error variable is set to true
	 * @param publicProfile
	 * @param error
	 * @param msg
	 */
	@Override
	public void onLookupPublicProfile(PastContent publicProfile, boolean error, String msg) {
		guiObserver.onLogMessage(msg);
		if (error){
			guiObserver.onFailedPublicProfileLoad();
		}
		else{
			try{
				this.publicProfile = (PublicProfile) publicProfile;
				guiObserver.onViewPublicProfile(this.publicProfile.getSelf_firstName(), this.publicProfile.getSelf_surnames(),
						this.publicProfile.getSelf_telephone(), this.publicProfile.getSelf_email());
			}
			catch (ClassCastException e) {
				guiObserver.onFailedPublicProfileLoad();
			}
		}
		
		loadPublicProfileSemaphore.release();
	}
}