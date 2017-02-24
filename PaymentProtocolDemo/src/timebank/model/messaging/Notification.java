package timebank.model.messaging;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import timebank.observer.GUIObserver;

/**
 * Class that implements Message Interface. It represents a message between two FreePastry nodes
 * and it contains a hash and a id of a content previously stored into the DHT
 * @author yamal
 *
 */
public abstract class Notification implements Message {
	protected String notificationRef; //reference to this notification
	protected Id from;
	
	/**
	 * Constructor
	 * @param id
	 * @param hash
	 */
	public Notification(Id from) {
		super();
		this.from = from;
		//The reference of this notification has to be unique so it is formed by a concatenation of the id
		//of sender and a timestamp
		this.notificationRef = (new Timestamp(System.currentTimeMillis())).toString() + from.toString();
	}
	
	/**
	 * Returns the reference to this pair
	 * @return
	 */
	public String getNotificationReference(){
		return notificationRef;
	}
	
	/**
	 * Returns the Id of the message originator
	 * @return
	 */
	public Id getFrom(){
		return from;
	}
	
	/**
	 * Each notification has an own way of being handled. This method is implemented by each notification type
	 * @param observer
	 */
	public abstract void handleNotification(GUIObserver observer);
	
	/**
	 * THis method returns the type of the notification. This method is implemented by each notification type
	 * @return
	 */
	public abstract NotificationType getNotificationType();
	
	/**
	 * Returns the priority of the FreePastry Message. It has to be low because of
	 * the inner maintenance traffic of FreePastry's network
	 */
	@Override
	public int getPriority() {
		return LOW_PRIORITY;
	}
}
