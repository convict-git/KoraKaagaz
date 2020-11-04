package processing.handlers;

import java.io.IOException;
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
	
	public static void handleBoardObject(String message) {
		

		try {
				BoardObject boardObject = (BoardObject)Serialize.deSerialize(message);
				boardObject.getOperation();
		} catch (ClassNotFoundException e) {
			//Log the exception
		} catch (IOException e) {
			//Log the exception
		}
		
	}
	
	public void onMessageReceived(String message) {
	
		HandleBoardObject runnable = new HandleBoardObject(message);
		Thread objectHandler = new Thread(runnable);
		objectHandler.start();
		
	}
}
