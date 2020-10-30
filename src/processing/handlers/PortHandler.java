package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;

/**
 * This class will handle the Port Request that the client made to
 * get the board server port number.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class PortHandler implements INotificationHandler {

	public void onMessageReceived(String message) {
		Port portNumber = new Port(Integer.parseInt(message));
		ClientBoardState.portNumber = portNumber;
	}
}
