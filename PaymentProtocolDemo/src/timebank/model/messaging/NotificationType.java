package timebank.model.messaging;

/**
 * These enumerates are declared to distinguish the multiple notifications
 * interchanged during the payment protocol
 * @author yamal
 *
 */
public enum NotificationType {
	DEBITOR_PAYMENT_PHASE1, //First type of notification sent by debtor to creditor
	CREDITOR_PAYMENT_PHASE2, //Second notification type sent by creditor to debtor
	DEBITOR_PAYMENT_PHASE3 //Last type of notification sent by debtor to creditor
	
}
