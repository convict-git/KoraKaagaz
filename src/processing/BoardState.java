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

	/* To authenticate that the user deserializing this object */
	private static final long serialVersionUID = 1L;

	/* Map from object ID to the Board Object */
	private Map <ObjectId, BoardObject> objIdToBoardObject;

	/* Map from position on the board to Priority Queue of objects on that position */
	private Map <Position, PriorityQueue <PriorityQueueObject>> posToObjects;

	/* Constructor to initialize both the maps */
	public BoardState() {
		objIdToBoardObject = new HashMap <ObjectId, BoardObject> ();
		posToObjects = new HashMap <Position, PriorityQueue <PriorityQueueObject>> ();
	}

	/* Looks up BoardObject for the ObjectId key in the argument */
	public synchronized BoardObject getBoardObjectFromId(ObjectId objId) {
		return objIdToBoardObject.get(objId);
	}

	/**
	 * Gets ObjectIds at the top of Priority Queues at each position (x, y) in given ArrayList
	 * The ObjectId that is present at the maximum number of positions i.e. which occurs
	 * the maximum number of times is returned
	 * This is done as the selected pixel positions can be such that
	 * some of the pixels belong to an object and some to others
	 *
	 * @param positions list of positions on the board
	 * @return ObjectId present on top of maximum number of input positions
	 */
	public synchronized ObjectId getMostProbableObjectId(ArrayList <Position> positions) {
		HashMap<ObjectId, Integer> count = new HashMap<ObjectId, Integer>();
		for (Position pos: positions) {
			PriorityQueue <PriorityQueueObject> queueAtPos = posToObjects.get(pos);
			if (queueAtPos == null) {
				continue;
			}
			PriorityQueueObject topPQobj = (PriorityQueueObject) queueAtPos.peek();
			if (topPQobj == null) {
				continue;
			}
			// there should be a valid object for this ObjectId
			if (getBoardObjectFromId(topPQobj.objectId).isResetObject()) {
				continue;
			}

			Integer curr = count.get(topPQobj.objectId);
			if (curr == null) {
				curr = 0;
			}
			count.put(topPQobj.objectId, curr + 1);
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
	 * Gets pixels at the top of the input positions
	 *
	 * @param positions positions on the board
	 * @return pixels at these positions
	 */
	public synchronized ArrayList<Pixel> getPixelsAtTop (ArrayList<Position> positions) {
		ArrayList<Pixel> topPixels = new ArrayList<Pixel>();
		if (positions == null) {
			return topPixels;
		}

		for (Position p : positions) {
			PriorityQueue<PriorityQueueObject> queueAtPos = posToObjects.get(p);
			if (queueAtPos == null) {
				topPixels.add(new Pixel(p, new Intensity(255, 255, 255)));
				continue;
			}

			PriorityQueueObject topPQobj = queueAtPos.peek();
			if (topPQobj == null) {
				topPixels.add(new Pixel(p, new Intensity(255, 255, 255)));
			}
			else {
				BoardObject topObject = getBoardObjectFromId(topPQobj.objectId);
				Pixel atThisPos = topObject.getPixels().get(0);
				// Assuming that the object is uni-coloured and should have pixels

				topPixels.add(new Pixel(p, atThisPos.intensity));
			}
		}
		assert (positions.size() == topPixels.size());
		return topPixels;
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
	 *
	 * @param boardObject board object to be inserted into the maps
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
	 *
	 * @param objectId objectId to be removed from the maps
	 * @return a copy of the board object removed from the maps
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
	 * From the BoardObject, user name is returned as String
	 *
	 * @param objId objectId of a BoardObject
	 * @return username of the input object as a String
	 */
	public String getUserNameOfObject(ObjectId objId) {
		BoardObject boardObject = objIdToBoardObject.get(objId);
		if (boardObject == null) {
			return null;
		}
		return boardObject.getUserId().getUsername().toString();
	}
	
	/**
	 * Gives all the positions where there is something drawn on the board
	 * 
	 * @return List of all those positions
	 */
	public ArrayList<Position> getPositions() {
		
		ArrayList<Position> positions = new ArrayList<Position>();
		
		for (Map.Entry<Position, PriorityQueue <PriorityQueueObject>> entry : posToObjects.entrySet()) {
			
			positions.add(entry.getKey());
		}
		
		return positions;
	}

}
