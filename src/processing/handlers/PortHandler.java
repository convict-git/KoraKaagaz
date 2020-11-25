package processing.handlers;

import networking.INotificationHandler;
import infrastructure.content.*;
import processing.ClientBoardState;
import processing.server.board.IServerCommunication;
import processing.server.board.ServerCommunication;
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
			
			// sending the server port to the content module on the client side
			ContentFactory.getServerPort().setPort(portNumber.port);
			
			IServerCommunication communicator = new ServerCommunication();
			communicator.getBoardState();
		}
}
