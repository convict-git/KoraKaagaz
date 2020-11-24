package processing.server.main;

import java.util.*;

import processing.utility.*;
import networking.ICommunicator;

/**
 * This class stores the globals that will be used on the Main Server.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class ServerState {

	/**
	 * boardToPort is map containing boardID of all the board servers currently running
	 * in a map from boardID to their respective port number on which they are listening.
	 */
	public static Map <BoardId, Port> boardToPort;
	
	/**
	 * boardNumber will store the board number to be given as the boardID to the new board
	 * that need to be started.
	 */
	public static int boardNumber = 0;

	// communicator will store the communicator that we receive from the networking module.
	public static ICommunicator communicator;
	
	//Main Server port number
	public static Port portNumber = new Port(8467);
	
	/**
	 * wrapper send function of the networking module send so as to spawn a thread
	 * and then call the send function of networking module inside that thread.
	 * 
	 * @param address address of the receiver
	 * @param message message to be sent
	 * @param identifier identifier of the message
	 */
	public static void send(String address, String message, String identifier) {
		
		// create a runnable object of the class HandleSend
		HandleSend runnable = new HandleSend(address,message,identifier);
		
		// spawn a new thread with runnable as the object
		Thread handleSend = new Thread(runnable);
		
		// start the process in the thread
		handleSend.start();
		
	}
}