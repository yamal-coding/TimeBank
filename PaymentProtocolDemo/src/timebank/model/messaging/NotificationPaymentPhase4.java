package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.observer.GUIObserver;

/**
 * The last notification sent in a payment process. From creditor to debtor.
 * @author yamal
 *
 */
public class NotificationPaymentPhase4 extends NotificationPayment {
	
	public NotificationPaymentPhase4(Id from, String transRef) {
		super(from, transRef);
	}

	@Override
	public void handleNotification(GUIObserver observer) {
		observer.onPaymentFinished(notificationRef, transRef);
	}

	@Override
	public NotificationType getNotificationType() {
		return NotificationType.CREDITOR_PAYMENT_PHASE4;
	}

}
