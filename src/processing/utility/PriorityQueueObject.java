package processing.utility;

import java.io.Serializable;

/**
* This class describes the object to be stored in the Priority Queue.
* It will store the obejct ID and the timestamp to sort the objects in the
* priority queue.
*
* @author Himanshu Jain, Shruti Umat
*/

public class PriorityQueueObject implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = -2213699374085459395L;

	public ObjectId objectId;
	public Timestamp timestamp;

	/** constructor to set the object ID and the time */
	public PriorityQueueObject(ObjectId objId, Timestamp time) {
		objectId = objId;
		timestamp = time;
	}

	/** Copy constructor */
	public PriorityQueueObject(PriorityQueueObject obj) {
		this.objectId = obj.objectId;
		this.timestamp = obj.timestamp;
	}

	/** Overriding equals method for PriorityQueueObject */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PriorityQueueObject) {
			PriorityQueueObject p = (PriorityQueueObject)obj;
			return
				timestamp.equals(p.timestamp)
				&&
				objectId.equals(p.objectId);
		}
		else {
			return false;
		}
	}

	/** HashCode Method */
	@Override
	public int hashCode() {
		return 31 * objectId.hashCode() + timestamp.hashCode();
	}
}
