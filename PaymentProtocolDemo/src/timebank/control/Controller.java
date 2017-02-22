package timebank.control;


import timebank.model.Core;
import timebank.observer.GUIObserver;

public class Controller {
	private Core core;
	
	public Controller(Core core){
		this.core = core;
	}
	
	public void connect(int bindport, String bootAddress, int bootport){
		core.connect(bindport, bootAddress, bootport);
	}
	
	public void addObserver(GUIObserver obs){
		core.addObserver(obs);
	}
	
	public void viewPublicProfile(){
		core.viewPublicProfile();
	}
	
	public void viewTransaction(String ref){
		core.viewTransaction(ref);
	}
	
	public void handleNotification(String ref){
		core.handleNotification(ref);
	}
	
	//generacion de los tres primeros ficheros parciales
	public void debitorPaymentPhase1(String ref, String comment, int degreeOfStisfaction){
		core.paymentProtocolDebtorPhase1(ref, comment, degreeOfStisfaction);
	}
	//carga y validacion de los tres primeros ficheros parciales
	public void creditorPaymentPhase1(String notificationRef){
		core.paymentProtocolCreditorPhase1(notificationRef);
	}
	//generacion de los 6 segundos ficheros parciales
	public void creditorPaymentPhase2(String notificationRef, String comment, int degreeOfSatisfaction){
		core.paymentProtocolCreditorPhase2(notificationRef, comment, degreeOfSatisfaction);
	}
	//carga y validacion de los 6 segundos ficheros parciales
	public void debitorPaymentPhase2(String notificationRef){
		core.paymentProtocolDebitorPhase2(notificationRef);
	}
	//generacion de los 3 ficheros finales del debtor y generacion de los 3 ultimos ficheros parciales del creditor
	public void debitorPaymentPhase3(String notificationRef){
		core.paymentProtocolDebitorPhase3(notificationRef);
	}
	//carga y validacion de los 3 ultimos ficheros parciales del creditor
	public void creditorPaymentPhase3(String notificationRef){
		core.paymentProtocolCreditorPhase3(notificationRef);
	}
	
	public void creditorPaymentPhase4(String notificationRef){
		core.paymentProtocolCreditorPhase4(notificationRef);
	}
}
