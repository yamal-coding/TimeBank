package paymentprotocol.control;

import paymentprotocol.model.Core;
import paymentprotocol.model.command.Command;
import paymentprotocol.observer.GUIObserver;

public class Controller {
	private Core core;
	
	public Controller(Core core){
		this.core = core;
	}
	
	public void addObserver(GUIObserver obs){
		core.addObserver(obs);
	}
	
	//
	public void startPaymentProtocolPhase1(){
		
	}
	
	public void startPaymentProtocolPhase2(){
		
	}
	
	public void startPaymentProtocolPhase3(){
		
	}
	
	public void startPaymentProtocolPhase4(){
		
	}
	
	/*public void executeCommand(Command c){
		core.executeCommand(c);
	}*/
}
