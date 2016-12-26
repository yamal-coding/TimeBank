package paymentprotocol.model.messaging;

import paymentprotocol.observer.Observer;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class NotificationHandler implements Application {
	private Endpoint endpoint;
	
	private Observer observer;
	
	public NotificationHandler(Node node) {
		this.endpoint = node.buildEndpoint(this, "myinstance");
		this.endpoint.register();
	}
	
	@Override
	public void deliver(Id arg0, Message arg1) {
		// TODO Auto-generated method stub

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
