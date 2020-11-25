package processing.server.board;

import java.io.IOException;
import org.json.*;
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
				"[#" + Thread.currentThread().getId() + "] "
				+ "BoardState request is made by the client"
		);
		
		JSONObject messageInJSON = new JSONObject(message);
		
		// extract userID from the first part of the message
		String user = messageInJSON.getString("UserID");
		String[] id = user.split("_", 2);
		UserId userId = new UserId(new IpAddress(id[0]), new Username(id[1]));
		
		/**
		 * While requesting for BoardState the client passes it's address
		 * with port as the argument.
		 */
		IpAddress clientAddress = new IpAddress(messageInJSON.getString("ClientAddress"));
		
		String boardState = null;
		
		/**
		 * To the send the current BoardState i.e ClientBoardState.maps as
		 * we need to first serialize it and then transfer it over the network.
		 */
		try {
			boardState = Serialize.serialize(ClientBoardState.maps);
		} catch (IOException e) {
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "IOException occured while serializing BoardState on BoardServer"
			);
		}
		
		/**
		 * As the users will make the BoardState request in the start we need to save
		 * the user detail in the users list ClientBoardState.
		 */
		ClientBoardState.users.put(userId,clientAddress);
		
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
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the Board State to the client"
		);
	}
	
}
