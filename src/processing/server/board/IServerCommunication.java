package processing.server.board;

import processing.boardobject.*;

/**
 * This interface provides function to communicate with
 * the Board Server.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public interface IServerCommunication {

	/** This function will send the Board State to the client */
	public void getBoardState();
	
	/** This funciton will be used to send the object for
	 *  the server for broadcasting it.
	 *  
	 *  @param obj BoardObject to be sent on the Board Server
	 */
	public void sendObject(BoardObject obj);
	
	/** To send the stop connection request on the server */
	public void stopConnection();
	
}
