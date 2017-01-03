package paymentprotocol.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import paymentprotocol.model.files.local.PrivateProfile;
import paymentprotocol.model.files.network.persistent.AccountLedgerEntry;
import paymentprotocol.model.files.network.persistent.Bill;
import paymentprotocol.model.files.network.persistent.FAMEntry;
import paymentprotocol.model.files.network.persistent.FBMEntry;
import paymentprotocol.model.files.network.persistent.FileType;
import paymentprotocol.model.files.network.persistent.PublicProfile;
import paymentprotocol.model.messaging.NotificationPair;
import paymentprotocol.model.p2p.P2PLayer;
import paymentprotocol.observer.CoreObserver;
import paymentprotocol.observer.GUIObserver;
import rice.p2p.commonapi.Id;
import rice.p2p.past.PastContent;

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
	
	
	/**
	 * Core constructor
	 * @param p2pLayer
	 */
	public Core(P2PLayer p2pLayer, PrivateProfile privateProfile){
		this.loadedBills = new HashMap<String, Bill>();
		this.billsToload = privateProfile.getTransactionsDHTHashes().size();
		this.loadBillTrials = 0;
		this.p2pLayer = p2pLayer;
		this.privateProfile = privateProfile;
		this.p2pLayer.addObserver((this));
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
						
						/*Iterator it = loadedBills.keySet().iterator();
						while(it.hasNext()){
							Map.Entry pair = (Map.Entry) it.next();
							guiObserver.onTransactionLoaded(((Bill) pair.getValue()).getSelf_transRef());
						}*/
						
						for (Map.Entry<String, Bill> entry : loadedBills.entrySet())
							guiObserver.onTransactionLoaded(entry.getValue().getSelf_transRef());
						
					}
				} catch (InterruptedException e) {
					//Notify error to the GUI
				}
		}
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
	
	private AccountLedgerEntry createLedgerEntryPE1(){
		return null;
	}
	
	private FBMEntry createFBMEntryPE1(){
		return null;
	}
	
	private FAMEntry createFAMEntryPE1(){
		return null;
	}
	
	private AccountLedgerEntry createLedgerEntryPE2(){
		return null;
	}
	
	private FBMEntry createFBMEntryPE2(){
		return null;
	}
	
	private FAMEntry createFAMEntryPE2(){
		return null;
	}
	
	private AccountLedgerEntry createLedgerEntryFinal(){
		return null;
	}
	
	private FBMEntry createFBMEntryFinal(){
		return null;
	}
	
	private FAMEntry createFAMEntryFinal(){
		return null;
	}
		 
	
	/**
	 * Method called when a notification is received from another node
	 */
	@Override
	public synchronized void onReceiveNotification(NotificationPair notificationPair) {
		guiObserver.onReceiveNotification();
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
			
		}
		else{
			try{
				this.publicProfile = (PublicProfile) publicProfile;
				guiObserver.onPublicProfileLoaded(this.publicProfile.getSelf_firstName(), this.publicProfile.getSelf_surnames());
			}
			catch (ClassCastException e) {
				//Handle error
			}
		}
		notify();
	}
}
