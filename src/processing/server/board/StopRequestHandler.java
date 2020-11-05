package processing.server.board;

import java.io.IOException;
import processing.utility.*;
import networking.INotificationHandler;
import processing.*;
import processing.server.main.Requests;

/**
 * This class implements INotificationHandlerr and 
 * handles the stop request from the client when they
 * exit their application.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class StopRequestHandler implements INotificationHandler {
	
	public void onMessageReceived(String message) {
		
		ClientBoardState.users.remove(new Username(message));
		
		if(ClientBoardState.users.isEmpty()) {
			
			try {
				String persistence = Serialize.serialize(ClientBoardState.maps);
			} catch (IOException e) {
				// log the exception
			}
			
			// save the persistence file
			
			ClientBoardState.communicator.stop();
			Requests.removeBoardServer(ClientBoardState.boardId);
		}
	}
	
}
