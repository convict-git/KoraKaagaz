package processing.server.main;

import java.io.Serializable;
import java.util.*;
import processing.utility.*;

/**
 * This class stores the globals that will be used on the Main Server.
 * 
 * @author Himanshu Jain
 *
 */

public class ServerState implements Serializable {

	/** To authenticate while deserializing */
	private static final long serialVersionUID = 1L;

	public static Map <BoardId, Port> boardToPort = new HashMap <BoardId, Port>();
	
	public static Map <BoardId, String> persistencePath = new HashMap <BoardId, String>();
	
}