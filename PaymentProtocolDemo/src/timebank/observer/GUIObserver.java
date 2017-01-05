package timebank.observer;

public interface GUIObserver {
	public void failedConnection();
	
	public void onNoPendingTransactions();

	public void onReceiveNotification(String notification);
	
	public void onNodeAlreadyConnected();
	
	public void onPublicProfileLoaded(String name, String surname);
	
	public void onTransactionLoaded(String transref);
	
	public void onFailedPublicProfileLoad();
}
