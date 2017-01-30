package timebank.model.messaging;

import java.util.HashMap;
import java.util.Map;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import timebank.model.exception.NonExistingNotificationException;
import timebank.observer.CoreObserver;

public class Messenger implements Application {
	private Endpoint endpoint;
	
	//Observer to tell Core the deliver of new notifications
	private CoreObserver coreObserver;
	
	//Set of the current notifications received and not attended
	private Map<String, Notification> notifications;
	
	public Messenger(Node node, CoreObserver coreObserver){
		this.endpoint = node.buildEndpoint(this , "myinstance");
		this.endpoint.register();
		
		notifications = new HashMap<String, Notification>();
		
		this.coreObserver = coreObserver;
	}
	
	/**
	 * This method returns a notification associated to the reference given
	 * @param ref
	 * @return Notification
	 */
	public Notification getNotification(String ref) throws NonExistingNotificationException {
		if (!notifications.containsKey(ref))
			throw new NonExistingNotificationException();
		
		return notifications.get(ref);
	}
	
	/**
	 * This methods remove from the map of notifications the notification associated to the reference given
	 * @param ref
	 * @return False if there is not any notification with this reference, True if the erased could be done 
	 */
	public boolean deleteNotification(String ref){
		if (!notifications.containsKey(ref))
			return false;
		
		notifications.remove(ref);
		
		return true;
	}
	
	/**
	 * Method used to send a message to a concrete node through his NodeHandle
	 * @param nh
	 * @param msg
	 */
	public void sendNotification(NodeHandle nh, Message msg){
		endpoint.route(null, msg, nh);
	}
	
	/**
	 * This method is called when a Message object is received from other PastryNode
	 */
	@Override
	public void deliver(Id arg0, Message arg1) {
		try {
			Notification notif = (Notification) arg1;
			
			notifications.put(notif.getRef(), notif);
			
			coreObserver.onReceiveNotification(notif);
		}
		catch (ClassCastException e){
			
		}
	}

	@Override
	public boolean forward(RouteMessage arg0) {
		
		return true;
	}

	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

}
