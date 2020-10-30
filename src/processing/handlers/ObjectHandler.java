package processing.handlers;

import java.io.IOException;

import networking.INotificationHandler;
import processing.*;
import processing.boardobject.*;

public class ObjectHandler implements INotificationHandler{

	public void onMessageReceived (String message) {
		
		try {
			BoardObject boardObject = (BoardObject)Serialize.deSerialize(message);
			boardObject.getOperation();
			// Handle the boardObject
		} catch (ClassNotFoundException e) {
			// Log the exception
		} catch (IOException e) {
			// Log the exception
		}
	}
	
}
