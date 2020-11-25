package processing.testsimulator.handlers;

import infrastructure.validation.logger.*;
import networking.INotificationHandler;
import processing.CommunicateChange;
import processing.Serialize;
import processing.boardobject.BoardObject;
import processing.testsimulator.ui.ChangesHandler;

public class ClientObjectHandler implements INotificationHandler{
	
	/** receive the message from network **/
	public void onMessageReceived(String message) {
		
		ILogger logger = LoggerFactory.getLoggerInstance();
		/** 
		 * Create instance of processing module handler and pass message to it. 
		 * Processing module handler perform corresponding operation and
		 * compute the changes to be sent to UI
		 */
		BoardObject boardObject;
		
		try {
			boardObject = (BoardObject)Serialize.deSerialize(message);
			CommunicateChange.provideChanges(boardObject.getPixels(), null);
		} catch (Exception e) {
			
		}
		logger.log(ModuleID.PROCESSING, 
				LogLevel.INFO,
				"Test: Passed the message to processing module ObjectHandler.");
	}
	
}