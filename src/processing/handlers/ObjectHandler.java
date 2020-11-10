package processing.handlers;

import java.io.IOException;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import processing.threading.*;
import processing.*;
import processing.boardobject.*;
import networking.INotificationHandler;

/**
 * This class handles Board Object received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class ObjectHandler implements INotificationHandler{
	
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	public static void handleBoardObject(String message) {
		

		try {
				BoardObject boardObject = (BoardObject)Serialize.deSerialize(message);
				boardObject.getOperation();
		} catch (ClassNotFoundException e) {
			//Log the exception
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"Class Not Found"
			);
			
		} catch (IOException e) {
			//Log the exception
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"IO Not Found"
			);
			
		}
		
	}
	
	public void onMessageReceived(String message) {
	
		HandleBoardObject runnable = new HandleBoardObject(message);
		Thread objectHandler = new Thread(runnable);
		objectHandler.start();
		
	}
}
