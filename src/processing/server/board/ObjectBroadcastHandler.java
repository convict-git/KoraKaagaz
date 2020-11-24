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
				"[#" + Thread.currentThread().getId() + "] "
				+ "Received Board Object at the server for broadcasting"
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
					"[#" + Thread.currentThread().getId() + "] "
					+ "BoardState class not found while deserializing BoardState"
					+ " in Board State"
			);
			
		} catch (IOException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "IO Exception occured while deserializing BoardState"
			);
			
		}
		
		/**
		 * Handle all the processing related to the board object by calling the
		 * static function of ObjectHandler
		 */
		try {
			ObjectHandler.handleBoardObject(message);
		} catch (Exception e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Error while handling received object on the Board Server"
			);
			
		}
		
		// get the username of the user who created the object
		UserId objectUser = obj.getUserId();
		
		/**
		 * Send the same message to all the clients except for the client who sent it
		 * for broadcasting.
		 */
		for(Map.Entry<UserId, IpAddress> user : ClientBoardState.users.entrySet()) {
			
			// check if the user is not the same who sent the object
			if(!user.getKey().equals(objectUser)) {
				ClientBoardState.send(user.getValue().toString(), message, "ProcessingObject");
			}
			
		}
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Successfully sent the received object to all the other clients"
		);
	}
	
}
