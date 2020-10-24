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
	public UserId(IpAddress ipAddress, Username username) {
		userId = ipAddress.toString() + "_" + username.toString();
	}
	
	// Convert User ID to String
	public String toString() {
		return userId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserId)
			return userId == ((UserId)obj).userId;
		else
			return false;
	}
}
