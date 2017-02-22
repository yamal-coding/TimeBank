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

public class StartPaymentPhase4Confirmation extends JDialog {

	private final JPanel contentPanel = new JPanel();
		
	private Controller c;
	private String notRef;
	private String transRef;
	
	public StartPaymentPhase4Confirmation(Controller c, String notRef, String transRef){
		this.c = c;
		this.notRef = notRef;
		this.transRef = transRef;
		
		initGUI();
	}
	
	public void initGUI() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new GridLayout(4, 1));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.add(new JLabel("You have a new notification:"));
		contentPanel.add(new JLabel("Do you want to validate the previous phase and start the fourth phase of this payment?"));
		contentPanel.add(new JLabel("Notification reference: " + notRef));		
		contentPanel.add(new JLabel("This notification refers to the transaction: " + transRef));

		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.creditorPaymentPhase3(notRef);
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
