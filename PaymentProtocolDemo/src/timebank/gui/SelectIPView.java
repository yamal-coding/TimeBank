package timebank.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SelectIPView extends JFrame {
	private JLabel enterIPLabel;
	private JTextField enterIPTextField;
	private JButton okButton;
	private JButton cancelButton;
		
	public SelectIPView() {
		super("Time bank");
		this.setSize(500, 200);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.setLayout(null);
		
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setLocation((int) (screenSize.getWidth() / 2 - (this.getWidth() * 2 / 3) / 2), 
			//	(int) (screenSize.getHeight() / 2 - (this.getHeight() * 2 / 3) / 2));
		
		enterIPLabel = new JLabel("Introduce una dirección IP válida:");
		enterIPLabel.setBounds(this.getWidth() / 5 + 30, 20, this.getWidth(), 20);
		//this.add(enterIPLabel);
		
		enterIPTextField = new JTextField();
		enterIPTextField.setBounds(this.getWidth() / 5 + 30, 30, this.getWidth(), 20);
		this.add(enterIPTextField);
		
		okButton = new JButton("Ok");
		okButton.setBounds(this.getWidth() - 65, this.getHeight() - 85, 60, 30);
		this.add(okButton);
		/*okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(this.getWidth() - 165, this.getHeight() - 85, 90, 30);
		this.add(cancelButton);
		/*cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});*/
	}
}
