package processing.utility;

/**
 * Class Representing a Port
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Port {
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
}