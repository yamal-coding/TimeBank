package paymentprotocol.observer;

public interface GUIObserver {
	public void failedConnection();

	public void onReceiveNotification();
}
