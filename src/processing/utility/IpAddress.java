package processing.utility;

/**
 * Class Representing IP Address
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class IpAddress {
	/** IP Address String */
	private String ipAddress;
	
	/**
	 * IP Address Constructor
	 * 
	 * @param ipAddress IP Address Constructor
	 */
	public IpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	/** Copy Constructor */
	public IpAddress(IpAddress ipAddressObject) {
		ipAddress = ipAddressObject.ipAddress;
	}
	
	/**
	 * Converts to String
	 * 
	 * @return IP Address as a String
	 */
	public String toString() {
		return ipAddress;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IpAddress)
			return ipAddress == ((IpAddress)obj).ipAddress;
		else
			return false;
	}
}
