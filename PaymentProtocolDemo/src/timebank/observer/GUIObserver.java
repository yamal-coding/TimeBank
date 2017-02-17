package timebank.observer;

public interface GUIObserver {
	public void onSuccesfulConnection();
	
	public void failedConnection();
	
	public void onNoPendingTransactions();

	public void onReceiveNotification(String notification);
	
	public void onNodeAlreadyConnected();
	
	public void onViewPublicProfile(String name, String surname);
	
	public void onViewTransaction(String ref, double hours);
	
	public void onTransactionLoaded(String transref);
	
	public void onFailedPublicProfileLoad();
	
	public void onFailedNotificationLoad();
}
