package paymentprotocol.model;

import paymentprotocol.model.files.local.PrivateProfile;
import paymentprotocol.model.files.network.persistent.AccountLedgerEntry;
import paymentprotocol.model.files.network.persistent.FAMEntry;
import paymentprotocol.model.files.network.persistent.FBMEntry;
import paymentprotocol.model.files.network.persistent.FileType;
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
	
	/**
	 * Core constructor
	 * @param p2pLayer
	 */
	public Core(P2PLayer p2pLayer, PrivateProfile privateProfile){
		//hay que hacer que el id de cada nodo se genere con un hash de un UUUID
		//para que no se repitan
		this.p2pLayer = p2pLayer;
		this.privateProfile = privateProfile;
		this.p2pLayer.addObserver((this));
	}
	
	/**
	 * Method to add the GUIObserver to this class
	 * @param obs
	 */
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
		switch(p2pLayer.join(bindport, bootAddress, bootport, this)){
		case NODE_ALREADY_CONNECTED:{
			//notify to the GUI
		} break;
		case FAILED_CONNECTION: {
			//notify to the GUI
		} break;
		default: //CONNECTION_SUCCESSFUL
			//User public profile is loaded from DHT
			loadPublicProfile();
			
			//establecer un punto de espera hasta que se haya cargado el perfil
			//y despues cargar las facturas
			
			//User transactions are loaded from DHT
			if (privateProfile.getTransactionsDHTHashes().isEmpty())
				//notify to the GUI that there are  not any pending transaction
				guiObserver.onNoPendingTransactions();
			else
				loadTransactions();
		}
	}
	
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
	public void onReceiveNotification(NotificationPair notificationPair) {
		guiObserver.onReceiveNotification();
	}

	/**
	 * Finished storage into the DHT.
	 * If an error has occurred, the boolean error variable is set to true
	 * @param msg
	 * @param error
	 */
	@Override
	public void onFinishedStorage(String msg, boolean error) {
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
	public void onLookupBill(PastContent bill, boolean error, String msg) {
		if (error){
			
		}
		else{
			
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
	public void onLookupAccountLedger(PastContent accountLedger, boolean error, String msg) {
		if (error){
			
		}
		else{
			
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
	public void onLookupFAMEntry(PastContent famEntry, boolean error, String msg) {
		if (error){
			
		}
		else{
			
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
	public void onLookupFBMEntry(PastContent fbmEntry, boolean error, String msg) {
		if (error){
			
		}
		else{
			
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
	public void onLookupPublicProfile(PastContent publicProfile, boolean error, String msg) {
		if (error){
			
		}
		else{
			
		}
	}
}
