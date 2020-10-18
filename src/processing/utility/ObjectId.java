package processing.utility;

import java.security.Timestamp;

public class ObjectId {
	private String objectId;
	
	// Create Object ID using provided User ID and 
	// Time stamp
	public ObjectId(UserId userId, Timestamp timestamp) {
		objectId = userId.toString() + "_" + timestamp.toString();
	}
	
	// Convert object ID to String
	public String toString() {
		return objectId;
	}
}
