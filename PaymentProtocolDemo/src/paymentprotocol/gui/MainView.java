package paymentprotocol.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import paymentprotocol.control.Controller;
import paymentprotocol.observer.GUIObserver;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;

public class MainView extends JFrame implements GUIObserver{

	private JTextPane logTextPane;
	
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
		this.setBounds(100, 100, 587, 437);
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
		
		JPanel logPanel = new JPanel();
		panel.add(logPanel);
		GridBagLayout gbl_logPanel = new GridBagLayout();
		gbl_logPanel.columnWidths = new int[]{0, 0};
		gbl_logPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_logPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_logPanel.rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		logPanel.setLayout(gbl_logPanel);
		
		JPanel paymentsPanel = new JPanel();
		panel.add(paymentsPanel);
		GridBagLayout gbl_paymentsPanel = new GridBagLayout();
		gbl_paymentsPanel.columnWidths = new int[]{0, 0};
		gbl_paymentsPanel.rowHeights = new int[]{0, 0, 0};
		gbl_paymentsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_paymentsPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		paymentsPanel.setLayout(gbl_paymentsPanel);
		
		JLabel paymentsLabel = new JLabel("Payments");
		GridBagConstraints gbc_paymentsLabel = new GridBagConstraints();
		gbc_paymentsLabel.insets = new Insets(0, 0, 5, 0);
		gbc_paymentsLabel.gridx = 0;
		gbc_paymentsLabel.gridy = 0;
		paymentsPanel.add(paymentsLabel, gbc_paymentsLabel);
		
		JPanel paymentsSubpanel = new JPanel();
		GridBagConstraints gbc_paymentsSubpanel = new GridBagConstraints();
		gbc_paymentsSubpanel.fill = GridBagConstraints.BOTH;
		gbc_paymentsSubpanel.gridx = 0;
		gbc_paymentsSubpanel.gridy = 1;
		paymentsPanel.add(paymentsSubpanel, gbc_paymentsSubpanel);
		
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
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void failedConnection() {
		logTextPane.setText(logTextPane.getText() + "Failed connection: internal error.\n");
	}

	@Override
	public void onNoPendingTransactions() {
		logTextPane.setText(logTextPane.getText() + "There is not any transaction to load.\n");
	}

	@Override
	public void onReceiveNotification() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNodeAlreadyConnected() {
		logTextPane.setText(logTextPane.getText() + "Failed connection: Node already connected.\n");
	}

	@Override
	public void onPublicProfileLoaded(String name, String surname) {
		logTextPane.setText(logTextPane.getText() + "Public profile loaded succesfully. Name: "
				+ name + " Surname: " + surname + "\n");
	}

	@Override
	public void onTransactionLoaded(String transref) {
		logTextPane.setText(logTextPane.getText() + "Bill " + transref + " loaded.\n");
	}

	@Override
	public void onFailedPublicProfileFailLoad() {
		logTextPane.setText(logTextPane.getText() + "Public profile falied load.\n");
	}

}
