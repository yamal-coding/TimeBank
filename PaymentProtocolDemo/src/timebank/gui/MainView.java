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
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JList;

public class MainView extends JFrame implements GUIObserver{

	private JTextPane logTextPane;
	private JLabel nameLabel;
	private JLabel surnameLabel;
	
	private DefaultListModel<String> transactionsListModel;
	private JList<String> transactionsList;
	
	private DefaultListModel<String> notificationsListModel;
	private JList<String> notificationsList;
	
	private Controller c;

	/**
	 * Create the application.
	 */
	public MainView(Controller c, int bindport, String bootAddress, int bootport) {
		super("Time bank");
		initialize();
		this.c = c;
		this.c.addObserver(this);
		this.c.connect(bindport, bootAddress, bootport);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		profileSubPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		nameLabel = new JLabel("");
		profileSubPanel.add(nameLabel);
		
		surnameLabel = new JLabel("");
		profileSubPanel.add(surnameLabel);
		
		JPanel paymentsPanel = new JPanel();
		panel.add(paymentsPanel);
		paymentsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel paymentsLabel = new JLabel("Transactions");
		paymentsPanel.add(paymentsLabel);
		
		JPanel paymentsSubpanel = new JPanel();
		paymentsPanel.add(paymentsSubpanel);
		
		transactionsListModel = new DefaultListModel<String>();
		transactionsList = new JList<String>(transactionsListModel);
		JScrollPane transactionsScrollPane = new JScrollPane();
		transactionsScrollPane.setViewportView(transactionsList);
		paymentsSubpanel.add(transactionsScrollPane);
		
		JPanel notificationsPanel = new JPanel();
		panel.add(notificationsPanel);
		GridBagLayout gbl_notificationsPanel = new GridBagLayout();
		gbl_notificationsPanel.columnWidths = new int[]{0, 0};
		gbl_notificationsPanel.rowHeights = new int[]{0, 0, 0};
		gbl_notificationsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_notificationsPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		notificationsPanel.setLayout(gbl_notificationsPanel);
		
		JLabel notificationsLabel = new JLabel("Notifications");
		GridBagConstraints gbc_notificationsLabel = new GridBagConstraints();
		gbc_notificationsLabel.insets = new Insets(0, 0, 5, 0);
		gbc_notificationsLabel.gridx = 0;
		gbc_notificationsLabel.gridy = 0;
		notificationsPanel.add(notificationsLabel, gbc_notificationsLabel);
		
		JPanel notificationsSubpanel = new JPanel();
		GridBagConstraints gbc_notificationsSubpanel = new GridBagConstraints();
		gbc_notificationsSubpanel.fill = GridBagConstraints.BOTH;
		gbc_notificationsSubpanel.gridx = 0;
		gbc_notificationsSubpanel.gridy = 1;
		notificationsPanel.add(notificationsSubpanel, gbc_notificationsSubpanel);
		
		notificationsListModel = new DefaultListModel<String>();
		notificationsList = new JList<String>(notificationsListModel);
		JScrollPane notificationsScrollPane = new JScrollPane();
		notificationsScrollPane.setViewportView(notificationsList);
		paymentsSubpanel.add(transactionsScrollPane);
		
		this.setVisible(true);
	}

	@Override
	public void failedConnection() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				logTextPane.setText(logTextPane.getText() + "Failed connection: internal error.\n");
			}
		});
	}

	@Override
	public void onNoPendingTransactions() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				logTextPane.setText(logTextPane.getText() + "There is not any transaction to load.\n");
			}
		});
	}

	@Override
	public void onReceiveNotification(String notification) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				SwingUtilities.invokeLater(new Runnable() {
					public void run(){
						logTextPane.setText(logTextPane.getText() + "Notification received.\n");
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
				logTextPane.setText(logTextPane.getText() + "Failed connection: Node already connected.\n");
			}
		});
	}

	@Override
	public void onPublicProfileLoaded(String name, String surname) {
		logTextPane.setText(logTextPane.getText() + "Public profile loaded succesfully.\n");
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				nameLabel.setText("Name: " + name);
				surnameLabel.setText("Surame: " + surname);
			}
		});
	}

	@Override
	public void onTransactionLoaded(String transref) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				logTextPane.setText(logTextPane.getText() + "Bill " + transref + " loaded.\n");
				transactionsListModel.addElement(transref);
			}
		});
	}

	@Override
	public void onFailedPublicProfileLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				logTextPane.setText(logTextPane.getText() + "Public profile falied load.\n");
			}
		});
	}

}
