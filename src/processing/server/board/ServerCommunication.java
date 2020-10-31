package processing.server.board;

import processing.ClientBoardState;

import java.io.IOException;

import processing.*;
import processing.boardobject.*;

/**
 * This class implements IServerCommunication interface.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ServerCommunication {
	
	String serverAddress;
	
	public ServerCommunication() {
		serverAddress = ClientBoardState.serverIp.toString()
					  + ":"
					  + ClientBoardState.serverPort.toString();
	}

	public void getBoardState() {
		ClientBoardState.communicator.send(serverAddress, null, "BoardStateRequest");
	}
	
	public void sendObject(BoardObject obj) {
	
		String message = null;
		
		try {
			message = Serialize.serialize(obj);
		} catch (IOException e) {
			// log the exception
		}
		
		ClientBoardState.communicator.send(serverAddress, message, "ObjectBroadcast");
	}
	
	public void stopConnection() {
		String message = ClientBoardState.userIP.toString()
					   + ":"
					   + ClientBoardState.userPort.toString();
		
		ClientBoardState.communicator.send(serverAddress, message, "StopConnection");
	}
	
}
