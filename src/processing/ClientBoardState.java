package processing;

import java.util.*;
import processing.board_object.*;
import processing.utility.*;

/**
*
* @author Himanshu Jain
*/

public class ClientBoardState {
	
	/**
	 * maps is an object of the BoardState containing both the maps
	 */
	public static BoardState maps = new BoardState();
	
	//port Number of the Board Server
	public static int portNumber;
	
	//public static Map <Identifier identifier, IChanges handler>
	
	public static String BoardId;
	
	//undo and redo stacks
	public static ArrayList <BoardObject> unodStack = new ArrayList <BoardObject>();
	public static ArrayList <BoardObject> redoStack = new ArrayList <BoardObject>();
	
	//to store the selected object
	public static PriorityQueueObject selectedObject;
}
