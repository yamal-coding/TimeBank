package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import timebank.observer.GUIObserver;

public abstract class NotificationPayment extends Notification {
	//Reference of the transaction associated to the current payment
	protected String transRef;
	
	public NotificationPayment(Id from, String transRef) {
		super(from);
		this.transRef = transRef;
	}

	/**
	 * This method returns the reference of the transaction associated to the current payment
	 * @return
	 */
	public String getTransactionReference(){
		return transRef;
	}

	public abstract void handleNotification(GUIObserver observer);
	
	public abstract NotificationType getNotificationType();
}
