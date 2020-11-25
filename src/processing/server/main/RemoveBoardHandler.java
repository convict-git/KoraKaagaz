package processing.server.main;

import networking.INotificationHandler;
import processing.utility.*;

/**
 * This class handles the Remove Board Request from any Board Server when
 * they needs to be shutdown. It implements INotificaitonHandler
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class RemoveBoardHandler implements INotificationHandler{
	
	public void onMessageReceived(String message) {
		
		/**
		 * As the Board Server with the given board ID is no longer active
		 * we need to remove it from the map containing list of currently 
		 * running boards.
		 */
		BoardId boardId = new BoardId(message);
		ServerState.boardToPort.remove(boardId);
		
	}

}
