package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;

public class PortHandler implements INotificationHandler{
	
		public void onMessageReceived(String message) {
			Port portNumber = new Port(Integer.parseInt(message));
			ClientBoardState.portNumber = portNumber;
		}
}
