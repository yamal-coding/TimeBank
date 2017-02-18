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
