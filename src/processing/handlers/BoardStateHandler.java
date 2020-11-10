package processing.handlers;

import java.io.IOException;
import processing.threading.*;
import processing.Serialize;
import networking.INotificationHandler;
import processing.BoardState;
import processing.ClientBoardState;

/**
 * This class handles Board State received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class BoardStateHandler implements INotificationHandler {
	
	public static void handleBoardState(String message) {
		try {
			BoardState boardState = (BoardState)Serialize.deSerialize(message);
			ClientBoardState.maps = boardState;
		} catch (ClassNotFoundException e) {
			//Log the exception
		} catch (IOException e) {
			//Log the exception
		}
	}
	
	public void onMessageReceived(String message) {
		
		HandleBoardState runnable = new HandleBoardState(message);
		Thread boardState = new Thread(runnable);
		boardState.start();
		
	}
}
