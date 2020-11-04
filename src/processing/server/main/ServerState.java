package processing.server.main;

import java.util.*;

import processing.threading.HandleSend;
import processing.utility.*;
import networking.ICommunicator;

/**
 * This class stores the globals that will be used on the Main Server.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class ServerState {


	public static Map <BoardId, Port> boardToPort = new HashMap <BoardId, Port>();
	
	public static Map <BoardId, String> persistencePath = new HashMap <BoardId, String>();
	
	public static int boardNumber = 0;

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