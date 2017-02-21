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
	
	public void debitorPaymentPhase1(String ref, String comment, int degreeOfStisfaction){
		core.paymentProtocolDebtorPhase1(ref, comment, degreeOfStisfaction);
		
	}
	
	public void creditorPaymentPhase1(String notificationRef){
		core.paymentProtocolCreditorPhase1(notificationRef);
	}
	
	public void creditorPaymentPhase2(String notificationRef, String comment, int degreeOfSatisfaction){
		core.paymentProtocolCreditorPhase2(notificationRef, comment, degreeOfSatisfaction);
	}
	
	public void debitorPaymentPhase2(String notificationRef){
		core.paymentProtocolDebitorPhase2(notificationRef);
	}
	
	public void debitorPaymentPhase3(String notificationRef){
		core.paymentProtocolDebitorPhase3(notificationRef);
	}
	
	/*
	public void startPaymentProtocolPhase1(){
		
	}
	
	public void startPaymentProtocolPhase2(){
		
	}
	
	public void startPaymentProtocolPhase3(){
		
	}
	
	public void startPaymentProtocolPhase4(){
		
	}*/
}
