package paymentprotocol.model.messaging;

import paymentprotocol.observer.CoreObserver;
import paymentprotocol.observer.GUIObserver;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.MessageReceipt;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class NotificationHandler implements Application {
	private Endpoint endpoint;
	
	private CoreObserver coreObserver;
	
	public NotificationHandler(Node node, CoreObserver coreObserver) {
		this.endpoint = node.buildEndpoint(this, "myinstance");
		this.endpoint.register();
		this.coreObserver = coreObserver;
	}
	
	/**
	 * Method used to send a message to a concrete node trough his NodeHandle
	 * @param nh
	 * @param msg
	 */
	public void sendNotification(NodeHandle nh, Message msg){
		endpoint.route(null, msg, nh);
	}
	
	@Override
	public void deliver(Id arg0, Message arg1) {
		//filtrar el mensaje que se recibe
		coreObserver.onReceiveNotification();
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
