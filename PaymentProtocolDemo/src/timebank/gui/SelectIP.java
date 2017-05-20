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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import timebank.demos.paymentprotocoldemo.Demo;

public class SelectIP extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField puerto1;
	private JTextField puerto2;
	private JTextField puerto3;
	private JTextField enterIPTextField;
	/**
	 * Create the dialog.
	 */
	public SelectIP() {
		setBounds(100, 100, 442, 242);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPuerto_2 = new JLabel("Puerto 3:");
		lblPuerto_2.setBounds(59, 138, 70, 15);
		contentPanel.add(lblPuerto_2);
		
		puerto3 = new JTextField();
		puerto3.setBounds(136, 136, 114, 19);
		contentPanel.add(puerto3);
		puerto3.setColumns(10);
		
		JLabel lblPuerto_1 = new JLabel("Puerto 2:");
		lblPuerto_1.setBounds(59, 103, 70, 15);
		contentPanel.add(lblPuerto_1);
		
		puerto2 = new JTextField();
		puerto2.setBounds(136, 102, 114, 19);
		contentPanel.add(puerto2);
		puerto2.setColumns(10);
		
		enterIPTextField = new JTextField();
		enterIPTextField.setBounds(136, 40, 181, 19);
		contentPanel.add(enterIPTextField);
		
		JLabel lblPuerto = new JLabel("Puerto 1:");
		lblPuerto.setBounds(59, 73, 70, 15);
		contentPanel.add(lblPuerto);
		
		puerto1 = new JTextField();
		puerto1.setBounds(136, 71, 114, 19);
		contentPanel.add(puerto1);
		puerto1.setColumns(10);
		{
			JLabel lblNewLabel = new JLabel("IP:");
			lblNewLabel.setBounds(110, 42, 440, 15);
			contentPanel.add(lblNewLabel);
		}
		
		JLabel enterIPLabel = new JLabel("Introduce una dirección IP válida y tres puertos abiertos:");
		enterIPLabel.setBounds(12, 12, 410, 15);
		contentPanel.add(enterIPLabel);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String ip = enterIPTextField.getText();
							
							int p1 = Integer.parseInt(puerto1.getText());
							int p2 = Integer.parseInt(puerto2.getText());
							int p3 = Integer.parseInt(puerto3.getText());
							
							if (p1 == p2 || p1 == p3 || p2 == p3)
								throw new Exception();
							
							(new Demo()).runDemo(p1, p2, ip, p3);
							
							dispose();
						} catch (Exception e1) {
							System.out.println("Error en los campos de entrada");
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
