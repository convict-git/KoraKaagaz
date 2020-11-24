package processing.server.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import processing.boardobject.*;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import networking.CommunicatorFactory;
import processing.*;
import infrastructure.content.ContentFactory;
import processing.utility.*;

/**
 * This contains the main function for the Board Server. Jar file for 
 * the Board Server will be made using this as the main starting file and 
 * this jar file will be used while starting a new Board server. So this file 
 * will be the first one to start when a Board Server will start.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class BoardServer {

	public static void main(String[] args) {
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Starting a new Board Server"
		);
		
		// initialising all the variables connected to this board server
		ClientBoardState.users = new HashMap<UserId, IpAddress>();
		ClientBoardState.maps = new BoardState();
		ClientBoardState.redoStack = new ArrayList<BoardObject>();
		ClientBoardState.undoStack = new ArrayList<BoardObject>();
		
		/**
		 * While starting a new Board Server the port on which it should start 
		 * the communicator is given as the first argument, so we are first 
		 * extracting that.
		 */
		Port serverPort = new Port(Integer.parseInt(args[0]));
		
		/**
		 * Get a new communicator from the networking module giving the same port
		 * number received as the argument.
		 */
		ClientBoardState.communicator = CommunicatorFactory.getCommunicator(
				serverPort.port
		);
		
		/**
		 * Start the communicator by calling the start function of the communicator
		 * given above. It will now continuously listen on the port given as the input
		 * while creating the communicator.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Starting the communication on the Board Server"
		);
		
		ClientBoardState.communicator.start();
		
		// set the Board Server's port as the port on which it is listening
		ClientBoardState.serverPort = serverPort;
		
		//set the user IP using the networking module getIP() function
		try {
			ClientBoardState.userIP = new IpAddress(CommunicatorFactory.getClientInfo().getIp());
		} catch (Exception e) {
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Error while getting IP Address from network module in the BoardServer"
			);
		}
		
		/**
		 * While starting a new Board Server, the Board ID is given as the second argument
		 * so extract it and save it in the Client Board State.
		 */
		ClientBoardState.boardId = new BoardId(args[1]);
		
		/**
		 * If the board existed before then it will have some persistence data which needs to
		 * be loaded again, so while starting the new Board Server the persistence file is
		 * passed as the third argument.
		 */
		String persistence = args[2];
		
		/**
		 * If the board never existed before i.e the board is a new board then the persistence 
		 * will be null, so we need not to do anything.
		 */
		if(persistence != null) {
			
			try {
				/**
				 * Deserialize the persistence string and cast it the BoardState
				 * and save this old persistence state in the maps of ClientBoardState.
				 */
				ClientBoardState.maps = (BoardState)Serialize.deSerialize(
						persistence
				);
			} catch (ClassNotFoundException e) {
				
				ClientBoardState.logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "BoardState class not found"
				);
				
			} catch (IOException e) {
				
				ClientBoardState.logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "IO Exception occured while deserializing BoardState"
				);
				
			}
		}
		
		/**
		 * As soon as the client receives the port on which their requested Board's server
		 * is running they will make a request to the server asking for the previous 
		 * BoardState, so the server need to subscribe for thus request using the same 
		 * identifier the client is using to send BoardState Request.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Board Server subscribing for BoardState Request"
		);
		
		ClientBoardState.communicator.subscribeForNotifications(
				"BoardStateRequest", 
				new BoardStateRequestHandler()
		);
		
		/**
		 * As any client will make change on the board, it will pass it as the BoardObject
		 * to the server to broadcast it to the other clients, so the server need to subscribe
		 * for this request using the same identifier as the client is using to send the
		 * BoardObject.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Board Server subscribing for receiving BoardObject for broadcasting it"
		);
		
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast", 
				new ObjectBroadcastHandler()
		);
		
		/**
		 * When the client want to stop the connection it needs to tell the server
		 * so the client will send a message with identifier StopConnection and message as
		 * their userID, so the server need to subscribe for this message to receive.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Board Server subscribing for Stop Connection Request from client"
		);
		
		ClientBoardState.communicator.subscribeForNotifications(
				"StopConnection", 
				new StopRequestHandler()
		);
		
		// start the content module server
		ContentFactory.startContentServer();
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully started new Board Server"
		);
	}

}
