package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.observer.GUIObserver;

/**
 * A second payment phase notification
 * @author yamal
 *
 */
public class NotificationPaymentPhase2 extends NotificationPayment {

	private Id creditorLedgerPE1;
	private Id debitorFACreditorPE1;
	private Id creditorFBMPE1;

	private Id debitorLedgerPE2;
	private Id creditorFADebitorPE2;
	private Id debitorFBMPE2;
	
	public NotificationPaymentPhase2(Id from, String transRef, Id creditorLedgerPE1,
			Id debitorFACreditorPE1, Id creditorFBMPE1, Id debitorLedgerPE2, 
			Id creditorFADebitorPE2, Id debitorFBMPE2) {
		super(from, transRef);
		
		this.creditorLedgerPE1 = creditorLedgerPE1;
		this.debitorFACreditorPE1 = debitorFACreditorPE1;
		this.creditorFBMPE1 = creditorFBMPE1;
		
		this.debitorLedgerPE2 = debitorLedgerPE2;
		this.creditorFADebitorPE2 = creditorFADebitorPE2;
		this.debitorFBMPE2 = debitorFBMPE2;
	}

	@Override
	public NotificationType getNotificationType() {
		return NotificationType.CREDITOR_PAYMENT_PHASE2;
	}
	
	public void handleNotification(GUIObserver observer){
		//This notification is shown to the Debtor of a payment process,
		//and it has to ask him if he wants to proceed with the next payment phase	(third phase)			
		observer.onPaymentPhase2Started(notificationRef, transRef);
	}
	
	/**
	 * 
	 * @return creditorLedgerPE1Hash
	 */
	public Id getCreditorLedgerPE1Hash() {
		return creditorLedgerPE1;
	}

	/**
	 * 
	 * @return debitorFACreditorPE1Hash
	 */
	public Id getDebitorFACreditorPE1Hash() {
		return debitorFACreditorPE1;
	}

	/**
	 * creditorFBMPE1Hash
	 * @return
	 */
	public Id getCreditorFBMPE1Hash() {
		return creditorFBMPE1;
	}

	/**
	 * debitorLedgerPE2Hash
	 * @return
	 */
	public Id getDebitorLedgerPE2Hash() {
		return debitorLedgerPE2;
	}

	/**
	 * 
	 * @return creditorFADebitorPE2Hash
	 */
	public Id getCreditorFADebitorPE2Hash() {
		return creditorFADebitorPE2;
	}
	
	/**
	 * 
	 * @return debitorFBMPE2Hash
	 */
	public Id getDebitorFBMPE2Hash() {
		return debitorFBMPE2;
	}
}
