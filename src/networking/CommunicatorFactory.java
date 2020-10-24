package networking;

//import networking.LanCommunicator;
import networking.*;

/**
*
* @author Pulagam Prudhvi Vardhan Reddy
*
*/

public class CommunicatorFactory{
	
	public static ICommunicator getCommunicator(int port){
		return new LanCommunicator();
	}
	public static int getPort(){
		return 0;
	}
}