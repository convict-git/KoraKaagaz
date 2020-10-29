package networking;

//import networking.LanCommunicator;
import networking.*;
import networking.utility.ClientInfo;

/**
*
* @author Pulagam Prudhvi Vardhan Reddy
*
* @CommunicatorFactory class used in creation logic for LanCommunicator, This class
* tries to implement Singleton factory pattern.
*
* @getCommunicator() method returns LanCommunicator(which implements ICommunicator interface) object and takes an argument Client port number as integer.
*
* @getClientInfo() method returns Client information about private IP and Port number as object @ClientInfo and takes no argument.
*
*/

public class CommunicatorFactory{
	
	public static ICommunicator getCommunicator(int port){
		return new LanCommunicator();
	}
	public static ClientInfo getClientInfo(){
		return new ClientInfo();
	}
}