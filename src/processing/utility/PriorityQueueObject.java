package processing.utility;

/**
* This class describes the object to be stored in the Priority Queue.
* It will store the obejct ID and the timestamp to sort the objects in the
* priority queue.
*
* @author Ahmed Zaheer Dadarkar, Shruti Umat
*/

public class PriorityQueueObject {
	
	public ObjectId objectId;
	public Timestamp timestamp;

	//constructor to set the object ID and the time
	public PriorityQueueObject(ObjectId objId, Timestamp time) {
		objectId = objId;
		timestamp = time;
	}

	//Copy constructor
	public PriorityQueueObject(PriorityQueueObject obj) {
		this.objectId = obj.objectId;
		this.timestamp = obj.timestamp;
	}
}
