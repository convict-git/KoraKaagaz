package processing.threading;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.ClientBoardState;

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
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Spawning a new thread to send a message over the network"
		);
		
		/**
		 * Call the networking module send function so now this
		 * function will be run when the run is called of this class.
		 */
		try {
			ClientBoardState.communicator.send(address, message, identifier);
		} catch(Exception e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Error while sending a message over the network"
			);
			
		}
		
	}
	
}
