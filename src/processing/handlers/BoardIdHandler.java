package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;

public class BoardIdHandler implements INotificationHandler{

	public void onMessageReceived(String message) {
		BoardId boardId = new BoardId(message);
		ClientBoardState.boardId = boardId;
	}
	
}
