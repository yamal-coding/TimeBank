package paymentprotocol.model.messaging;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

/**
 * Class that implements Message Interface. It represents a message between two FreePastry nodes
 * and it cointains a hash and a id of a content previously stored into the DHT
 * @author yamal
 *
 */
public class NotificationPair implements Message {
	private String id; //cambiar tipo
	private Id hash;
	
	/**
	 * Constructor
	 * @param id
	 * @param hash
	 */
	public NotificationPair(String id, Id hash) {
		super();
		this.id = id;
		this.hash = hash;
	}

	/**
	 * Returns the id stored in this notification pair
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the hash stored in this notification pair
	 * @return hash
	 */
	public Id getHash() {
		return hash;
	}
	
	public String toString(){
		return "NotificationPair: id = " + id + " hash = " + hash;
	}

	/**
	 * Returns the priority of the FreePastry Message. It has to be low because of
	 * the inner maintenance traffic of FreePastry's network
	 */
	@Override
	public int getPriority() {
		return LOW_PRIORITY;
	}
}
