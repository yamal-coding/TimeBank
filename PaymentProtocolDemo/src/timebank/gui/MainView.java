package timebank.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import timebank.control.Controller;
import timebank.observer.GUIObserver;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
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
public class MainView extends JFrame implements ActionListener, GUIObserver{

	private JTextPane logTextPane;
	private JLabel nameLabel;
	private JLabel surnameLabel;
	private JLabel phoneNumber;
	private JLabel emailLabel;
	
	private DefaultListModel<String> transactionsListModel;
	private JList<String> transactionsList;
	
	private DefaultListModel<String> notificationsListModel;
	private JList<String> notificationsList;
	
	private JButton viewTransaction;
	private JButton viewNotification;
	
	private Controller c;

	/**
	 * Create the application.
	 */
	public MainView(Controller c, int bindport, String bootAddress, int bootport) {
		super("Time bank");
		initGUI();
		this.c = c;
		this.c.addObserver(this);
		this.c.connect(bindport, bootAddress, bootport);
	}

	private void initGUI() {
		this.setBounds(100, 100, 736, 437);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		logTextPane = new JTextPane();
		logTextPane.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(logTextPane);
		scrollPane.setPreferredSize(new Dimension(this.getSize().width, this.getSize().height / 3));
		this.getContentPane().add(scrollPane, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel profilePanel = new JPanel();
		panel.add(profilePanel);
		GridBagLayout gbl_profilePanel = new GridBagLayout();
		gbl_profilePanel.columnWidths = new int[]{0, 0};
		gbl_profilePanel.rowHeights = new int[]{0, 0, 0};
		gbl_profilePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_profilePanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		profilePanel.setLayout(gbl_profilePanel);
		
		JLabel publicProfileLabel = new JLabel("Public Profile");
		GridBagConstraints gbc_publicProfileLabel = new GridBagConstraints();
		gbc_publicProfileLabel.insets = new Insets(0, 0, 5, 0);
		gbc_publicProfileLabel.gridx = 0;
		gbc_publicProfileLabel.gridy = 0;
		profilePanel.add(publicProfileLabel, gbc_publicProfileLabel);
		
		JPanel profileSubPanel = new JPanel();
		GridBagConstraints gbc_profileSubPanel = new GridBagConstraints();
		gbc_profileSubPanel.fill = GridBagConstraints.BOTH;
		gbc_profileSubPanel.gridx = 0;
		gbc_profileSubPanel.gridy = 1;
		profilePanel.add(profileSubPanel, gbc_profileSubPanel);
		profileSubPanel.setLayout(new GridLayout(4, 1));
		
		nameLabel = new JLabel("");
		profileSubPanel.add(nameLabel);
		
		surnameLabel = new JLabel("");
		profileSubPanel.add(surnameLabel);
		
		phoneNumber = new JLabel("");
		profileSubPanel.add(phoneNumber);
		
		emailLabel = new JLabel("");
		profileSubPanel.add(emailLabel);
		
		
		JPanel paymentsPanel = new JPanel();
		panel.add(paymentsPanel);
		paymentsPanel.setLayout(new BorderLayout());
		
		JLabel paymentsLabel = new JLabel("Transactions");
		paymentsPanel.add(paymentsLabel, BorderLayout.NORTH);
		
		JPanel paymentsSubpanel = new JPanel();
		paymentsPanel.add(paymentsSubpanel, BorderLayout.CENTER);
		
		transactionsListModel = new DefaultListModel<String>();
		transactionsList = new JList<String>(transactionsListModel);
		JScrollPane transactionsScrollPane = new JScrollPane();
		transactionsScrollPane.setViewportView(transactionsList);
		paymentsSubpanel.add(transactionsScrollPane);
		
		viewTransaction = new JButton("View");
		viewTransaction.setName("viewTransaction");
		viewTransaction.addActionListener(this);
		paymentsPanel.add(viewTransaction, BorderLayout.SOUTH);
		
		JPanel notificationsPanel = new JPanel();
		panel.add(notificationsPanel);
		notificationsPanel.setLayout(new BorderLayout());
		
		JLabel notificationsLabel = new JLabel("Notifications");
		notificationsPanel.add(notificationsLabel, BorderLayout.NORTH);
		
		JPanel notificationsSubpanel = new JPanel();
		notificationsPanel.add(notificationsSubpanel, BorderLayout.CENTER);
		
		notificationsListModel = new DefaultListModel<String>();
		notificationsList = new JList<String>(notificationsListModel);
		JScrollPane notificationsScrollPane = new JScrollPane();
		notificationsScrollPane.setViewportView(notificationsList);
		notificationsSubpanel.add(notificationsScrollPane);
		
		
		viewNotification = new JButton("View");
		notificationsPanel.add(viewNotification, BorderLayout.SOUTH);
		viewNotification.setActionCommand("viewNotification");
		viewNotification.setName("viewNotification");
		viewNotification.addActionListener(this);
		
		this.setVisible(true);
	}
	
	private void log(String msg){
		logTextPane.setText(logTextPane.getText() + msg + "\n");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String option = b.getName();
		
		if (option.equals("viewNotification")){
			c.handleNotification(notificationsList.getSelectedValue());
		}
		else if (option.equals("viewTransaction")){
			c.viewTransaction(transactionsList.getSelectedValue());
		}
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
				SwingUtilities.invokeLater(new Runnable() {
					public void run(){
						log("New notification received: " + notification);
						notificationsListModel.addElement(notification);
					}
				});
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
				nameLabel.setText("Name: " + name);
				surnameLabel.setText("Surname: " + surname);
				phoneNumber.setText("Phone: " + phone);
				emailLabel.setText("E-mail: " + email);
			}
		});
	}

	@Override
	public void onTransactionLoaded(String transref) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				log("Bill " + transref + " loaded.");
				transactionsListModel.addElement(transref);
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
		log("An error occurred loading the selected notification.");
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
				log("Phase 1 validation success.");
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
				log("Phase 2 validation success.");
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
				log("Phase 3 validation success.");
				c.creditorPaymentPhase4(notificationRef);
			}
		});
	}

	@Override
	public void onDeleteNotification(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				notificationsListModel.removeElement(notificationRef);
			}
		});
	}

	@Override
	public void onPaymentFinished(String notificationRef, String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new FinishedPayment(transRef);
				notificationsListModel.removeElement(notificationRef);
			}
		});
	}
}
