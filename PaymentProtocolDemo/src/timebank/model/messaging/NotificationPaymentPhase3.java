package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.model.files.network.persistent.AccountLedgerEntry;
import timebank.model.files.network.persistent.FAMEntry;
import timebank.model.files.network.persistent.FBMEntry;

public class NotificationPaymentPhase3 extends Notification {

	private AccountLedgerEntry debitorLedgerPE1;
	private FAMEntry creditorFADebitorPE1;
	private FBMEntry debitorFBMPE1;

	public NotificationPaymentPhase3(Id from, AccountLedgerEntry debitorLedgerPE1,
			FAMEntry creditorFADebitorPE1, FBMEntry debitorFBMPE1) {
		super(from);
		this.debitorLedgerPE1 = debitorLedgerPE1;
		this.creditorFADebitorPE1 = creditorFADebitorPE1;
		this.debitorFBMPE1 = debitorFBMPE1;
	}

	@Override
	public NotificationType getNotificationType() {
		return NotificationType.DEBITOR_PAYMENT_PHASE3;
	}
	
	public AccountLedgerEntry getDebitorLedgerPE1() {
		return debitorLedgerPE1;
	}

	public FAMEntry getCreditorFADebitorPE1() {
		return creditorFADebitorPE1;
	}

	public FBMEntry getDebitorFBMPE1() {
		return debitorFBMPE1;
	}
}
