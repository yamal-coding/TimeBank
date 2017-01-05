package timebank.model.messaging;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

/**
 * Class that implements Message Interface. It represents a message between two FreePastry nodes
 * and it cointains a hash and a id of a content previously stored into the DHT
 * @author yamal
 *
 */
public class NotificationPair implements Message {
	private Id from; //originator
	private Id to; //destination
	private String id; //cambiar tipo?
	private Id hash;
	private String ref; //reference to this pair
	
	/**
	 * Constructor
	 * @param id
	 * @param hash
	 */
	public NotificationPair(Id from, Id to, String id, Id hash, String ref) {
		super();
		this.from = from;
		this.to= to;
		this.id = id;
		this.hash = hash;
		this.ref = ref;
	}

	/**
	 * Returns the Id of the originator of the message
	 * @return from
	 */
	public Id getIdFrom(){
		return from;
	}
	
	/**
	 * Returns the Id of the destination of the message
	 * @return from
	 */
	public Id getIdTo(){
		return to;
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
	
	/**
	 * Returns the reference to this pair
	 * @return
	 */
	public String getRef(){
		return ref;
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
