package timebank.observer;

import rice.p2p.past.PastContent;
import timebank.model.messaging.NotificationPair;

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
	
	public void onReceivedLastAccountLedgerEntry(PastContent ledger, boolean error);
	
	public void onReceivedLastFAMEntry(PastContent fam, boolean error);
	
	public void onReceivedLastFBMEntry(PastContent fbm, boolean error);
}
