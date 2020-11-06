package processing.server.main;

import networking.INotificationHandler;
import processing.utility.*;
import networking.CommunicatorFactory;

/**
 * This Handler implements INotificationHandler and handles
 * requests for a new board made ny any new client.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class NewBoardRequestHandler implements INotificationHandler{

	/**
	 * Defining the onMessageReceived function of INotificationHandler which
	 * the networking module will call if they receive any message with 
	 * new board request identifier.
	 */
	public void onMessageReceived(String message) {
		
		/**
		 * While making a new board request, the client will send their full
		 * address as the message in that request.
		 */
		String clientAddress = message;
		
		/**
		 * Find the boardID using the current board number in the Server
		 * State.
		 */
		BoardId boardId = new BoardId(
				Integer.toString(ServerState.boardNumber)
		);
		
		// increment the boardNumber
		ServerState.boardNumber++;
		
		/**
		 * Get a new free port from the networking module where to start 
		 * this new board's server
		 */
		Port boardServerPort = new Port(
				CommunicatorFactory.getClientInfo().getPort()
		);
		
		/**
		 * Insert this board's port number information in the map boardToPort
		 * for future reference
		 */
		ServerState.boardToPort.put(boardId, boardServerPort);
		
		/**
		 * Call the startBoardServer function of BoardRequestHandler to start the board
		 * server with the given port number and the boardId. Here the persistent data
		 * is null as it's a new board so no earlier data existed.
		 */
		BoardRequestHandler.startBoardServer(boardServerPort, boardId, null);
		
		/**
		 * Send the boardID back to the client using the identifier ProcessingBoardID with
		 * the destination address as the address received while requested for new board i.e to 
		 * the client who made this request.
		 */
		ServerState.send(
				clientAddress, 
				boardId.toString(), 
				"ProcessingBoardId"
		);
		
		/**
		 * We also need to send the port number of the board back to the same client who
		 * made the new board request.
		 */
		ServerState.send(
				clientAddress, 
				boardServerPort.toString(), 
				"ProcessingServerPort"
		);
	}
	
}
