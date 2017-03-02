package timebank.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import timebank.control.Controller;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;

/**
 * 
 * @author yamal
 *
 */
public class FeedbackInput extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	private JTextPane textPane;
	
	private JRadioButton [] degree_radioButtons;
	private final int NUM_RADIOBUTTONS = 5;
	
	private final String [] degreeActionCommands = {Degree.ONE.toString(), Degree.TWO.toString(), Degree.THREE.toString(), Degree.FOUR.toString(), Degree.FIVE.toString()};
	private final String okActionCommand = "ok";
	private final String cancelActionCommand = "cancel";
	
	private Degree selectedDegree;
	
	private Controller c;
	private String ref;
	private boolean isCreditor;
	
	private enum Degree {
		NONE("0", 0), ONE("1", 1), TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5);
		
		private String name;
		private int index;
		
		private Degree(String name, int index){
			this.name= name;
			this.index = index;
		}
		
		public int getIndex(){
			return index;
		}
		
		public String toString(){
			return name;
		}
	}
	

	public FeedbackInput(Controller c, String ref, boolean isCreditor){
		this.c = c;
		this.ref = ref;
		this.isCreditor = isCreditor;
		
		initGUI();
	}
	
	public void initGUI() {
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
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if (action.equals(okActionCommand)){
			if (selectedDegree == Degree.NONE){
				//mensaje de que hay que seleccionar un degree of satisfaction
				
			}
			else {
				String feedback = textPane.getText();
				int degree = selectedDegree.getIndex();
				
				if (isCreditor){
					c.creditorPaymentPhase2(ref, feedback, degree);
				}
				else
					c.debitorPaymentPhase1(ref, feedback, degree);
				
				dispose();
			}
		}
		else if (action.equals(cancelActionCommand))
			dispose();
		
		else if (action.equals(Degree.FIVE.toString())){
			deselectOthers(Degree.FIVE.getIndex());
			selectedDegree = Degree.FIVE;
		}
		else if (action.equals(Degree.FOUR.toString())){
			deselectOthers(Degree.FOUR.getIndex());
			selectedDegree = Degree.FOUR;
		}
		else if (action.equals(Degree.THREE.toString())){
			deselectOthers(Degree.THREE.getIndex());
			selectedDegree = Degree.THREE;
		}
		else if (action.equals(Degree.TWO.toString())){
			deselectOthers(Degree.TWO.getIndex());
			selectedDegree = Degree.TWO;
		}
		else if (action.equals(Degree.ONE.toString())){
			deselectOthers(Degree.ONE.getIndex());
			selectedDegree = Degree.ONE;
		}
	}
	
	private void deselectOthers(int index){
		for (int i = 0; i < degree_radioButtons.length; i++){
			if (i != index - 1 && degree_radioButtons[i].isSelected())
				degree_radioButtons[i].setSelected(false);
		}
	}
}
