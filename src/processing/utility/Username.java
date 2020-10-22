package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Username
public class Username {
    // The Username is internally stored as a String
    private String username;
    
    // Build username as string
    public Username(String username) {
    	this.username = username;
    }
    
    // Copy Constructor for Username
    public Username(Username usernameObject) {
    	username = usernameObject.username;
    }
    
    // Return the username String
    public String toString() {
    	return username;
    }
    
    @Override
	public boolean equals(Object obj) {
		if(obj instanceof Username)
			return username == ((Username)obj).username;
		else
			return false;
	}
}
