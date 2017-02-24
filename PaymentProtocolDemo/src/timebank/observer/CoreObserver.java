package timebank.observer;

import rice.p2p.past.PastContent;
import timebank.model.messaging.Notification;

/**
 * Interface used by P2PLayer to communicate with Core
 * @author yamal
 *
 */
public interface CoreObserver {
	/**
	 * When a new notification has arrived, this method is called
	 * @param notificationPair
	 */
	public void onReceiveNotification(Notification notificationPair);
	
	/**
	 * WHen the DHT storage of an entry has finished, this method is called
	 * @param msg
	 * @param error
	 */
	public void onFinishedStorage(String msg, boolean error);
	
	/**
	 * Successfully Bill request. If an error has occurred, the boolean error variable is set to true
	 * @param bill
	 * @param error
	 * @param msg
	 */
	public void onLookupBill(PastContent bill, boolean error, String msg);
	
	/**
	 * Successfully AccountLedgerRequest. If an error has occurred, the boolean error variable is set to true
	 * @param accountLedger
	 * @param error
	 * @param msg
	 */
	public void onLookupAccountLedger(PastContent accountLedger, boolean error, String msg);
	
	/**
	 * Successfully FAMEntry request. If an error has occurred, the boolean error variable is set to true
	 * @param famEntry
	 * @param error
	 * @param msg
	 */
	public void onLookupFAMEntry(PastContent famEntry, boolean error, String msg);
	
	/**
	 * Successfully FBMEntry request. If an error has occurred, the boolean error variable is set to true
	 * @param fbmEntry
	 * @param error
	 * @param msg
	 */
	public void onLookupFBMEntry(PastContent fbmEntry, boolean error, String msg);
	
	/**
	 * Successfully PublicProfile request. If an error has occurred, the boolean error variable is set to true
	 * @param publicProfile
	 * @param error
	 * @param msg
	 */
	public void onLookupPublicProfile(PastContent publicProfile, boolean error, String msg);
}
