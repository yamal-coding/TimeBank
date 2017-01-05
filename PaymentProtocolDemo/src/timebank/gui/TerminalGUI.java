package timebank.gui;

import timebank.control.Controller;
import timebank.observer.GUIObserver;

public class TerminalGUI implements GUIObserver {
	private Controller c;
	
	public TerminalGUI(Controller c, int bindport, String bootAddress, int bootport){
		this.c = c;
		this.c.addObserver(this);
		this.c.connect(bindport, bootAddress, bootport);
	}
	
	@Override
	public void failedConnection() {
		System.err.println("Error de conexión");
	}

	@Override
	public void onNoPendingTransactions() {
		System.out.println("No hay transacciones pendientes");
	}

	@Override
	public void onReceiveNotification(String notification) {
		System.out.println("Notificación recibida");
	}

	@Override
	public void onNodeAlreadyConnected() {
		System.err.println("Nodo ya conectado a la red");
	}

	@Override
	public void onPublicProfileLoaded(String name, String surname) {
		System.out.println("Name: " + name);
		System.out.println("Surname: " + surname);
	}
	
	@Override
	public void onTransactionLoaded(String transref){
		System.out.println("Bill: " + transref);
	}
	
	@Override
	public void onFailedPublicProfileLoad(){
		System.err.println("Error al cargar el perfil publico");
	}
}