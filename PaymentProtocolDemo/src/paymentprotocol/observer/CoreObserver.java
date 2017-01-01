package paymentprotocol.observer;

import paymentprotocol.model.messaging.NotificationPair;
import rice.p2p.past.PastContent;

/**
 * Interface used by P2PLayer to communicate with Core
 * @author yamal
 *
 */
public interface CoreObserver {
	public void onReceiveNotification(NotificationPair notificationPair);
	
	public void onFinishedStorage(String msg, boolean error);
	
	public void onLookupBill(PastContent bill, boolean error, String msg);
	
	public void onLookupAccountLedger(PastContent accountLedger, boolean error, String msg);
	
	public void onLookupFAMEntry(PastContent famEntry, boolean error, String msg);
	
	public void onLookupFBMEntry(PastContent fbmEntry, boolean error, String msg);
	
	public void onLookupPublicProfile(PastContent publicProfile, boolean error, String msg);
}
