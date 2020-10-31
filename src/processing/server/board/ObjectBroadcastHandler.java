package processing.server.board;

import java.io.IOException;

import networking.INotificationHandler;
import processing.Serialize;
import processing.boardobject.*;

/**
 * This class implements INotificationHandler to handle
 * the event when client will send an object to broadcast.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ObjectBroadcastHandler implements INotificationHandler {

	public void onMessageReceived (String message) {
		
		BoardObject obj = null;
		
		try {
			obj = (BoardObject)Serialize.deSerialize(message);
		} catch (ClassNotFoundException e) {
			// log the exception
		} catch (IOException e) {
			// log the exception
		}
		
		// handle the object
		
		// broadcast the object
	}
	
}
