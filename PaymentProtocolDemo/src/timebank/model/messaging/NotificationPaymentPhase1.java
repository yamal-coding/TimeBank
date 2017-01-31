package timebank.model.messaging;

import rice.p2p.commonapi.Id;

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

	public Id getDebitorLedgerPE1Hash() {
		return debitorLedgerPE1;
	}

	public Id getCreditorFADebitorPE1Hash() {
		return creditorFADebitorPE1;
	}

	public Id getDebitorFBMPE1Hash() {
		return debitorFBMPE1;
	}
}
