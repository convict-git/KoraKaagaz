package networking;

//import networking.LanCommunicator;
import networking.*;
import networking.utility.ClientInfo;

/**
*
* @author Pulagam Prudhvi Vardhan Reddy
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