package paymentprotocol.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;

public class FeedbackInput extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	private JTextPane textPane;
	
	private JRadioButton [] degree_radioButtons;
	private final int NUM_RADIOBUTTONS = 5;
	
	private final String [] degreeActionCommands = {Degree.ONE.toString(), Degree.TWO.toString(), Degree.THREE.toString(), Degree.FOUR.toString(), Degree.FIVE.toString()};
	private final String okActionCommand = "ok";
	private final String cancelActionCommand = "cancel";
	
	private Degree selectedDegree;
	
	private enum Degree {
		NONE("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");
		
		private String name;
		
		private Degree(String name){
			this.name= name;
		}
		
		public String toString(){
			return name;
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FeedbackInput dialog = new FeedbackInput();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FeedbackInput() {
		setBounds(100, 100, 621, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
			
		JLabel lblFeedbackAboutOther = new JLabel("Feedback about Me");
		contentPanel.add(lblFeedbackAboutOther, BorderLayout.NORTH);
		
		textPane = new JTextPane();
		textPane.setEditable(true);
		JScrollPane scrollPane = new JScrollPane(textPane);
		contentPanel.add(scrollPane);
		
	
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("Degree of satisfaction:");
		panel.add(lblNewLabel);
		
			
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		selectedDegree = Degree.NONE;
		
		degree_radioButtons = new JRadioButton[NUM_RADIOBUTTONS];
		
		for (int i = 0; i < degree_radioButtons.length; i++){
			degree_radioButtons[i] = new JRadioButton("" + (i + 1));
			degree_radioButtons[i].setActionCommand(degreeActionCommands[i]);
			degree_radioButtons[i].addActionListener(this);
			panel_1.add(degree_radioButtons[i]);
			
		}
		
		JPanel buttonPane = new JPanel();
		
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.setActionCommand(okActionCommand);
		okButton.addActionListener(this);
	
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.setActionCommand(cancelActionCommand);
		cancelButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		String action = e.getActionCommand();
		
		if (action.equals(okActionCommand)){
			
		}
		else if (action.equals(cancelActionCommand)){
			
		}
		else if (action.equals(degreeActionCommands[4])){
			deselectOthers(4);
		}
		else if (action.equals(degreeActionCommands[3])){
			deselectOthers(3);
		}
		else if (action.equals(degreeActionCommands[2])){
			deselectOthers(2);
		}
		else if (action.equals(degreeActionCommands[1])){
			deselectOthers(1);
		}		
		else if (action.equals(degreeActionCommands[0])){
			deselectOthers(0);
		}
	}
	
	private void deselectOthers(int index){
		for (int i = 0; i < degree_radioButtons.length; i++){
			if (i != index && degree_radioButtons[i].isSelected())
				degree_radioButtons[i].setSelected(false);
		}
	}
}
