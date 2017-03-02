package timebank.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import timebank.control.Controller;
import javax.swing.JLabel;

/**
 * This GUI displays a window with information of a transaction
 * @author yamal
 *
 */
public class ViewTransaction extends JDialog{
	private Controller c;
	private String ref;
	private double hours;
	//If this attribute is set to false, the transaction must be paid (By the debtor)
	private boolean isCreditor;
	
	private final JPanel contentPanel = new JPanel();

	public ViewTransaction(Controller c, String ref, double hours, boolean isCreditor){
		this.c = c;
		this.ref = ref;
		this.hours = hours;
		this.isCreditor = isCreditor;
		
		initGUI();
	}

	private void initGUI() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new GridLayout(3, 1));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel refLabel = new JLabel("Transaction reference: " + this.ref);
		JLabel hoursLabel = new JLabel("Service hours: " + this.hours);
		
		contentPanel.add(refLabel);

		contentPanel.add(hoursLabel);
		
		if (!isCreditor) {
			JButton paymentButton = new JButton("Pay bill");
			contentPanel.add(paymentButton);
			paymentButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new FeedbackInput(c, ref, isCreditor);
					dispose();
				}
			});
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
