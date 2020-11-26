package processing.server.main;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.ClientBoardState;
import processing.utility.*;
import org.json.*;

/**
 * This class implements IRequests Interface which handles requests
 * for new and existing boards.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class Requests implements IRequests{
	
	// serverAddr will store the full address of the server
	String serverAddr;
	
	/**
	 * Constructor to find the Main server's full address and save it into
	 * serverAddr
	 */
	public Requests() {
		// full address is IPAddress:PortNumber
		this.serverAddr = ClientBoardState.serverIp.toString() 
						+ ":" 
						+ ClientBoardState.serverPort.toString();
	}

	/**
	 * requestForNewBoard function will handle the new board request, whenever
	 * the client will give boardID as null, processing module will call this
	 * to tell the Main Server to start a new board server.
	 * 
	 * @param ipAddress IP Address of the client making the request
	 * @param portNumber port number of the client making the request
	 */
	public void requestForNewBoard(IpAddress ipAddress, Port portNumber) {
				
		JSONObject message = new JSONObject();
		
		/**
		 * We need to pass the client's full address as the message while making
		 * a new board request thus calculating the full address using client's 
		 * IP Address and the port number.
		 */
		String fullAddress = ipAddress.toString()
							+ ":"
							+ portNumber.toString();

		message.put("ClientAddress", fullAddress);
		
		/**
		 * Send the message with identifier "NewBoard" to the serverAddr to
		 * notify it to start a new board server.
		 */
		ClientBoardState.send(serverAddr,message.toString(),"NewBoard");
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the request for new board from client to the server"
		);
		
	}
	
	/**
	 * requestForExistingBoard will handle the existing board request, whenever the client
	 * will give a boardID which is not null, the processing module will call this function
	 * to tell the Main serve to start a board Server for this boardID.
	 * 
	 * @param boardID boardID of the existing board
	 * @param ipAddress IP Address of the client
	 * @param portNumber port number of the client
	 */
	public void requestForExistingBoard(BoardId boardId, IpAddress ipAddress, Port portNumber) {
		
		JSONObject message = new JSONObject();
		
		/**
		 * While requesting for existing board we need to give boardID and the client's full
		 * address as the message thus calculating that and combining with delimiter ":"
		 */
		String fullAddress = ipAddress.toString()
							+ ":"
							+ portNumber.toString();
		
		message.put("BoardID", boardId.toString());
		message.put("ClientAddress", fullAddress);
		
		/**
		 * Send the message with identifier "ExistingBoard" to the serverAddr to notify it to start
		 * a server for this board.
		 */
		ClientBoardState.send(serverAddr, message.toString(), "ExistingBoard");
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the request for existing board from client to the server"
		);
		
	}
	
	/**
	 * This function will be called by a board server when every client gets disconnected from the 
	 * server to stop the board server.
	 * @param boardId boardID of the board server which needs to be stopped
	 */
	public static void removeBoardServer(BoardId boardId) {
		ServerState.boardToPort.remove(boardId);
	}
	
}