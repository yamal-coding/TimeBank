package timebank.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import timebank.control.Controller;
import timebank.observer.GUIObserver;

public class LogPanel extends JPanel implements GUIObserver {
	private JTextPane logTextPane;
	
	private Controller c;
	
	public LogPanel(int w, int h, Controller c){
		setBorder(BorderFactory.createTitledBorder("Log"));
		setPreferredSize(new Dimension(w, h / 3));
		setLayout(new BorderLayout());
		
		logTextPane = new JTextPane();
		logTextPane.setEditable(false);
		
		JScrollPane logScrollPane = new JScrollPane(logTextPane);
		logScrollPane.setPreferredSize(new Dimension(w, h / 3));
		add(logScrollPane, BorderLayout.CENTER);
		
		this.c = c;
		c.addObserver(this);
	}

	private void log(String msg){
		logTextPane.setText(logTextPane.getText() + msg + "\n");
	}
	
	public void addObserver() {
		
	}
	
	@Override
	public void onSuccesfulConnection() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Succesful connection.");
			}
		});
	}

	@Override
	public void failedConnection() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Failed connection: internal error.");
			}
		});
	}

	@Override
	public void onNoPendingTransactions() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("There is not any transaction to load.");
			}
		});
	}

	@Override
	public void onReceiveNotification(String notification) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("New notification received: " + notification);
			}
		});	
	}

	@Override
	public void onNodeAlreadyConnected() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Failed connection: Node already connected.");
			}
		});
	}

	@Override
	public void onViewPublicProfile(String name, String surname, int phone, String email) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onViewTransaction(String ref, double hours, boolean isCreditor) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onTransactionLoaded(String transref) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Bill " + transref + " loaded.");
			}
		});
	}

	@Override
	public void onFailedPublicProfileLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Public profile falied load.");
			}
		});
	}

	@Override
	public void onFailedNotificationLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("An error occurred loading the selected notification.");
			}
		});
	}

	@Override
	public void onLogMessage(String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log(msg);
			}
		});
	}

	@Override
	public void onPaymentPhase1Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onPaymentPhase1ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Phase 1 validation success.");
			}
		});
	}

	@Override
	public void onPaymentPhase2Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onPaymentPhase2ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Phase 2 validation success.");
			}
		});
	}

	@Override
	public void onPaymentPhase3Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onPaymentPhase3ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Phase 3 validation success.");
			}
		});
	}

	@Override
	public void onDeleteNotification(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onPaymentFinished(String notificationRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Payment finished.");
			}
		});
	}
}
