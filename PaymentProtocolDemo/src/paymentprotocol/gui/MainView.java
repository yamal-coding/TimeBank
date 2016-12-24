package paymentprotocol.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

public class MainView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel logPanel = new JPanel();
		frame.getContentPane().add(logPanel);
		GridBagLayout gbl_logPanel = new GridBagLayout();
		gbl_logPanel.columnWidths = new int[]{0, 0};
		gbl_logPanel.rowHeights = new int[]{0, 0, 0};
		gbl_logPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_logPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		logPanel.setLayout(gbl_logPanel);
		
		JLabel logLabel = new JLabel("System logs");
		GridBagConstraints gbc_logLabel = new GridBagConstraints();
		gbc_logLabel.insets = new Insets(0, 0, 5, 0);
		gbc_logLabel.gridx = 0;
		gbc_logLabel.gridy = 0;
		logPanel.add(logLabel, gbc_logLabel);
		
		JTextPane logTextPane = new JTextPane();
		logTextPane.setEditable(false);
		GridBagConstraints gbc_logTextPane = new GridBagConstraints();
		gbc_logTextPane.fill = GridBagConstraints.BOTH;
		gbc_logTextPane.gridx = 0;
		gbc_logTextPane.gridy = 1;
		logPanel.add(logTextPane, gbc_logTextPane);
		
		JPanel paymentsPanel = new JPanel();
		frame.getContentPane().add(paymentsPanel);
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
		frame.getContentPane().add(notificationsPanel);
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
		
	}

}
