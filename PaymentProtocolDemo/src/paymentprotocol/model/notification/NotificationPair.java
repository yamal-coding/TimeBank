package paymentprotocol.model.notification;

import rice.p2p.commonapi.Message;

public class NotificationPair implements Message {
	private String id; //cambiar tipo
	private String hash; //cambiar tipo
	
	public NotificationPair(String id, String hash) {
		super();
		this.id = id;
		this.hash = hash;
	}

	public String getId() {
		return id;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String toString(){
		return "NotificationPair: id = " + id + " hash = " + hash;
	}

	@Override
	public int getPriority() {
		return LOW_PRIORITY;
	}
}
