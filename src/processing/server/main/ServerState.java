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


	public static Map <BoardId, Port> boardToPort = new HashMap <BoardId, Port>();
	
	public static Map <BoardId, String> persistencePath = new HashMap <BoardId, String>();
	
	public static int boardNumber = 0;

	public static ICommunicator communicator;
	
	//Main Server port number
	public static Port portNumber = new Port(8467);
}