package processing.utility;

import java.security.Timestamp;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Object ID
public class ObjectId {
	private String objectId; // Object ID String
	
	// Create Object ID using provided User ID and 
	// Time stamp
	public ObjectId(UserId userId, Timestamp timestamp) {
		objectId = userId.toString() + "_" + timestamp.toString();
	}
	
	// Convert object ID to String
	public String toString() {
		return objectId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectId)
			return 
				objectId == ((ObjectId)obj).objectId;
		else
			return false;
	}
}
