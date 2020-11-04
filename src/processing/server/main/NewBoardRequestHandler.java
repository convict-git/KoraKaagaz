package processing.server.main;

import networking.INotificationHandler;
import processing.utility.*;
import networking.CommunicatorFactory;

/**
 * This Handler implements INotificationHandler and handles
 * requests for new board.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class NewBoardRequestHandler implements INotificationHandler{

	public void onMessageReceived(String message) {
		
		String clientAddress = message;
		
		BoardId boardId = new BoardId(
				Integer.toString(ServerState.boardNumber)
		);
		
		ServerState.boardNumber++;
		
		Port boardServerPort = new Port(
				CommunicatorFactory.getClientInfo().getPort()
		);
		
		ServerState.boardToPort.put(boardId, boardServerPort);
		
		BoardRequestHandler.startBoardServer(boardServerPort, boardId, null);
		
		ServerState.send(
				clientAddress, 
				boardId.toString(), 
				"ProcessingBoardId"
		);
		
		ServerState.send(
				clientAddress, 
				boardServerPort.toString(), 
				"ProcessingServerPort"
		);
	}
	
}
