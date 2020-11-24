package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a user's User ID
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class UserId implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = 5435383521733211617L;
	
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
	public UserId(IpAddress ipAddress, Username username) {
		userId = ipAddress.toString() + "_" + username.toString();
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
	
	/**
	 * Gets the Username present in the User ID
	 * 
	 * @return username who has this User ID
	 */
	public Username getUsername() {
		return new Username(userId.split("_", 2)[1]);
	}
	
	/** Equals Method  */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserId)
			return userId.equals(((UserId)obj).userId);
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return userId.hashCode();
	}
}
