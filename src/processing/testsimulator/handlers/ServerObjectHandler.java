package processing.testsimulator.handlers;

import infrastructure.validation.logger.*;
import networking.INotificationHandler;
import processing.handlers.ObjectHandler;

/**
 * This class handles Board Object received from the network.
 * 
 * @author Sakshi Rathore
 *
 */

public class ServerObjectHandler implements INotificationHandler{
	
	/** receive the message from network **/
	public void onMessageReceived(String message) {
		
		ILogger logger = LoggerFactory.getLoggerInstance();
		/** 
		 * Create instance of processing module handler and pass message to it. 
		 * Processing module handler perform corresponding operation and
		 * compute the changes to be sent to UI
		 */
		ObjectHandler objectHandler = new ObjectHandler();
		objectHandler.onMessageReceived(message);
		logger.log(ModuleID.PROCESSING, 
				LogLevel.INFO,
				"Test: Passed the message to processing module ObjectHandler.");
	}
	
}