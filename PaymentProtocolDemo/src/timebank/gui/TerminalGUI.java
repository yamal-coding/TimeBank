package timebank.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import timebank.control.Controller;
import timebank.observer.GUIObserver;

public class TerminalGUI implements GUIObserver {
	private Controller c;
	private List<String> notifications;
	private List<String> transactions;
	
	private String outputPrefix;
	
	private Scanner in; 
	
	private boolean connectionSuccesful;
	
	public TerminalGUI(Controller c, String id){
		this.outputPrefix = "[" + id + "] ";
		this.connectionSuccesful = false;
		this.in = new Scanner(System.in);
		this.c = c;
		this.notifications = new ArrayList<String>();
		this.transactions = new ArrayList<String>();
		this.c.addObserver(this);
	}
	
	public synchronized void run(int bindport, String bootAddress, int bootport) throws InterruptedException{
		this.c.connect(bindport, bootAddress, bootport);
		
		//wait();
		
		if (connectionSuccesful){
			boolean exit = false;
			while (!exit){
				println("Notifications:");
				for (String notf : notifications)
					println("\t" + notf);
				
				println("Pending transactions:");
				for (String trns : transactions)
					println("\t" + trns);
				
				println(this.outputPrefix + "Choose an action:");
				println("\t1 - Select notification");
				println("\t2 - Select transaction");
				println("\t3 - View my public profile");
				println("\t0 - Exit");
				
				switch(readOptionChosen()){
					case 0: exit = true; break;
					case 1: selectNotification(); break;
					case 2: selectTransaction(); break;
					case 3: viewPublicProfile(); break;
					default:
				}
			}
		}
		else
			System.err.println("Error de conexión");
		
		//System.exit(0);
	}
	
	private void selectNotification(){
		println("Choose a notification by its index:");
		for (String notf : notifications)
			println("\t" + notf);
	}
	
	private void selectTransaction(){
		println("Choose a transaction by its index:");
		for (String trns : transactions)
			println("\t" + trns);
	}
	
	private void viewPublicProfile(){
		c.viewPublicProfile();
	}
	
	private int readOptionChosen(){
		boolean ok = false;
		int op = 0;
		
		String read = in.nextLine();
		do{
			try {
				op = (Integer) Integer.parseInt(read);
				
				if (op == 0 || op == 1 || op == 2 || op == 3)
					ok = true;
				else
					println("Enter the correct index of the action please:");
			}
			catch (Exception e) {
				println("Enter an integer value please:");
			}
		}while (!ok);
		
		return op;
		
	}
	
	private void print(String text){
		System.out.print(this.outputPrefix + text);
	}
	
	private void println(String text){
		System.out.println(this.outputPrefix + text);
	}
	
	@Override
	public synchronized void onSuccesfulConnection() {
		connectionSuccesful = true;
		notifyAll();
	}
	
	@Override
	public synchronized void failedConnection() {
		notifyAll();
	}

	@Override
	public synchronized void onNoPendingTransactions() {
		println("No hay transacciones pendientes");
	}

	@Override
	public synchronized void onReceiveNotification(String notification) {
		println("Nueva notificación recibida");
		notifications.add(notification);
	}

	@Override
	public synchronized void onNodeAlreadyConnected() {
		System.err.println(this.outputPrefix + "Nodo ya conectado a la red");
	}

	@Override
	public synchronized void onViewPublicProfile(String name, String surname, int phone, String email) {
		println("Name: " + name);
		println("Surname: " + surname);
	}
	
	@Override
	public synchronized void onTransactionLoaded(String transref){
		transactions.add(transref);
		println("Loaded transaction: " + transref);
		
	}
	
	@Override
	public synchronized void onFailedPublicProfileLoad(){
		println(this.outputPrefix + "Error al cargar el perfil publico");
	}

	@Override
	public synchronized void onFailedNotificationLoad() {
		println(this.outputPrefix + "Error al cargar la información asociada a la notificación.");
		println(this.outputPrefix + "Asegúrese de que ha elegido una notificación existente.");
	}

	@Override
	public void onViewTransaction(String ref, double hours, boolean isCreditor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase1Started(String notRef, String transRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase1ValidationSuccess(String notificationRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase2Started(String notRef, String transRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase2ValidationSuccess(String notificationRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase3ValidationSuccess(String notificationRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaymentPhase3Started(String notRef, String transRef) {
		// TODO Auto-generated method stub
		
	}
}