package processing.utility;

public class UserId {
	private String userId;
	
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
