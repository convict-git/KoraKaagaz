package processing.testsimulator;

import networking.INotificationHandler;
import processing.handlers.*;

public class ServerObjectHandler implements INotificationHandler{
	public void onMessageReceived(String message) {
		ObjectHandler objectHandler = new ObjectHandler();
		objectHandler.onMessageReceived(message);
	}
}
