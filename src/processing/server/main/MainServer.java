package processing.server.main;

import java.util.HashMap;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import networking.CommunicatorFactory;
import processing.ClientBoardState;
import processing.utility.BoardId;
import processing.utility.Port;

/**
 * Main class of main server.
 * This will start the main server to listen for Board Requests.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class MainServer {

	// main function
	public static void main(String[] args) {
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Starting the Main Server"
		);
		
		// initialise the boarToPort map 
		ServerState.boardToPort = new HashMap <BoardId, Port>();
		
		/**
		 * The port number of the Main Server is fixed and saved in ServerState
		 * get the communicator from the networking module using the same port 
		 * number and save the resultant communicator in the ServerState.
		 */
		ServerState.communicator = CommunicatorFactory.getCommunicator(
				ServerState.portNumber.port
		);
		
		/**
		 *  call the start function of the networking module to continuously listen
		 *  on the given port.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Starting communication on the Main Server"
		);
		
		ServerState.communicator.start();
		
		/**
		 * Now Client can make a new board request to the server, for that Main Server
		 * need to subscribe for this request to the networking module.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for new board request on the Main Server"
		);
		
		ServerState.communicator.subscribeForNotifications(
				"NewBoard",
				new NewBoardRequestHandler()
		);
		
		/**
		 * Client can also make existing board request so subscribing for existing board
		 * request, to the networking module.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for existing board request on the Main Server"
		);
		
		ServerState.communicator.subscribeForNotifications(
				"ExistingBoard", 
				new BoardRequestHandler()
		);
		
		/**
		 * Board Server will make Remove Board request when all the clients disconnected
		 * from it so it needs to be shutdown.
		 */
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for Remove Board event"
		);
		
		ServerState.communicator.subscribeForNotifications(
				"RemoveBoard", 
				new RemoveBoardHandler()
		);
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully started the Main Server"
		);
		
	}

}
