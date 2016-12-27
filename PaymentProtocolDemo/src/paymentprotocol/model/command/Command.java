package paymentprotocol.model.command;

/**
 * This interface represents a command to be executed by the user in the application core
 * @author yamal
 *
 */
public interface Command {
	/**
	 * Command action to be implemented by each concrete command class
	 */
	public void execute();
}
