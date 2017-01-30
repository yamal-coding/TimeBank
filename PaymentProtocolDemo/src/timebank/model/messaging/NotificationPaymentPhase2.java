package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.model.files.network.persistent.AccountLedgerEntry;
import timebank.model.files.network.persistent.FAMEntry;
import timebank.model.files.network.persistent.FBMEntry;

public class NotificationPaymentPhase2 extends Notification {

	private AccountLedgerEntry creditorLedgerPE1;
	private FAMEntry debitorFACreditorPE1;
	private FBMEntry creditorFBMPE1;	

	private AccountLedgerEntry debitorLedgerPE2;
	private FAMEntry creditorFADebitorPE2;
	private FBMEntry debitorFBMPE2;
	
	public NotificationPaymentPhase2(Id from, AccountLedgerEntry creditorLedgerPE1,
			FAMEntry debitorFACreditorPE1, FBMEntry creditorFBMPE1, AccountLedgerEntry debitorLedgerPE2, 
			FAMEntry creditorFADebitorPE2, FBMEntry debitorFBMPE2) {
		super(from);
		
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
	
	public AccountLedgerEntry getCreditorLedgerPE1() {
		return creditorLedgerPE1;
	}

	public FAMEntry getDebitorFACreditorPE1() {
		return debitorFACreditorPE1;
	}

	public FBMEntry getCreditorFBMPE1() {
		return creditorFBMPE1;
	}

	public AccountLedgerEntry getDebitorLedgerPE2() {
		return debitorLedgerPE2;
	}

	public FAMEntry getCreditorFADebitorPE2() {
		return creditorFADebitorPE2;
	}

	public FBMEntry getDebitorFBMPE2() {
		return debitorFBMPE2;
	}

}
