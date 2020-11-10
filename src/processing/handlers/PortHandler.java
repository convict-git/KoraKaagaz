package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;
/**
 * This class handles Port number of the Board Server received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class PortHandler implements INotificationHandler{
	
		public void onMessageReceived(String message) {
			Port portNumber = new Port(Integer.parseInt(message));
			ClientBoardState.portNumber = portNumber;
		}
}
