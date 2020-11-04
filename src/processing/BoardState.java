package processing;

import java.util.*;
import java.io.Serializable;
import processing.utility.*;
import processing.boardobject.*;

/**
* This BoardState class stores two maps which should be serializable for persistence.
* ClientBoardState and ServerBoardState will store the object of this class in their class.
*
* @author Himanshu Jain, Shruti Umat
* @reviewer Ahmed Zaheer Dadarkar, Satchit Desai
*/

public class BoardState implements Serializable {

	/** To authenticate that the user deserializing this object */
	private static final long serialVersionUID = 1L;

	/** Map from object ID to the Board Object */
	private Map <ObjectId, BoardObject> objIdToBoardObject;

	/** Map from position on the board to Priority Queue of objects on that position */
	private Map <Position, PriorityQueue <PriorityQueueObject>> posToObjects;

	/** Constructor to initialize both the maps */
	public BoardState() {
		objIdToBoardObject = new HashMap <ObjectId, BoardObject> ();
		posToObjects = new HashMap <Position, PriorityQueue <PriorityQueueObject>> ();
	}

	/** Looks up BoardObject for the ObjectId key in the argument */
	public synchronized BoardObject getBoardObjectFromId(ObjectId objId) {
		return objIdToBoardObject.get(objId);
	}

	/**
	 * Gets ObjectIds at the top of Priority Queues at each position (x, y) in given ArrayList.
	 * The ObjectId that is present at the maximum number of positions i.e. which occurs
	 * the maximum number of times is returned
	 * This is done as the selected pixel positions can be such that
	 * some of the pixels belong to an object and some to others.
	 */
	public synchronized ObjectId getMostProbableObjectId(ArrayList <Position> positions) {
		HashMap<ObjectId, Integer> count = new HashMap<ObjectId, Integer>();
		for (Position pos: positions) {
			PriorityQueue <PriorityQueueObject> queue_at_pos = posToObjects.get(pos);
			if (queue_at_pos == null) {
				continue;
			}
			PriorityQueueObject top_obj = (PriorityQueueObject) queue_at_pos.peek();
			if (top_obj == null) {
				continue;
			}

			Integer curr = count.get(top_obj.objectId);
			if (curr == null) {
				curr = 0;
			}
			count.put(top_obj.objectId, curr + 1);
		}

		Integer maxOccurrence = 0;
		ObjectId maxOccObjectId = null;
		Iterator <Map.Entry<ObjectId, Integer>> it = count.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry <ObjectId, Integer> mapElement = (Map.Entry <ObjectId, Integer>) it.next();
			if (maxOccurrence < mapElement.getValue()) {
				maxOccurrence = mapElement.getValue();
				maxOccObjectId = mapElement.getKey();
			}
		}

		return maxOccObjectId;
	}

	/**
	 * For each Position (x, y) in the input ArrayList, insert the PriorityQueueObject into the
	 * Priority Queue at the position (x, y) present in the map
	 */
	private synchronized void insertIntoPQ (
			ArrayList <Position> positions, 
			PriorityQueueObject toBeInserted
	) {
		
		for (Position pos : positions) {
			PriorityQueue <PriorityQueueObject> pq = posToObjects.get(pos);
			if (pq == null) {
				posToObjects.put(
						pos, 
						new PriorityQueue<PriorityQueueObject>(20, new ObjectComparator())
				);
			}
			posToObjects.get(pos).add(toBeInserted);
		}
	}

	/**
	 * For each Position (x, y) in the input ArrayList, delete the PriorityQueueObject from the
	 * Priority Queue at the position (x, y) present in the map, if it exists in the Priority Queue
	 */
	private synchronized void removeFromPQ (
			ArrayList <Position> positions, 
			PriorityQueueObject objectToBeRemoved
	) {
		for (Position pos : positions) {
			PriorityQueue <PriorityQueueObject> pq = posToObjects.get(pos);
			if (pq == null) {
				continue;
			}
			pq.remove(objectToBeRemoved);
			if(pq.isEmpty()) {
				posToObjects.remove(pos);
			}
		}
	}

	/**
	 * Read the ObjectId field from the BoardObject and
	 * insert the (ObjectId, BoardObject) key, value pair in the map
	 */
	private synchronized void insertObjectIntoMap(BoardObject boardObject) {
		ObjectId objId = boardObject.getObjectId();
		objIdToBoardObject.put(objId, boardObject);
	}

	/**
	 * Remove the input ObjectId's BoardObject from the map
	 * Thus, the key, value pair (ObjectId, BoardObject) gets deleted from the map
	 */
	private synchronized BoardObject removeObjectFromMap(ObjectId objId) {
		return objIdToBoardObject.remove(objId);
	}


	/* APIs to Update maps to be used internally by other Processing Module classes */

	/**
	 * Reads the ObjectId, Timestamp, ArrayList<Position> fields of the input BoardObject
	 * calls insertIntoPQ and insertObjectIntoMap to update both the maps
	 */
	public void insertObjectIntoMaps (BoardObject boardObject) {
		ObjectId objId = boardObject.getObjectId();
		Timestamp timestamp = boardObject.getTimestamp();

		ArrayList<Position> positions = boardObject.getPositions();

		PriorityQueueObject pqObject = new PriorityQueueObject(objId, timestamp);

		insertObjectIntoMap(boardObject);
		insertIntoPQ(positions, pqObject);
	}

	/**
	 * Performs a lookup in the ObjectId to BoardObject map to obtain the BoardObject
	 * corresponding to this ObjectId
	 * Reads the ArrayList <Position> and Timestamp fields from the BoardObject and calls removeFromPQ
	 * Lastly, it calls removeObjectFromMap with the given ObjectId
	 * The deleted BoardObject is passed for serialization if applicable
	 * i.e. only if this was a local delete operation and needs to be sent to other clients via the server
	 */
	public BoardObject removeObjectFromMaps (ObjectId objectId) {
		BoardObject remove = removeObjectFromMap(objectId);
		if (remove == null) {
			return null;			// object not present already
		}

		Timestamp timestamp = remove.getTimestamp();
		
		ArrayList<Position> positions = remove.getPositions();

		PriorityQueueObject pqRemoveObject = new PriorityQueueObject(objectId, timestamp);

		removeFromPQ(positions, pqRemoveObject);
		return remove;
	}

	/**
	 * Looks up the BoardObject of the ObjectId using the map
	 * From the BoardObject, UserId field is read
	 * From UserId, username is extracted (after the '_')
	 */
	public String getUserNameOfObject(ObjectId objId) {
		BoardObject boardObject = objIdToBoardObject.get(objId);
		if (boardObject == null) {
			return null;
		}
		return boardObject.getUserId().getUsername().toString();
	}

}
