package timebank.model.messaging;

import rice.p2p.commonapi.Id;

public class NotificationPaymentPhase3 extends NotificationPayment {

	private Id creditorLedgerPE2;
	private Id debitorFACreditorPE2;
	private Id creditorFBMPE2;

	public NotificationPaymentPhase3(Id from, String transRef, Id creditorLedgerPE2,
			Id debitorFACreditorPE2, Id creditorFBMPE2) {
		super(from, transRef);
		this.creditorLedgerPE2 = creditorLedgerPE2;
		this.debitorFACreditorPE2 = debitorFACreditorPE2;
		this.creditorFBMPE2 = creditorFBMPE2;
	}

	@Override
	public NotificationType getNotificationType() {
		return NotificationType.DEBITOR_PAYMENT_PHASE3;
	}

	public Id getCreditorLedgerPE2() {
		return creditorLedgerPE2;
	}

	public Id getDebitorFACreditorPE2() {
		return debitorFACreditorPE2;
	}

	public Id getCreditorFBMPE2() {
		return creditorFBMPE2;
	}
	
	
}
