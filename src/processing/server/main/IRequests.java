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
	 * @return the port number where the new requested board's server will be running
	 */
	public Port requestForNewBoard();
	
	/**
	 * This function will be called when existing board request is to be made.
	 * 
	 * @param boardId BoardID of the existing board
	 * @return the port number where the new requested board's server will be running
	 */
	public Port requestForExistingBoard(BoardId boardId);
	
	/**
	 * This will return the port number where given board's server is running
	 * 
	 * @param boardId BoardID whose port number need to know
	 * @return Port number where given board's server is running
	 * 		   null if the given board's server is not running
	 */
	public Port getPortNumber(BoardId boardId);
}