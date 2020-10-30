package processing.handlers;

import java.io.IOException;

import networking.INotificationHandler;
import processing.*;

public class BoardStateHandler implements INotificationHandler{

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
