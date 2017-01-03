package paymentprotocol.control;


import paymentprotocol.model.Core;
import paymentprotocol.observer.GUIObserver;

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
	
	
	public void startPaymentProtocolPhase1(){
		
	}
	
	public void startPaymentProtocolPhase2(){
		
	}
	
	public void startPaymentProtocolPhase3(){
		
	}
	
	public void startPaymentProtocolPhase4(){
		
	}
}
