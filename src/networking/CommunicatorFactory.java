package networking;

import networking.ICommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;

/**
* This file contains information about CommunicatorFactory class, This class is used for implementation of creation logic 
* of LanCommunicator object, which uses Singleton factory design pattern.
*
* @author Pulagam Prudhvi Vardhan Reddy
*
*/

public class CommunicatorFactory{

	/**
	* Constructor of this class is not accessible to other packages or classes.
	*/
	private CommunicatorFactory(){

	}
	
	/**
	* This method helps in creating the LanCommunicator object which uses Singleton and factory design pattern in 
	* creating LanCommunicator object.
	*
	* @param port is a free port number available at client.
	*
	* @return LanCommunicator(which implements ICommunicator interface) object.
	*/
	public static ICommunicator getCommunicator(int port){
		return new LanCommunicator();
	}

	/**
	* This method provides information about private IP address of the client and free port available at client without 
	* creating the object of CommunicatorFactory class.
	*
	* @return ClientInfo object which consists of local or private IP address and free port number at client.
	*/
	public static ClientInfo getClientInfo(){
		return new ClientInfo();
	}
}