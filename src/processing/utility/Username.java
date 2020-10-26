package processing.utility;

/**
 * Class Representing a user's Username
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Username {
	/** Username is stored as a String */
    private String username;
    
    /**
     * Username Constructor
     * 
     * @param username Username String
     */
    public Username(String username) {
    	this.username = username;
    }
    
    /** Copy Constructor */
    public Username(Username usernameObject) {
    	username = usernameObject.username;
    }
    
    /**
     * Converts Username to String
     * 
     * @return Username as a String
     */
    public String toString() {
    	return username;
    }
    
    /** Equals Method */
    @Override
	public boolean equals(Object obj) {
		if(obj instanceof Username)
			return username == ((Username)obj).username;
		else
			return false;
	}
}
