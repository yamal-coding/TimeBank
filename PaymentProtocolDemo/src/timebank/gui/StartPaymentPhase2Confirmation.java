package timebank.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import timebank.control.Controller;

/**
 * 
 * @author yamal
 *
 */
public class StartPaymentPhase2Confirmation extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private Controller c;
	private String notificationRef;
	private String transactionRef;
	
	public StartPaymentPhase2Confirmation(Controller c, String notRef, String transRef){
		this.c = c;
		this.notificationRef = notRef;
		this.transactionRef = transRef;
		
		initGUI();
	}
	
	private void initGUI() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new GridLayout(4, 1));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.add(new JLabel("You have a new notification:"));
		contentPanel.add(new JLabel("<html>" + "Do you want to validate the previouse "
				+ "phase and start the second phase of this payment?" + "</html>"));
		contentPanel.add(new JLabel("Notification reference: " + notificationRef));
		contentPanel.add(new JLabel("This notification refers to the transaction: " + transactionRef));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				c.creditorPaymentPhase1(notificationRef);
				dispose();
			}
		});
	
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
