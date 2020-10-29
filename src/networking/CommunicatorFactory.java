package networking;

//import networking.LanCommunicator;
import networking.*;
import networking.utility.GetClientInfo;

/**
*
* @author Pulagam Prudhvi Vardhan Reddy
*
*/

public class CommunicatorFactory{
	
	public static ICommunicator getCommunicator(int port){
		return new LanCommunicator();
	}
	public static GetClientInfo getClientInfo(){
		return new GetClientInfo();
	}
	public static void freeCommunicator(){
		return;
	}
}