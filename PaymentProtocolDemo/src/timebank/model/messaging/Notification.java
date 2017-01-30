package timebank.model.messaging;

import java.sql.Timestamp;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

/**
 * Class that implements Message Interface. It represents a message between two FreePastry nodes
 * and it contains a hash and a id of a content previously stored into the DHT
 * @author yamal
 *
 */
public abstract class Notification implements Message {
	private String ref; //reference to this pair
	
	/**
	 * Constructor
	 * @param id
	 * @param hash
	 */
	public Notification(Id from) {
		super();
		//The reference of this notification has to be unique so it is formed by a concatenation of the id
		//of sender and a timestamp
		this.ref = (new Timestamp(System.currentTimeMillis())).toString() + from.toString();
	}
	
	/**
	 * Returns the reference to this pair
	 * @return
	 */
	public String getRef(){
		return ref;
	}
	
	/**
	 * Returns the type of the notification. This method is implemented by each notification type
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
