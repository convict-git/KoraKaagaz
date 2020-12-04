package networking.utility;

import infrastructure.validation.logger.*;

/**
* This file contains ClientInfo class, which is used as utility for providing client's IP address
* and free Port number on which the White Board application going to listening too or clientId available
* at helping server(AWS) depending on type of communicator using.
* If Network Communicator is used then ClientId is returned else port number is returned.
*
* @author Prudhvi Vardhan Reddy Pulagam
* 
*/

public class ClientInfo{

	/** This variable stores the private IP of the client */
	private String ip;

	/** This variable stores a free port available at client */
	private int port;

	/** having logger object from LoggerFactory to log messages */
	ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	* This is default constructor of ClientInfo. As the functions invoking getClientInfo are not designed 
	* handle the exception like NullPointerException, So this default constructor is used to show any error
	* takes place, and logger module shows particular error. 
	*/ 
	public ClientInfo() {
		this.ip="0.0.0.0";
		this.port=-1;

		logger.log(
			ModuleID.NETWORKING,
			LogLevel.INFO,
			"created dummy ClientInfo object"
		);
	}

	/**
	* This is the actual constructor which gives the Private IP and free port number available at client or clientId available
	* at helping server(AWS) depending on type of communicator using.
	*/
	public ClientInfo( String ip , int port ) {
		this.ip=ip;
		this.port=port;

		logger.log(
			ModuleID.NETWORKING,
			LogLevel.INFO,
			"created ClientInfo object with IP and port available at client"
		);
	}


	/**
	* @return String which is client's private IP address.
	*/
	public String getIp(){
		return this.ip;
	}

	/**
	* @return int which is free port number available at client or clientId available
	* at helping server(AWS) depending the type of communicator using. 
	*/
	public int getPort(){
		return this.port;
	}
}
