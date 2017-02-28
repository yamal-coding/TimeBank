package timebank.observer;

/**
 * Interface used by the kernel application to communicate with the GUI
 * @author yamal
 *
 */
public interface GUIObserver {
	/**
	 * Method called when the connection on the FreePastry network has been established correctly
	 */
	public void onSuccesfulConnection();
	
	/**
	 * Method called when there has been an error during the connection process
	 */
	public void failedConnection();
	
	/**
	 * Method called to indicate that there isn't any pending transaction
	 */
	public void onNoPendingTransactions();

	/**
	 * A new notification has arrived
	 * @param notification
	 */
	public void onReceiveNotification(String notification);
	
	/**
	 * Method called in case the Core method "connect" has been called for the second time 
	 */
	public void onNodeAlreadyConnected();
	
	/**
	 * Method called to pass some public information of the current user to the GUI
	 * @param name
	 * @param surname
	 * @param phone
	 * @param email
	 */
	public void onViewPublicProfile(String name, String surname, int phone, String email);
	
	/**
	 * This method returns information about a transaction previously selected on the GUI to be shown
	 * @param ref
	 * @param hours
	 * @param isCreditor
	 */
	public void onViewTransaction(String ref, double hours, boolean isCreditor);
	
	/**
	 * Method that returns a concrete transaction reference to the GUI
	 * @param transref
	 */
	public void onTransactionLoaded(String transref);
	
	/**
	 * In case the public profile could not be loaded, this method is called
	 */
	public void onFailedPublicProfileLoad();
	
	/**
	 * When the user wants to see a notification from the GUI, this method is called when there is an error
	 * during the search of this notification
	 */
	public void onFailedNotificationLoad();
	
	/**
	 * This method returns a concrete message with some information to the GUI
	 * @param msg
	 */
	public void onLogMessage(String msg);
	
	/**
	 * Method called when the payment phase 1 has started
	 * @param notRef
	 * @param transRef
	 */
	public void onPaymentPhase1Started(String notRef, String transRef);
	
	/**
	 * Method called when the first payment phase files have been correctly validated
	 * @param notificationRef
	 */
	public void onPaymentPhase1ValidationSuccess(String notificationRef);
	
	/**
	 * Method called when the payment phase 2 has started
	 * @param notRef
	 * @param transRef
	 */
	public void onPaymentPhase2Started(String notRef, String transRef);
	
	/**
	 *  Method called when the second payment phase files have been correctly validated
	 * @param notificationRef
	 */
	public void onPaymentPhase2ValidationSuccess(String notificationRef);
	
	/**
	 * Method called when the payment phase 3 has started
	 * @param notRef
	 * @param transRef
	 */
	public void onPaymentPhase3Started(String notRef, String transRef);
	
	/**
	 *  Method called when the last payment phase files have been correctly validated
	 * @param notificationRef
	 */
	public void onPaymentPhase3ValidationSuccess(String notificationRef);
}
