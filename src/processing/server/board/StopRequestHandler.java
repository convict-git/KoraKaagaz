package processing.server.board;

import java.io.IOException;
import processing.server.main.*;
import java.io.UnsupportedEncodingException;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.utility.*;
import networking.INotificationHandler;
import processing.*;
import org.json.*;

/**
 * This class implements INotificationHandlerr and 
 * handles the stop request from the client when they
 * exit their application.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class StopRequestHandler implements INotificationHandler {
	
	/**
	 * onMessageRecived function of INotificationHandler which will be called
	 * by the networking module whenever any message they have to send here with
	 * this particular identifier to stop the connection from a particular client. 
	 */
	public void onMessageReceived(String message) {
		
		ClientBoardState.logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Received stop connection request from a client on the server"
		);
		
		JSONObject messageInJSON = new JSONObject(message);
		String userID = messageInJSON.getString("UserID");
		
		/**
		 * As the client is closing it's application we need to remove it from 
		 * the list of all the clients connected to this board.
		 */
		String[] id = userID.split("_", 2);
		IpAddress ipAddress = new IpAddress(id[0]);
		Username username = new Username(id[1]);
		ClientBoardState.users.remove(new UserId(ipAddress,username));
		
		/**
		 * If after leaving this client user list becomes empty i.e no client is connected
		 * to this board server, we need to close this server and inform the main server as
		 * well.
		 */
		if(ClientBoardState.users.isEmpty()) {
			
			String persistence = null;
			
			/**
			 * As this Board Server is going to shut, we need to save the data of this server
			 * for persistence, thus serializing the maps in ClientBoardState
			 */
			try {
				persistence = Serialize.serialize(ClientBoardState.maps);
			} catch (IOException e) {
				
				ClientBoardState.logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "IO Exception occured during serializing BoardState"
				);
				
			}
			
			/**
			 * Saving the serialized the data in the file with name same as the BoardID.
			 */
			try {
				PersistanceSupport.storeStateString(persistence, ClientBoardState.boardId);
			} catch (UnsupportedEncodingException e) {
				
				ClientBoardState.logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "UnsupportedEncodingException occured while saving the persistence state"
				);
				
			} catch (IOException e) {
				
				ClientBoardState.logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "IO Exception occured during saving the persistence state"
				);
				
			}
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.SUCCESS, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Successfully saved the persistence file in the local machine"
			);
			
			/**
			 * Notifying the main server to shut this board's server by passing board ID as
			 * the argument.
			 */
			String mainServerAddress = ClientBoardState.userIP.toString()
									 + ":"
									 + Integer.toString(ServerState.portNumber.port);
			
			ClientBoardState.send(
					mainServerAddress, 
					ClientBoardState.boardId.toString(), 
					"RemoveBoard"
			);
			
			/**
			 * Notifying the networking module to stop listening for this server
			 * messages by calling their stop function.
			 */
			ClientBoardState.communicator.stop();
			
			System.exit(0);
		}
	}
	
}
