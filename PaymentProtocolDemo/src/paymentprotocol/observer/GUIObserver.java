package paymentprotocol.observer;

public interface GUIObserver {
	public void failedConnection();
	
	public void onNoPendingTransactions();

	public void onReceiveNotification();
	
	public void onNodeAlreadyConnected();
	
	public void onPublicProfileLoaded(String name, String surname);
	
	public void onFailedPublicProfileFailLoad();
}
