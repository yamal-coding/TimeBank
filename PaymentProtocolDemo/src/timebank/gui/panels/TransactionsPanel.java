package timebank.gui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import timebank.control.Controller;

public class TransactionsPanel extends JPanel implements ActionListener {
	private DefaultListModel<String> transactionsListModel;
	private JList<String> transactionsList;
	
	private Controller c;
	
	public TransactionsPanel(Controller c){
		this.c = c;
		
		setBorder(BorderFactory.createTitledBorder("Transactions"));
		setLayout(new BorderLayout());
		
		transactionsListModel = new DefaultListModel<String>();
		transactionsList = new JList<String>(transactionsListModel);
		
		JScrollPane transactionsScrollPane = new JScrollPane();
		transactionsScrollPane.setViewportView(transactionsList);
		
		add(transactionsScrollPane, BorderLayout.CENTER);
		
		JButton viewButton = new JButton("View transaction");
		viewButton.addActionListener(this);
		viewButton.setName("view");
		add(viewButton, BorderLayout.SOUTH);
	}
	
	public void addTransaction(String transref) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				transactionsListModel.addElement(transref);
			}
		});
	}
	
	public void deleteTransaction(String transRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				transactionsListModel.removeElement(transRef);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String option = b.getName();
		
		if (option.equals("view")){
			c.viewTransaction(transactionsList.getSelectedValue());
		}
	}
}
