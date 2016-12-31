package paymentprotocol.observer;

public interface CoreObserver {
	public void onReceiveNotification();
	
	public void onFinishedStorage(String msg, boolean error);
}
