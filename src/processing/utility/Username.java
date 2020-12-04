package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a user's Username
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Username implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = -8485675822804355578L;
	
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
			return username.equals(((Username)obj).username);
		else
			return false;
	}
    
    /** HashCode Method */
	@Override
	public int hashCode() {
		return username.hashCode();
	}
}
