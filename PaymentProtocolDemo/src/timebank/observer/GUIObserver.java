package timebank.observer;

public interface GUIObserver {
	public void onSuccesfulConnection();
	
	public void failedConnection();
	
	public void onNoPendingTransactions();

	public void onReceiveNotification(String notification);
	
	public void onNodeAlreadyConnected();
	
	public void onViewPublicProfile(String name, String surname);
	
	public void onViewTransaction(String ref, double hours, boolean isCreditor);
	
	public void onTransactionLoaded(String transref);
	
	public void onFailedPublicProfileLoad();
	
	public void onFailedNotificationLoad();
	
	public void onLogMessage(String msg);
	
	//Este metodo se llama cuando se abre una notificacion de tipo NotificationPaymentPhase1
	public void onNewlyStartedPayment(String notRef, String transRef);
	
	public void onPhase1ValidationSuccess(String notificationRef);
	
}
