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

public class NotificationsPanel extends JPanel implements ActionListener {
	private DefaultListModel<String> notificationsListModel;
	private JList<String> notificationsList;
	
	private Controller c;
	
	public NotificationsPanel(Controller c){
		this.c = c;
		
		setBorder(BorderFactory.createTitledBorder("Notifications"));
		setLayout(new BorderLayout());
		
		notificationsListModel = new DefaultListModel<String>();
		notificationsList = new JList<String>(notificationsListModel);
		JScrollPane transactionsScrollPane = new JScrollPane();
		transactionsScrollPane.setViewportView(notificationsList);
		
		add(transactionsScrollPane, BorderLayout.CENTER);
		
		JButton viewButton = new JButton("View notification");
		viewButton.addActionListener(this);
		viewButton.setName("view");
		add(viewButton, BorderLayout.SOUTH);
	}
	
	public void addNotification(String notification) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				notificationsListModel.addElement(notification);
			}
		});
	}
	
	public void deleteNotification(String notificationRef) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				notificationsListModel.removeElement(notificationRef);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String option = b.getName();
		
		if (option.equals("view")){
			c.handleNotification(notificationsList.getSelectedValue());
		}
	}
}

