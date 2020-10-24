package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar, Shruti Umat
*/

public class PriorityQueueObject {
	
	public ObjectId objectId;
	public Timestamp timestamp;

	public PriorityQueueObject(ObjectId objId, Timestamp time) {
		objectId = objId;
		timestamp = time;
	}

	public PriorityQueueObject(PriorityQueueObject obj) {
		this.objectId = obj.objectId;
		this.timestamp = obj.timestamp;
	}
}
