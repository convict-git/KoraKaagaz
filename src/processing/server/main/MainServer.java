package processing.server.main;

import networking.CommunicatorFactory;

/**
 * Main class of main server.
 * This will start the main server
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class MainServer {

	public static void main(String[] args) {
		
		ServerState.communicator = CommunicatorFactory.getCommunicator(
				ServerState.portNumber.port
		);
		
		ServerState.communicator.start();
		
		ServerState.communicator.subscribeForNotifications(
				"NewBoard",
				new NewBoardRequestHandler()
		);
		
		ServerState.communicator.subscribeForNotifications(
				"ExistingBoard", 
				new BoardRequestHandler()
		);
	}

}
