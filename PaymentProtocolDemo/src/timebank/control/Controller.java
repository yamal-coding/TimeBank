package timebank.control;


import timebank.model.Core;
import timebank.observer.GUIObserver;

/**
 * 
 * @author yamal
 *
 */
public class Controller {
	//The logic of the application
	private Core core;
	
	public Controller(Core core){
		this.core = core;
	}
	
	/**
	 * Method used to connect the node to the Pastry network
	 * @param bindport
	 * @param bootAddress
	 * @param bootport
	 */
	public void connect(int bindport, String bootAddress, int bootport){
		core.connect(bindport, bootAddress, bootport);
	}
	
	/**
	 * This method adds a GUIObserver to Core
	 * @param obs
	 */
	public void addObserver(GUIObserver obs){
		core.addObserver(obs);
	}
	
	/**
	 * An order to view the public profile
	 */
	public void viewPublicProfile(){
		core.viewPublicProfile();
	}
	
	/**
	 * An order to view a transaction referred by the "ref" param
	 * @param ref
	 */
	public void viewTransaction(String ref){
		core.viewTransaction(ref);
	}
	
	/**
	 * An order to view a notification referred by the "ref" param
	 * @param ref
	 */
	public void handleNotification(String ref){
		core.handleNotification(ref);
	}
	
	/**
	 * Generation of the Debtor three first partial entries
	 * @param ref
	 * @param comment
	 * @param degreeOfStisfaction
	 */
	public void debitorPaymentPhase1(String ref, String comment, int degreeOfStisfaction){
		core.paymentProtocolDebtorPhase1(ref, comment, degreeOfStisfaction);
	}
	
	/**
	 * Load and validation of the Debtor three first partial entries
	 * @param notificationRef
	 */
	public void creditorPaymentPhase1(String notificationRef){
		core.paymentProtocolCreditorPhase1(notificationRef);
	}
	
	/**
	 * Generation of the Creditor three first partial entries and Debtor three second partial entries
	 * @param notificationRef
	 * @param comment
	 * @param degreeOfSatisfaction
	 */
	public void creditorPaymentPhase2(String notificationRef, String comment, int degreeOfSatisfaction){
		core.paymentProtocolCreditorPhase2(notificationRef, comment, degreeOfSatisfaction);
	}
	
	/**
	 * Load and validation of the Creditor three first partial entries and Debtor three second partial entries
	 * @param notificationRef
	 */
	public void debitorPaymentPhase2(String notificationRef){
		core.paymentProtocolDebitorPhase2(notificationRef);
	}
	
	/**
	 * Generation of the Debtor three last final entries and Creditor three second partial entries 
	 * @param notificationRef
	 */
	public void debitorPaymentPhase3(String notificationRef){
		core.paymentProtocolDebitorPhase3(notificationRef);
	}
	
	/**
	 * Load and validation of Creditor three second partial entries
	 * @param notificationRef
	 */
	public void creditorPaymentPhase3(String notificationRef){
		core.paymentProtocolCreditorPhase3(notificationRef);
	}
	
	/**
	 * Generation of Creditor three last partial entries
	 * @param notificationRef
	 */
	public void creditorPaymentPhase4(String notificationRef){
		core.paymentProtocolCreditorPhase4(notificationRef);
	}
}
