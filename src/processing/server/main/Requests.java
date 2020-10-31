package processing.server.main;

import processing.ClientBoardState;
import processing.utility.*;

/**
 * This class implements IRequests Interface
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class Requests implements IRequests{
	
	String serverAddr;
	
	public Requests() {
		this.serverAddr = ClientBoardState.serverIp.toString() 
						+ ":" 
						+ ClientBoardState.serverPort.toString();
	}

	public void requestForNewBoard(IpAddress ipAddress, Port portNumber) {
				
		String message = ipAddress.toString()
						 + ":"
						 + portNumber.toString();
		
		ClientBoardState.communicator.send(serverAddr,message,"NewBoard");
		
	}
	
	public void requestForExistingBoard(BoardId boardId, IpAddress ipAddress, Port portNumber) {
		
		String message = boardId.toString()
					   + ":"
					   + ipAddress.toString()
					   + ":"
					   + portNumber.toString();
		
		ClientBoardState.communicator.send(serverAddr, message, "ExistingBoard");
		
	}
	
}