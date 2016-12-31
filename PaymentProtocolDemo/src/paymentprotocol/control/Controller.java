package paymentprotocol.control;

import java.util.List;

import paymentprotocol.model.Core;
import paymentprotocol.observer.GUIObserver;
import rice.p2p.commonapi.Id;

public class Controller {
	private Core core;
	
	public Controller(Core core){
		this.core = core;
	}
	
	public void addObserver(GUIObserver obs){
		core.addObserver(obs);
	}
	
	public void loadTransactions(List<Id> keys){
		core.loadTransactions(keys);
	}
	
	
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
