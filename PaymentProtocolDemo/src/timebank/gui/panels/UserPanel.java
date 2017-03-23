package timebank.gui.panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import timebank.control.Controller;

public class UserPanel extends JPanel {
	private JLabel nameLabel;
	private JLabel surnameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;
	
	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JTextField phoneTextField;
	private JTextField emailTextField;
	
	private Controller c;
	
	public UserPanel(int w, int h, Controller c){
		this.c = c;
		
		setBorder(BorderFactory.createTitledBorder("User information"));
		initFields();
		locateFields(w, h);
	}
	
	private void initFields(){
		nameLabel = new JLabel("Name:");
		surnameLabel = new JLabel("Surname:");
		phoneLabel = new JLabel("Phone:");
		emailLabel = new JLabel("E-mail:");
		
		nameTextField = new JTextField();
		nameTextField.setEditable(false);
		surnameTextField = new JTextField();
		surnameTextField.setEditable(false);
		phoneTextField = new JTextField();
		phoneTextField.setEditable(false);
		emailTextField = new JTextField();
		emailTextField.setEditable(false);
	}
	
	private void locateFields(int w, int h){
		/*setLayout(null);
		setPreferredSize(new Dimension(w,  h));
		nameLabel.setBounds(w / 10, h / 10, 50, 20);
		surnameLabel.setBounds(w / 10, h / 10 * 2, 70, 20);
		phoneLabel.setBounds(w / 10, h / 10 * 3, 50, 20);
		emailLabel.setBounds(w / 10, h / 10 * 4, 50, 20);*/
		setLayout(new GridLayout(4, 1));
		setPreferredSize(new Dimension(w,  h));
		
		add(nameLabel);
		add(surnameLabel);
		add(phoneLabel);
		add(emailLabel);
	}
	
	public void updateFields(String name, String surname, int phone, String email){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				nameLabel.setText("Name: " + name);
				surnameLabel.setText("Surname: " + surname);
				phoneLabel.setText("Phone: " + phone);
				emailLabel.setText("E-mail: " + email);
			}
		});
	}
}
