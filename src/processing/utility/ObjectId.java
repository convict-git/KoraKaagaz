package processing.utility;

/**
 * Class Representing the Board Object ID
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class ObjectId {
	/**
	 * Object ID as a String
	 */
	private String objectId;
	
	/**
	 * Object ID Constructor
	 * 
	 * Construct Object ID using a User ID and a Timestamp
	 * 
	 * @param userId The user ID of a user
	 * @param timestamp Time at which this object was built
	 */
	public ObjectId(UserId userId, Timestamp timestamp) {
		objectId = userId.toString() + "_" + timestamp.toString();
	}
	
	/** Copy Constructor */
	public ObjectId(ObjectId objectIdObject) {
		objectId = objectIdObject.objectId;
	}
	
	/**
	 * Converts to String
	 * 
	 * @return Object ID as a String
	 */
	public String toString() {
		return objectId;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectId)
			return 
				objectId == ((ObjectId)obj).objectId;
		else
			return false;
	}
}
