package processing.server.board;

import java.io.IOException;

import networking.CommunicatorFactory;
import processing.*;
import processing.utility.*;

/**
 * This contains the main function for the Board Server 
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class BoardServer {

	public static void main(String[] args) {
		
		Port serverPort = new Port(Integer.parseInt(args[0]));
		
		ClientBoardState.communicator = CommunicatorFactory.getCommunicator(
				serverPort.port
		);
		
		ClientBoardState.communicator.start();
		ClientBoardState.serverPort = serverPort;
		ClientBoardState.boardId = new BoardId(args[1]);
		
		String persistence = args[2];
		if(persistence != null) {
			
			try {
				ClientBoardState.maps = (BoardState)Serialize.deSerialize(
						persistence
				);
			} catch (ClassNotFoundException e) {
				// log the exception
			} catch (IOException e) {
				// log the exception
			}
		}
		
		ClientBoardState.communicator.subscribeForNotifications(
				"BoardStateRequest", 
				new BoardStateRequestHandler()
		);
		
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast", 
				new ObjectBroadcastHandler()
		);
		
		ClientBoardState.communicator.subscribeForNotifications(
				"StopConnection", 
				new ObjectBroadcastHandler()
		);
	}

}
