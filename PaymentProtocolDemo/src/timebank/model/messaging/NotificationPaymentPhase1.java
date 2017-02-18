package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.observer.GUIObserver;

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
		//Esta notificacion se recibe en la primera fase del pago siendo un Creditor,
		//por lo que hay que decir en la vista que el debitor X ha iniciado el pago y preguntar
		//si el Creditor desea proceder con la siguiente fase del pago
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
