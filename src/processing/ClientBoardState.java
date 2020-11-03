package processing;

import java.util.*;
import processing.boardobject.*;
import processing.utility.*;

/**
* This class contains all the info of the current board.
*
* @author Himanshu Jain
*/

public class ClientBoardState {
	
	//maps is an object of the BoardState containing both the maps
	public static BoardState maps = new BoardState();
	
	//port Number of the Board Server
	public static Port portNumber;
	
	//public static Map <Identifier identifier, IChanges handler>
	
	public static BoardId boardId;
	
	//undo and redo stacks
	public static ArrayList <BoardObject> undoStack = new ArrayList <BoardObject>();
	public static ArrayList <BoardObject> redoStack = new ArrayList <BoardObject>();
	
	/**
	 * Will remain empty on the client side, as there is no need to know all the users to the client.
	 * Server will use this users List to maintain the list of all the users connected to this Board.
	 */
	public static ArrayList <UserId> users = new ArrayList <UserId>();
	
	//to store the selected object
	private static PriorityQueueObject selectedObject;
	
	//to store the username and userId
	public static Username username;
	public static UserId userId;
	public static IpAddress userIP;
	
	public static IpAddress serverIp;
	
	public static BrushRadius brushSize;

	/** Synchronized handles to get and set the 'selectedObject' data member in ClientBoardState class */
	public static synchronized void setSelectedObject(PriorityQueueObject pqObject) {
		ClientBoardState.selectedObject = new PriorityQueueObject(pqObject);
	}

	public static synchronized PriorityQueueObject getSelectedObject() {
		return ClientBoardState.selectedObject;
	}

}
