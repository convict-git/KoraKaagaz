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
    
    // Return the username String
    public String toString() {
    	return username;
    }
}
