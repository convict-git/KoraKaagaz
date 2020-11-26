package processing.utility;

import java.io.Serializable;

/**
 * Class Representing IP Address
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class IpAddress implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = -292807028024686686L;
	
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
			return ipAddress.equals(((IpAddress)obj).ipAddress);
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return ipAddress.hashCode();
	}
}
