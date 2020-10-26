package processing.utility;

/**
 * Class Representing a user's User ID
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class UserId {
	/** User ID is stored as a String */
	private String userId;
	
	/**
	 * User ID Constructor
	 * 
	 * Construct the User ID using the user's machine's IP Address
	 * and user's Username
	 * 
	 * @param ipAddress 
	 * @param timestamp Time at which this object was built
	 */
	public UserId(String ipAddress, Username username) {
		userId = ipAddress + "_" + username.toString();
	}
	
	/** Copy Constructor */
	public UserId(UserId userIdObject) {
		userId = userIdObject.userId;
	}
	
	/**
	 * Converts to String
	 * 
	 * @return User ID as a String
	 */
	public String toString() {
		return userId;
	}
	
	/** Equals Method  */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserId)
			return userId == ((UserId)obj).userId;
		else
			return false;
	}
}
