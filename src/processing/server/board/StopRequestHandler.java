package processing.server.board;

import java.io.IOException;
import processing.utility.*;
import networking.INotificationHandler;
import processing.*;
import processing.server.main.Requests;

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
		
		/**
		 * As the client is closing it's application we need to remove it from 
		 * the list of all the clients connected to this board.
		 */
		ClientBoardState.users.remove(new Username(message));
		
		/**
		 * If after leaving this client user list becomes empty i.e no client is connected
		 * to this board server, we need to close this server and inform the main server as
		 * well.
		 */
		if(ClientBoardState.users.isEmpty()) {
			
			/**
			 * As this Board Server is going to shut, we need to save the data of this server
			 * for persistence, thus serializing the maps in ClientBoardState
			 */
			try {
				String persistence = Serialize.serialize(ClientBoardState.maps);
			} catch (IOException e) {
				// log the exception
			}
			
			/**
			 * Saving the serialized the data in the file with name same as the BoardID.
			 */
			// save the persistence file
			
			/**
			 * Notifying the networking module to stop listening for this server
			 * messages by calling their stop function.
			 */
			ClientBoardState.communicator.stop();
			
			/**
			 * Notifying the main server to shut this board's server by passing board ID as
			 * the argument.
			 */
			Requests.removeBoardServer(ClientBoardState.boardId);
		}
	}
	
}
