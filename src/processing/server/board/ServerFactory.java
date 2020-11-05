package processing.server.board;

/**
 * This is a factory class. Its function getIPHandler() will be called by the
 * content team so that they can use the function getClientDetails defined in the 
 * IClientDetails interface.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class ServerFactory {
	
	/**
	 * clientIP is an object of class ClientIP which implements IClientIP
	 * interface which has the function to give user details to content 
	 * module. So a new instance of class ClientIP is created and saved in 
	 * clientIP.
	 */
	public static ClientIP clientIP = new ClientIP();
	
	/**
	 * Making the constructor as private so as none can create an instance of 
	 * this class so as to make it a singleton pattern to send the same ClientIP
	 * instance everytime.
	 */
	private ServerFactory() {};

	/**
	 * Function which will content module call to get the instance of the ClientIP
	 * to use the function defined in that class.
	 * 
	 * @return instance of class ClientIP
	 */
	public static ClientIP getIPHandler() {
		return clientIP;
	}
	
}
