package processing.server.board;

import processing.boardobject.*;

/**
 * This interface provides function to communicate with
 * the Board Server once the client gets it's port number.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public interface IServerCommunication {

	/**
	 * As soon as the client gets the port number on which the Board
	 * server is listening it will use this function to get the BoardState.
	 */
	public void getBoardState();
	
	/** This function will be used to send the object to
	 *  the server for broadcasting it.
	 *  
	 *  @param obj BoardObject to be sent on the Board Server
	 */
	public void sendObject(BoardObject obj);
	
	/** To send the stop connection request on the server */
	public void stopConnection();
	
}
