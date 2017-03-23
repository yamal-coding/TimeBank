package timebank.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import timebank.control.Controller;
import timebank.gui.dialogs.FeedbackInput;
import timebank.gui.dialogs.FinishedPayment;
import timebank.gui.dialogs.StartPaymentPhase2Confirmation;
import timebank.gui.dialogs.StartPaymentPhase3Confirmation;
import timebank.gui.dialogs.StartPaymentPhase4Confirmation;
import timebank.gui.dialogs.ViewTransaction;
import timebank.gui.panels.LogPanel;
import timebank.gui.panels.NotificationsPanel;
import timebank.gui.panels.TransactionsPanel;
import timebank.gui.panels.UserPanel;
import timebank.observer.GUIObserver;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JList;

/**
 * Main view of the application
 * @author yamal
 *
 */
public class MainView extends JFrame implements GUIObserver{
	private UserPanel userPanel;
	private TransactionsPanel transactionsPanel;
	private NotificationsPanel notificationsPanel;
	private LogPanel logPanel;
	
	private Controller c;

	/**
	 * Create the application.
	 */
	public MainView(Controller c, int bindport, String bootAddress, int bootport) {
		super("Time bank");
		this.c = c;
		initGUI();
		this.c.addObserver(this);
		this.c.connect(bindport, bootAddress, bootport);
	}
	
	public void initGUI(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3);
		setResizable(false);
		setLayout(null);
		setLocation((int) (screenSize.getWidth() / 2 - (screenSize.getWidth() * 2 / 3) / 2), 
				(int) (screenSize.getHeight() / 2 - (screenSize.getHeight() * 2 / 3) / 2));
		
		userPanel = new UserPanel(this.getWidth() / 3, (this.getHeight() / 3) * 2 - 20, c);
		userPanel.setBounds(0, 0, this.getWidth() / 3, (this.getHeight() / 3) * 2 - 20);
		add(userPanel);
		
		transactionsPanel = new TransactionsPanel(c);
		transactionsPanel.setBounds(this.getWidth() / 3, 0, this.getWidth() / 3, (this.getHeight() / 3) * 2 - 20);
		add(transactionsPanel);
		
		notificationsPanel = new NotificationsPanel(c);
		notificationsPanel.setBounds((this.getWidth() / 3) * 2, 0, this.getWidth() / 3, (this.getHeight() / 3) * 2 - 20);
		add(notificationsPanel);
		
		logPanel = new LogPanel(this.getWidth(), this.getHeight(), c);
		logPanel.setBounds(0, this.getHeight() - (this.getHeight() / 3) - 20, this.getWidth() - 4, this.getHeight() / 3 - 20);
		add(logPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
	}

	@Override
	public void failedConnection() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onNoPendingTransactions() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onReceiveNotification(String notification) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				//notificationsListModel.addElement(notification);
				notificationsPanel.addNotification(notification);
			}
		});
	}

	@Override
	public void onNodeAlreadyConnected() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onViewTransaction(String ref, double hours, boolean isCreditor) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new ViewTransaction(c, ref, hours, isCreditor);
			}
		});
	}
	
	@Override
	public void onViewPublicProfile(String name, String surname, int phone, String email) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				userPanel.updateFields(name, surname, phone, email);
			}
		});
	}

	@Override
	public void onTransactionLoaded(String transref) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				//transactionsListModel.addElement(transref);
				transactionsPanel.addTransaction(transref);
			}
		});
	}

	@Override
	public void onFailedPublicProfileLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onFailedNotificationLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onSuccesfulConnection() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}


	@Override
	public void onLogMessage(String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
			}
		});
	}

	@Override
	public void onPaymentPhase1Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				//Las cosas que se preguntaran aqui son si el Creditor desea validar ahora o no
				//los ficheros parciales de la primera fase del pago creados por el Debtor asociados
				//a la notificacion recibida
				new StartPaymentPhase2Confirmation(c, notRef, transRef);
			}
		});
	}

	@Override
	public void onPaymentPhase1ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				//Notificar que la validacion de los ficheros de la primera fase son correctos
				//y pedir el feedback y enviarlo para empezar la segunda fase por parte del creditor
				new FeedbackInput(c, notificationRef, true);
			}
		});	
	}

	@Override
	public void onPaymentPhase2Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new StartPaymentPhase3Confirmation(c, notRef, transRef);
			}
		});
	}
	
	@Override
	public void onPaymentPhase2ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
				c.debitorPaymentPhase3(notificationRef);
			}
		});
	}
	
	@Override
	public void onPaymentPhase3Started(String notRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new StartPaymentPhase4Confirmation(c, notRef, transRef);
			}
		});
	}
	
	@Override
	public void onPaymentPhase3ValidationSuccess(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				c.creditorPaymentPhase4(notificationRef);
			}
		});
	}

	@Override
	public void onDeleteNotification(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				notificationsPanel.deleteNotification(notificationRef);
			}
		});
	}

	@Override
	public void onPaymentFinished(String notificationRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new FinishedPayment(transRef);
				notificationsPanel.deleteNotification(notificationRef);
				transactionsPanel.deleteTransaction(transRef);
			}
		});
	}
}
