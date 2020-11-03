package processing.handlers;

import java.io.IOException;
import networking.INotificationHandler;
import processing.BoardState;
import processing.ClientBoardState;
import processing.Serialize;

/**
 * This class handles Board State message received from the server.
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class BoardStateHandler implements INotificationHandler {
	public void onMessageReceived(String message) {
		
		try {
				BoardState boardState = (BoardState)Serialize.deSerialize(message);
				ClientBoardState.maps = boardState;
		} catch (ClassNotFoundException e) {
			//Log the exception
		} catch (IOException e) {
			//Log the exception
		}
	}
}
