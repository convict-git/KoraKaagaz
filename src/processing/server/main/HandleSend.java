package processing.server.main;


/**
 * This implements Runnable so as to spawn a new thread to call the 
 * send function of the networking module.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class HandleSend implements Runnable{

	/**
	 * address will store the address of the receiver
	 * whom to send the message using networking module.
	 */
	String address;
	
	/**
	 * message will store the message to be sent to the receiver
	 * using the networking module.
	 */
	String message;
	
	/**
	 * identifier will store the identifier of the message that
	 * to be sent.
	 */
	String identifier;
	
	/**
	 * Constructor to set the variables defined above
	 * 
	 * @param address address of the receiver
	 * @param message message to be sent
	 * @param identifier identifier of the message
	 */
	public HandleSend(
			String address,
			String message,
			String identifier
	) {
		this.message = message;
		this.address = address;
		this.identifier = identifier;
	}
	
	/**
	 * Runnable interface run function implementation
	 */
	@Override
	public void run() {
		
		/**
		 * Call the networking module send function so now this
		 * function will be run when the run is called of this class.
		 */
		ServerState.communicator.send(address, message, identifier);
		
	}
	
}
