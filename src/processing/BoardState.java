package processing;

import java.util.*;
import java.io.Serializable;
import processing.utility.*;
import processing.board_object.*;

/**
*
* @author Himanshu Jain
*/

public class BoardState implements Serializable {

	/**
	 *  To authenticate that the user deserializing this object.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * Map from object ID to the Board Object
	 */
	private Map <ObjectId, BoardObject> objIdToBoardObject;
	
	/**
	 *  Map from position on the board to Priority of objects on that position
	 */
	private Map <Position, PriorityQueue <PriorityQueueObject>> pixelToObjects;
	
	//constructor to initialize both the maps
	public BoardState() {
		objIdToBoardObject = new HashMap <ObjectId, BoardObject> ();
		pixelToObjects = new HashMap <Position, PriorityQueue <PriorityQueueObject>> ();
	}
	
	//get the Board Object given objectId
	public BoardObject getObject(ObjectId objectId) {
		if(objIdToBoardObject.containsKey(objectId)) {
			return objIdToBoardObject.get(objectId);
		} else {
			return null;
		}
	}
	
	//get the priority queue given the position
	public PriorityQueue<PriorityQueueObject> getObject(Position pos) {
		if(pixelToObjects.containsKey(pos)) {
			return pixelToObjects.get(pos);
		} else {
			return null;
		}
	}
	
	//insert a Board Object with corresponding objectId
	public void insertObject(ObjectId objectId, BoardObject obj) {
		objIdToBoardObject.put(objectId,  obj);
	}
	
	//insert an object in the priority queue
	public void insertObject(Position pos, PriorityQueueObject obj) {
		if(pixelToObjects.containsKey(pos)) {
			pixelToObjects.get(pos).add(obj);
		} else {
			PriorityQueue <PriorityQueueObject> pq = new PriorityQueue <PriorityQueueObject> (20, new ObjectComparator());
			pq.add(obj);
			pixelToObjects.put(pos, pq);
		}
	}
	
	//delete an object from the objIdToBoardObject map
	public void deleteObject(ObjectId objectId) {
		if(objIdToBoardObject.containsKey(objectId)) {
			objIdToBoardObject.remove(objectId);
		}
		return;
	}
	
	//delete an object from the priority queue
	public void deleteObject(Position pos, PriorityQueueObject obj) {
		if(pixelToObjects.containsKey(pos)) {
			pixelToObjects.get(pos).remove(obj);
		}
	}
}
