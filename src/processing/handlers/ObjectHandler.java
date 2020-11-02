package processing.handlers;

import java.io.IOException;
import processing.*;
import processing.boardobject.*;
import networking.INotificationHandler;

public class ObjectHandler implements INotificationHandler{
	
	public void onMessageReceived(String message) {
		
		try {
				BoardObject boardObject = (BoardObject)Serialize.deSerialize(message);
				boardObject.getOperation();
		} catch (ClassNotFoundException e) {
			//Log the exception
		} catch (IOException e) {
			//Log the exception
		}
	}
}
