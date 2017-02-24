package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.observer.GUIObserver;

/**
 * A first payment phase notification
 * @author yamal
 *
 */
public class NotificationPaymentPhase1 extends NotificationPayment {
	
	private Id debitorLedgerPE1;
	private Id creditorFADebitorPE1;
	private Id debitorFBMPE1;
	
	public NotificationPaymentPhase1(Id from, String transRef, Id debitorLedgerPE1,
			Id creditorFADebitorPE1, Id debitorFBMPE1) {
		super(from, transRef);
		this.debitorLedgerPE1 = debitorLedgerPE1;
		this.creditorFADebitorPE1 = creditorFADebitorPE1;
		this.debitorFBMPE1 = debitorFBMPE1;
	}
	
	@Override
	public NotificationType getNotificationType(){
		return NotificationType.DEBITOR_PAYMENT_PHASE1;
	}
	
	public void handleNotification(GUIObserver observer){
		//This notification is shown to the Creditor of a payment process,
		//and it has to indicate to the user that the payment has been started by the debtor
		//and then ask him if he wants to proceed with the next payment phase (second phase)
		observer.onPaymentPhase1Started(notificationRef, transRef);
	}

	/**
	 * 
	 * @return debitorLedgerPE1Hash
	 */
	public Id getDebitorLedgerPE1Hash() {
		return debitorLedgerPE1;
	}
	
	/**
	 * 
	 * @return creditorFADebitorPE1Hash
	 */
	public Id getCreditorFADebitorPE1Hash() {
		return creditorFADebitorPE1;
	}

	/**
	 * 
	 * @return debitorFBMPE1Hash
	 */
	public Id getDebitorFBMPE1Hash() {
		return debitorFBMPE1;
	}
}
