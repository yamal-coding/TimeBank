package paymentprotocol.model.files.persistent;

import paymentprotocol.model.command.Command;

public class Core {
	
	public void connect(){
		
	}
	
	public void executeCommand(Command c){
		c.execute();
	}
}
