package processing.server.board;

import java.io.IOException;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.utility.*;
import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.Serialize;

/**
 * This class implements INotificationHandler and handles
 * Board State Requests from the clients. This handler will handle 
 * all the messages with identifier BoardStateRequest
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class BoardStateRequestHandler implements INotificationHandler{
	
	/**
	 * Implementing the onMessageReceived function defined in
	 * INotificationHandler interface.
	 * 
	 * @param message the message received from the networking module
	 */
	public void onMessageReceived(String message) {
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"BoardState request is made by the client"
		);
		
		String[] arrOfArguments = message.split(":", 2);
		
		Username username = new Username(arrOfArguments[0]);
		
		/**
		 * While requesting for BoardState the client passes it's address
		 * with port as the argument.
		 */
		IpAddress clientAddress = new IpAddress(arrOfArguments[1]);
		
		String boardState = null;
		
		/**
		 * To the send the current BoardState i.e ClientBoardState.maps as
		 * we need to first serialize it and then transfer it over the network.
		 */
		try {
			boardState = Serialize.serialize(ClientBoardState.maps);
		} catch (IOException e) {
			// log the exception
		}
		
		/**
		 * As the users will make the BoardState request in the start we need to save
		 * the user detail in the users list ClientBoardState.
		 */
		ClientBoardState.users.put(username,clientAddress);
		
		/**
		 * Finally send the serialized boardState using the identifier ProcessingBoardState
		 * to the address we received in the message.
		 */
		ClientBoardState.send(
				clientAddress.toString(), 
				boardState, 
				"ProcessingBoardState"
		);
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Successfully sent the Board State to the client"
		);
	}
	
}
