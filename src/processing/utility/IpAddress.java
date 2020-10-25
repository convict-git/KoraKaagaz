package processing.utility;

/**
 * 
 * @author Himanshu Jain
 *
 */

public class IpAddress {
	// The ipAddress is internally stored as a String
    private String ipAddress;
    
    // Build ipAddress as string
    public IpAddress(String ipAddress) {
    	this.ipAddress = ipAddress;
    }

    // Copy Constructor for IP Address
    public IpAddress(IpAddress ipAddressObject) {
    	ipAddress = ipAddressObject.ipAddress;
    }
    
    // Return the ipAddress String
    public String toString() {
    	return ipAddress;
    }
    
    @Override
	public boolean equals(Object obj) {
		if(obj instanceof IpAddress)
			return ipAddress == ((IpAddress)obj).ipAddress;
		else
			return false;
	}
}
