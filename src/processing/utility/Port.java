package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Port
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Port implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = 9155983265223929975L;

	/** Port Number as an integer value */
	public int port;
	
	/**
	 * Port Constructor
	 * 
	 * @param port Port as an integer value
	 */
	public Port(int port) {
		this.port = port;
	}
	
	/** Copy Constructor */
	public Port(Port portObj) {
		port = portObj.port;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Port)
			return port == ((Port)obj).port;
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return port;
	}
}