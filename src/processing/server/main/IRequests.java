package processing.server.main;

import processing.utility.*;

/**
 * To handle new board requests on the main server
 * 
 * @author Himanshu Jain
 *
 */

public interface IRequests {

	/**
	 * This function will be called when new board request is to be made
	 * 
	 * @param ipAddress IP Address of the client requesting new Board
	 * @param portNumber Port Number of the client requesting new Board
	 * @return the port number where the new requested board's server will be running
	 */
	public void requestForNewBoard(IpAddress ipAddress, Port portNumber);
	
	/**
	 * This function will be called when existing board request is to be made.
	 * 
	 * @param boardId BoardID of the existing board
	 * @param ipAddress IP Address of the client requesting the Board
	 * @param portNumber Port Number of the client requesting the Board
	 * @return the port number where the new requested board's server will be running
	 */
	public void requestForExistingBoard(BoardId boardId, IpAddress ipAddress, Port portNumber);
}