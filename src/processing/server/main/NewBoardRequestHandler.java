package processing.server.main;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import networking.CommunicatorFactory;
import org.json.*;

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
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "New Board Server Request on the Main Server"
		);
		
		JSONObject messageInJSON = new JSONObject(message);
		
		/**
		 * While making a new board request, the client will send their full
		 * address as the message in that request.
		 */
		String clientAddress = messageInJSON.getString("ClientAddress");
		
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
		Port boardServerPort = null;
		
		try {
			boardServerPort = new Port(
					CommunicatorFactory.getClientInfo().getPort()
			);
		} catch (Exception e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Error while getting free port from the network module"
			);
			
		}
		
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
		BoardRequestHandler.startBoardServer(boardServerPort, boardId, "NoPersistence");
		
		// waiting for the Board Server to start
		try {
			Thread.sleep(5000);
		} catch(Exception e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Error while sleep in the current thread"
			);
			
		}
		
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
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the BoardID to the client"
		);
		
		/**
		 * We also need to send the port number of the board back to the same client who
		 * made the new board request.
		 */
		ServerState.send(
				clientAddress, 
				Integer.toString(boardServerPort.port), 
				"ProcessingServerPort"
		);
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the Port number of the server to the client"
		);
	}
	
}
