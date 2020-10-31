package processing.server.board;

import java.io.IOException;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.Serialize;

/**
 * This class implements INotificationHandler and handles
 * Board State Requests from the clients.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class BoardStateRequestHandler implements INotificationHandler{
	
	public void onMessageReceived(String message) {
		
		String clientAddress = message;
		
		String boardState = null;
		
		try {
			boardState = Serialize.serialize(ClientBoardState.maps);
		} catch (IOException e) {
			// log the exception
		}
		
		ClientBoardState.users.add(clientAddress);
		
		ClientBoardState.communicator.send(
				clientAddress, 
				boardState, 
				"ProcessingBoardState"
		);
	}
	
}
