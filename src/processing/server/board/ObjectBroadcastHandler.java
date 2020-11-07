package processing.server.board;

import java.io.IOException;
import java.util.Map;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.Serialize;
import processing.boardobject.*;
import processing.handlers.ObjectHandler;
import processing.utility.*;

/**
 * This class implements INotificationHandler to handle
 * the event when client will send an object to broadcast.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ObjectBroadcastHandler implements INotificationHandler {

	public void onMessageReceived (String message) {
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"Received Board Object at the server for broadcasting"
		);
		
		BoardObject obj = null;
		
		/**
		 * DeSerialize the received message and cast it to the BoardObject
		 * class as the message is the serialized form of the BoardObject.
		 */
		try {
			obj = (BoardObject)Serialize.deSerialize(message);
		} catch (ClassNotFoundException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"BoardState class not found"
			);
			
		} catch (IOException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"IO Exception occured while deserializing BoardState"
			);
			
		}
		
		/**
		 * Handle all the processing related to the board object by calling the
		 * static function of ObjectHandler
		 */
		ObjectHandler.handleBoardObject(message);
		
		// get the username of the user who created the object
		Username objectUser = obj.getUserId().getUsername();
		
		/**
		 * Send the same message to all the clients except for the client who sent it
		 * for broadcasting.
		 */
		for(Map.Entry<Username, IpAddress> user : ClientBoardState.users.entrySet()) {
			
			// check if the user is not the same who sent the object
			if(!user.getKey().equals(objectUser)) {
				ClientBoardState.send(user.getValue().toString(), message, "ProcessingObject");
			}
			
		}
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Successfully sent the received object to all the other clients"
		);
	}
	
}
