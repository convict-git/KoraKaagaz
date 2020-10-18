package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the User ID
public class UserId {
	private String userId; // user ID String
	
	// Build the User ID using the IP Address and
	// the username
	public UserId(String ipAddress, Username username) {
		userId = ipAddress + "_" + username.toString();
	}
	
	// Convert User ID to String
	public String toString() {
		return userId;
	}
}
