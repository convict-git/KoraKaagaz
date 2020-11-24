package processing.testsimulator.network;

import java.util.*;

import infrastructure.validation.logger.*;
import networking.*;

/**
 * Communicator implements ICommunicator interface and is used by Processor
 * module to start, stop, send message and subscribe for messages. This can 
 * only be used for testing purpose as it does not start the network connection
 * rather the data is send back to the same client using appropriate handler. 
 *  
 * @author Sakshi Rathore
 */

public class Communicator implements ICommunicator{
	
	/**
	 * Make the constructor protected so that this class can not 
	 * be instantiated by program in some other package so as to 
	 * implement factory pattern
	 */
	protected Communicator() {};
	
	/* Get logger instance */
	ILogger logger = LoggerFactory.getLoggerInstance();
	
	/** Hashmap contains the handlers for the respective modules processing **/
	private HashMap<String, INotificationHandler> handlerMap = new HashMap < > ();
	
	@Override
	public void start() {
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Test: Successfully call start API of the network."
		);
	}

	@Override
	public void stop() {
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Test: Successfully call stop API of network."
		);
	}

	@Override
	public void send(String destination, String message, String identifier) {
		
		// System.out.println("send through network " + identifier);
		INotificationHandler handler = handlerMap.get(identifier);
		handler.onMessageReceived(message);
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"Test: Received the sent message"
		);
	}

	@Override
	public void subscribeForNotifications(String identifier, 
			INotificationHandler handler) {
		/* Validate the handler */
		if (handler == null ) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.WARNING, 
					"Test: Handler is invalid."
			);
		}
		/* Validate the identifier */
		else if (identifier=="" || identifier==null) {
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.WARNING,
					"Test: Provide a valid identifier"
			);
		}
		else {
			
			if (handlerMap.containsKey(identifier)) {
				logger.log(
						ModuleID.PROCESSING, 
						LogLevel.INFO, 
						"Test: Already have the specified identifier"
				);
			}
			else {
				logger.log(
						ModuleID.PROCESSING, 
						LogLevel.SUCCESS, 
						"Test: Subscribed network for any notifications."
				);
			}
			/* Inserting the specified identifier into the hashmap */
			handlerMap.put(identifier,handler);
		}
	}
}
