package processing;

import java.util.*;
import java.io.Serializable;
import processing.utility.*;
import processing.board_object.*;

/**
* This BoardState class stores two maps which should be serializable for persistence.
* ClientBoardState and ServerBoardState will store the object of this class in their class.
*
* @author Himanshu Jain, Shruti Umat
*/

public class BoardState implements Serializable {

	// To authenticate that the user deserializing this object.
	private static final long serialVersionUID = 1L;

	// Map from object ID to the Board Object
	private Map <ObjectId, BoardObject> objIdToBoardObject;

	// Map from position on the board to Priority of objects on that position
	private Map <Position, PriorityQueue <PriorityQueueObject>> pixelToObjects;

	// Constructor to initialize both the maps
	public BoardState() {
		objIdToBoardObject = new HashMap <ObjectId, BoardObject> ();
		pixelToObjects = new HashMap <Position, PriorityQueue <PriorityQueueObject>> ();
	}
}
