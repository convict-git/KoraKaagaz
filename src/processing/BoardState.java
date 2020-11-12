package processing;

import java.util.*;
import java.io.Serializable;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.utility.*;
import processing.boardobject.*;

import static processing.ClientBoardState.logger;

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

	/**
	 * Constructor to initialize both the maps
	 */
	public BoardState() {
		objIdToBoardObject = new HashMap <ObjectId, BoardObject> ();
		posToObjects = new HashMap <Position, PriorityQueue <PriorityQueueObject>> ();
	}

	/**
	 * Looks up BoardObject for the ObjectId key in the argument
	 * Returns null if no board object is present as an object id key
	 *
	 * @param objId object Id as lookup key
	 */
	public synchronized BoardObject getBoardObjectFromId(ObjectId objId) {
		try {
			BoardObject object = objIdToBoardObject.get(objId);
			logger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() + "] getBoardObjectFromId: "
					+ "returning board object"
			);
			return object;
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] getBoardObjectFromId: "
							+ "could not lookup board object"
			);
			return null;
		}
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
	 * 		   null if an exception arises
	 */
	public synchronized ObjectId getMostProbableObjectId(ArrayList <Position> positions) {
		HashMap<ObjectId, Integer> count = new HashMap<ObjectId, Integer>();
		try {
			for (Position pos : positions) {
				PriorityQueue<PriorityQueueObject> queueAtPos = posToObjects.get(pos);
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
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO,
					"[#" + Thread.currentThread().getId() + "] getMostProbableObjectId: "
							+ "computed counts of objectIds occurring on top"
			);

		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() +
							"] getMostProbableObjectId: could not peek into top positions"
			);
			return null;
		}

		Integer maxOccurrence = 0;
		ObjectId maxOccObjectId = null;

		try {
			Iterator <Map.Entry<ObjectId, Integer>> it = count.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<ObjectId, Integer> mapElement = (Map.Entry<ObjectId, Integer>) it.next();
				if (maxOccurrence < mapElement.getValue()) {
					maxOccurrence = mapElement.getValue();
					maxOccObjectId = mapElement.getKey();
				}
			}
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() +
							"] getMostProbableObjectId: returning max occurring objectId"
			);

			return maxOccObjectId;
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() +
							"] getMostProbableObjectId: could not calculate max occurring objectId"
			);
			return null;
		}
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

		try {
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
					try {
						BoardObject topObject = getBoardObjectFromId(topPQobj.objectId);
						Pixel atThisPos = topObject.getPixels().get(0);
						// Assuming that the object is uni-coloured and should have pixels

						topPixels.add(new Pixel(p, atThisPos.intensity));
					}
					catch (Exception e) {
						logger.log(
								ModuleID.PROCESSING,
								LogLevel.WARNING,
								"[#" + Thread.currentThread().getId() + "] getPixelsAtTop: "
										+ "could not get pixels from the object"
						);
					}
				}
			}
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() + "] getPixelsAtTop: "
							+ "returning looked-up top pixels from input positions"
			);

			return topPixels;
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] getPixelsAtTop: "
							+ "could not lookup into maps or Priority queues"
			);
			return new ArrayList<>();
		}
	}

	/**
	 * For each Position (x, y) in the input ArrayList, insert the PriorityQueueObject into the
	 * Priority Queue at the position (x, y) present in the map
	 */
	private synchronized void insertIntoPQ (
		ArrayList <Position> positions,
		PriorityQueueObject toBeInserted
	) throws Exception {
		for (Position pos : positions) {
			try {
				PriorityQueue<PriorityQueueObject> pq = posToObjects.get(pos);
				if (pq == null) {
					posToObjects.put(
						pos,
						new PriorityQueue<PriorityQueueObject>(20, new ObjectComparator())
					);
				}
			}
			catch (Exception e) {
				logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] insertIntoPQ: "
						+ "could not lookup into maps or initialize priority queue"
				);
				throw new Exception();
			}

			try {
				posToObjects.get(pos).add(toBeInserted);
			}
			catch (Exception e) {
				logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] insertIntoPQ: "
						+ "could not insert into a priority queue"
				);
				throw new Exception();
			}
		}
		logger.log(
			ModuleID.PROCESSING,
			LogLevel.SUCCESS,
			"[#" + Thread.currentThread().getId() + "] insertIntoPQ: "
						+ "successful"
		);
	}

	/**
	 * For each Position (x, y) in the input ArrayList, delete the PriorityQueueObject from the
	 * Priority Queue at the position (x, y) present in the map, if it exists in the Priority Queue
	 */
	private synchronized void removeFromPQ (
			ArrayList <Position> positions,
			PriorityQueueObject objectToBeRemoved
	) throws Exception {
		try {
			for (Position pos : positions) {
				PriorityQueue<PriorityQueueObject> pq = posToObjects.get(pos);
				if (pq == null) {
					continue;
				}
				pq.remove(objectToBeRemoved);
				if (pq.isEmpty()) {
					posToObjects.remove(pos);
				}
			}
			logger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] removeFromPQ: "
					+ "removed the object from priority queues"
			);
		}
		catch (Exception e) {
			logger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] removeFromPQ: "
					+ "could not lookup from maps or remove from priority queue"
			);
			throw new Exception();
		}
	}

	/**
	 * Read the ObjectId field from the BoardObject and
	 * insert the (ObjectId, BoardObject) key, value pair in the map
	 */
	private synchronized void insertObjectIntoMap(BoardObject boardObject) throws Exception {
		try {
			ObjectId objId = boardObject.getObjectId();
			objIdToBoardObject.put(objId, boardObject);
			logger.log (
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] insertObjectIntoMap: "
					+ "inserted (objId, BoardObject) pair into the map"
			);
		}
		catch (Exception e) {
			logger.log (
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] insertObjectIntoMap: "
					+ "could not insert (objId, BoardObject) pair into the map"
			);
			throw new Exception();
		}
	}

	/**
	 * Remove the input ObjectId's BoardObject from the map
	 * Thus, the key, value pair (ObjectId, BoardObject) gets deleted from the map
	 * If the key does not exist or an exception is caught, null is returned
	 */
	private synchronized BoardObject removeObjectFromMap(ObjectId objId) throws Exception {
		try {
			BoardObject object = objIdToBoardObject.remove(objId);
			logger.log (
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMap: "
							+ "BoardObject with objId removed from the map"
			);
			return object;
		}
		catch (Exception e) {
			logger.log (
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMap: "
							+ "BoardObject with objId could not be removed from the map"
			);
			throw new Exception();
		}
	}

	/* APIs to Update maps to be used internally by other Processing Module classes */

	/**
	 * Reads the ObjectId, Timestamp, ArrayList<Position> fields of the input BoardObject
	 * calls insertIntoPQ and insertObjectIntoMap to update both the maps
	 *
	 * @param boardObject board object to be inserted into the maps
	 */
	public void insertObjectIntoMaps (BoardObject boardObject) {
		ObjectId objId;
		Timestamp timestamp;
		ArrayList<Position> positions;
		PriorityQueueObject pqObject;

		try {
			objId = boardObject.getObjectId();
			timestamp = boardObject.getTimestamp();
			positions = boardObject.getPositions();
			pqObject = new PriorityQueueObject(objId, timestamp);

			logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "got the board object's data members"
			);
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "could not get board object data members"
			);
			return;
		}
		try {
			insertObjectIntoMap(boardObject);
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "inserted the object into objectId -> BoardObject map"
			);
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "couldn't insert the object into objectId -> BoardObject map"
			);
			return;
		}
		try {
			insertIntoPQ(positions, pqObject);
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "inserted the board object into both the maps"
			);
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] insertObjectIntoMaps: "
							+ "couldn't insert the board object into Position to PriorityQueue map"
			);
		}
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
		BoardObject remove;
		try {
			remove = removeObjectFromMap(objectId);
			if (remove == null) {
				// object not present already
				return null;
			}
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMaps: "
							+ "removed the board object from objectId -> BoardObject map"
			);
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMaps: "
							+ "couldn't remove the board object from objectId -> BoardObject map"
			);
			return null;
		}

		try {
			Timestamp timestamp = remove.getTimestamp();
			ArrayList<Position> positions = remove.getPositions();

			// constructing the new Priority Queue object to be removed
			PriorityQueueObject pqRemoveObject = new PriorityQueueObject(objectId, timestamp);

			removeFromPQ(positions, pqRemoveObject);

			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMaps: "
							+ "returning the board object removed from both the maps"
			);

			return remove;
		}
		catch (Exception e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] removeObjectFromMaps: "
							+ "couldn't remove the BoardObject from Position -> PriorityQueue map"
			);
			return null;
		}
	}

	/**
	 * Looks up the BoardObject of the ObjectId using the map
	 * From the BoardObject, user name is returned as String
	 * Returns null if an exception is caught or if no object
	 * with the input objectId exists in the map
	 *
	 * @param objId objectId of a BoardObject
	 * @return username of the input object as a String
	 */
	public String getUserNameOfObject(ObjectId objId) {
		try {
			BoardObject boardObject = objIdToBoardObject.get(objId);
			if (boardObject == null) {
				logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO,
					"[#" + Thread.currentThread().getId() + "] getUserNameOfObject: "
						+ " no board object with this objectId present in the map"
				);
				return null;
			}

			String username = boardObject.getUserId().getUsername().toString();
			logger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] getUserNameOfObject: "
						+ " returning username corresponding to this objectId's board object"
			);

			return username;
		}
		catch (Exception e) {
			logger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] getUserNameOfObject: "
						+ " could not get username of this objectId's board object"
			);
			return null;
		}
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
