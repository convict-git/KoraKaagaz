package processing.server.main;

import networking.INotificationHandler;
import processing.utility.*;
import java.io.*;
import networking.CommunicatorFactory;

/**
 * This handler implements INotificationHandler and handles
 * existing board requests
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class BoardRequestHandler implements INotificationHandler{

	public void startBoardServer(Port port) {
		
		ProcessBuilder processbuilder = new ProcessBuilder();
		processbuilder.command("java", "-jar", "BoardServer.jar", port.toString());
		
		try {
			
			processbuilder.start();
			
		} catch(IOException e) {
			//handle exception
		}
	}
	
	public void onMessageReceived(String message) {
		
		String[] arguments = message.split(":", 2);
		BoardId boardId = new BoardId(arguments[0]);
		
		String clientAddress = arguments[1];
		
		Port boardServerPort;
		
		if(ServerState.boardToPort.containsKey(boardId)) {
			boardServerPort = ServerState.boardToPort.get(boardId);
		} else {
			boardServerPort = new Port(CommunicatorFactory.getClientInfo().getPort());
			startBoardServer(boardServerPort);
		}
		
		
		ServerState.communicator.send(clientAddress, boardServerPort.toString(), "ProcessingServerPort");
	}
	
}
