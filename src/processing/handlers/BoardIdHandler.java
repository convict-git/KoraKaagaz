package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;

/**
 * This class handles Board ID received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class BoardIdHandler implements INotificationHandler {
	public void onMessageReceived (String message) {
		BoardId boardId = new BoardId(message);
		ClientBoardState.boardId = boardId;
	}
}
