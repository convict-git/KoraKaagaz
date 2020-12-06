package networking;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.*;

import networking.ICommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;
import infrastructure.validation.logger.*;

/**
* This file contains information about CommunicatorFactory class, This class is used for implementation of creation logic 
* of LanCommunicator object as well as InternetCommunicator object, where Singleton factory design pattern is used.
* This creation of appropriate objects is done based on the availability of internet and server(AWS or any specified), While creating 
* the Communicator object priority is given to Internet Communicator over LAN. Meaning initial check is done for availability of internet 
* and server and if these are not available then goes for LAN.
*
* @author Pulagam Prudhvi Vardhan Reddy
*
*/

public class CommunicatorFactory {

	/** initialising ICommunicator variable to store the instance of LanCommunicator or InternetCommunicator class to implement singleton design pattern. */
	private static ICommunicator communicatorInstance = null;

	/** saving logger object from LoggerFactory class to log messages */
	static ILogger logger = LoggerFactory.getLoggerInstance();

	/** Used for storing type of communicator going to be used for full session */
	private static String typeOfCommunicator = null;

	/**
	* This is getter method used in accessing and knowing the type of Communicator
	*
	* @return String of type of communicator going or using for communication
	*/
	private static String getTypeOfCommunicator() {
		return typeOfCommunicator;
	}

	/** 
	* This is setter method used in assigning the type of communicator going to be used in communication in near future
	*
	* @param temp indicates type of communicator
	*/ 
	private static void setTypeOfCommunicator( String temp ) {
		typeOfCommunicator=temp;
	}
	
	/**
	* Constructor of this class is not accessible to other packages or classes.
	*/
	private CommunicatorFactory() {

	}
	
	/**
	* This method creates the LanCommunicator object as well as InternetCommunicator object based on the the type of communicator 
	* set. This uses Singleton and factory design pattern.
	*
	* @param port is a free port number available at client.
	*
	* @return ICommunicator going to used for communication.
	*/
	public synchronized static ICommunicator getCommunicator( int port ) {

		/** 
		* checking for communicatorinstance if LanCommunicator is already created or not 
		*/
		if ( communicatorInstance == null ) {
			/** checking to create type of communicator available for communication */
			if ( getTypeOfCommunicator().equals("INTERNET") ) {
				/** creating Internet Communicator object, which is going to be used for full session. */
				communicatorInstance = new InternetCommunicator(port);
				
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.SUCCESS,
					"InternetCommunicator object created successfully"
				);
			}
			/** creating LanCommunicator object which is going to be used for communication in future.	*/
			else {
				communicatorInstance=new LanCommunicator(port);
				
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.SUCCESS,
					"LanCommunicator object created successfully"
				);
			} 
		}

		else
			logger.log(
				ModuleID.NETWORKING,
				LogLevel.WARNING,
				"ICommunicator object already created"
			);
		
		/** returning new or already created ICommunicator object */
		return communicatorInstance;
	}


	/**
	* This access the XML file which consists of Server IP address and listening port at server, These details are stored 
	* by the respective user or client.
	*
	* @return String[] which consists of AWS server IP address and port
	*/
	static String[] getServerInfo() {
		try{
			/** creating DocumentBuilder object */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
		 
		 	String home = System.getProperty("user.home");
			/** Building Document from the file ServerInfo.xml*/
			Document document = builder.parse( new File(home+"/.config/KoraKaagaz/ServerInfo.xml") );
		 
			/** Normalising the XML Structure */
			document.getDocumentElement().normalize();
		 
		 	/** getting the root element from XML document */
			Element root = document.getDocumentElement();

			/** getting the specified IP address of the server */
			String ip=root.getElementsByTagName( "ip" ).item( 0 ).getTextContent().toString();

			/** getting the specified listening port at server */
			String port=root.getElementsByTagName( "port" ).item( 0 ).getTextContent().toString();

			logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS,"Successfully accessed the server info");
			return new String[]{ip,port};
		}
		catch ( Exception e ){
			logger.log(
				ModuleID.NETWORKING,
				LogLevel.ERROR,
				e.toString()
			);
			return new String[2];
		} 
	}

	/**
	* This method helps in getting clientid(which is unique) assigned to this client at server
	*
	* @return int which is client id assigned at the server.
	*/
	private static int getClientId() {

		/** getting aws server info */
		String[] str = getServerInfo();

		/** if there is no provided  or error in reading IP address and port of AWS(or any) server. */
		if ( str[0] == null || str[1] == null )
			return -1;

		/** This variable is used to store the client id */
		String from = "";
		Socket sock = null;
		try {
			/** Creates a stream socket and connects it to the specified port number at the specified IP address. */
			sock = new Socket( str[0] , Integer.parseInt( str[1] ) );
			
			/** Creating DataInputStream that gets input underlying input stream of the socket */
			DataInputStream in = new DataInputStream( sock.getInputStream() );

			/** Creating DataOutputStream that uses underlying output stream of socket. */
			DataOutputStream out = new DataOutputStream( sock.getOutputStream() );
			
			/** Sending message to server to get Client id of respective client */
			out.writeUTF( "GET_CLIENT_INFO" );

			/** reading client id from socket's input stream through datainputstream */
			from = in.readUTF();
		}
		catch ( ConnectException e ){
			logger.log(
				ModuleID.NETWORKING,
				LogLevel.ERROR,
				"Check with server, Error in establishing connection "+e.toString()
			);
		}
		catch ( Exception e ){
			logger.log(
				ModuleID.NETWORKING,
				LogLevel.ERROR,
				e.toString()
			);
			
		}
		finally{
			try {
				if ( sock != null )
					sock.close();
			} catch ( IOException e ) {
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.ERROR, 
						"Error in closing the socket" + e.toString()
					);
			}
		}
		/** In case failed to get client Id from server */
		if ( from.equals("") )
			return -1;

		else return Integer.parseInt( from );
	}

	/**
	* This method provides information about private IP address of the client and free port available at client without 
	* creating the object of CommunicatorFactory class.
	*
	* @return ClientInfo object which consists of local or private IP address and free port number at client.
	*/
	public synchronized static ClientInfo getClientInfo() {

		DatagramSocket sock;

		/** This try block code tries to establish Datagram socket connection and if it face any exception in its way 
		* it catches appropriate exception and executes that catch block code.
		*/
		try {
			/** 
			* using datagram oriented protocol over connection oriented protocol because it is faster, simpler and
			* more efficient than connection oriented.
			*/
			sock =new DatagramSocket();

			logger.log(
				ModuleID.NETWORKING,
				LogLevel.SUCCESS,
				"Datagram socket created"
			);
			
			String ip = "";
			int port = -1;

			/** 
			* This try block code tries to establish Datagram socket connection in Local Area Network machine and 
			* if it face any exception in its way it catches appropriate exception and executes that catch block code.
			*/
			try {
				/**
				* tries to connect to the specified private IP and port to the Local Area Network machine 
				*/
				sock.connect( InetAddress.getByName("10.0.0.0") , 1024 );
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.INFO,
					"Datagram set to send"
				);
				
				/**
				* obtaining the private IP address of the client.
				*/
				ip = sock.getLocalAddress().getHostAddress().toString();

				/** Checking for the type of Communicator we are running Lan or Internet communicator */
				if ( getTypeOfCommunicator() == null ) {

					/** Initially checks for internet availability and server(AWS or any specified IP and port in XML file) availability */ 
					port = getClientId();

					/** 
					* Initialising the CommunicatorFactory class to create LAN communicator as there is 
					* no internet or AWS availability 
					*/ 
					if ( port == -1 ) {
						setTypeOfCommunicator( "LAN" );
						/** getting local port for LAN communication */
						port = sock.getLocalPort();
					}
					
					/** 
					* Initialising the CommunicatorFactory class to create Internet communicator as there is internet available 
					* and as well as specified server 
					*/
					else setTypeOfCommunicator( "INTERNET" );
					logger.log(
						ModuleID.NETWORKING,
						LogLevel.INFO,
						"Determined the type of Communicator going to used"
					);
				}

				else if ( getTypeOfCommunicator().equals( "INTERNET" ) )
					port = getClientId();
				/**
				* obtaining the port used here which can be used further after closing this socket here in this file
				*/ 
				else port = sock.getLocalPort();
			}

			/** This catch block is executed after raising the exception when there is given host is unkown. */
			catch ( UnknownHostException e ) {
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.ERROR,
					"UnknownHost "+e.toString()
				);

				sock.close();

				return new ClientInfo();

			}

			/**
			* This catch block code is executed after facing IllegalArgumentException at try block, This exception raises
			* if there is wrong format in arguments given.
			*/
			catch ( IllegalArgumentException e ) {
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.ERROR,
					"Invalid arguments in connetion method "+e.toString()
				);
			}

			/** 
			* This catch block code is executed after facing SecurityException at try block, This exception raises
			* if a security manager exists and its checkListen method doesn't allow the operation
			*/
			catch ( SecurityException e ) {
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.ERROR,
					"Security Manager exists "+e.toString()
				);
			}

			/** disconnecting and closing the datagram socket.So now port used in this file for finding IP is free */
			sock.disconnect();
			sock.close();

			logger.log(
				ModuleID.NETWORKING,
				LogLevel.SUCCESS,
				"closed the Datagram socket"
			);

			if ( ip.equals("") ||
				ip.equals("0.0.0.0") ||
				port == -1 ) {
			/** Found no valid IP or port So returning default IP and port, which are invalid too */
				return new ClientInfo();
			}
			else {
				return new ClientInfo( ip , port );
			}
		}

		/**
		* This catch block code is executed after facing SocketException at try block, This exception raises
		* if the socket could not be opened, or the socket could not bind to the specified local port.
		*/
		catch ( SocketException e ){
				logger.log(
					ModuleID.NETWORKING,
					LogLevel.ERROR,
					"connetion to other host failed "+e.toString()
				);
		}

		/** 
		* This catch block code is executed after facing SecurityException at try block, This exception raises
		* if a security manager exists and its checkListen method doesn't allow the operation
		*/
		catch ( SecurityException e ) {
			logger.log(
				ModuleID.NETWORKING,
				LogLevel.ERROR,
				"Security Manager exists "+e.toString()
			);
		}
		/** Found no valid IP or port So returning default IP and port,which are invalid too */
		return new ClientInfo();
	}

	/**
	* This method freeCommunicator frees or releases the LanCommunicator instance stored at CommunicatorFactory class.
	*/
	public static void freeCommunicator() {

		/** 
		* releasing the communicatorinstance which makes it eligible for garbage collection after processing module
		* releases the given communicator.
		*/
		communicatorInstance=null;
		logger.log(
			ModuleID.NETWORKING,
			LogLevel.SUCCESS,
			"freed Communicator at CommunicatorFactory"
		);
		
		/** unsetting the type of communicator */
		setTypeOfCommunicator(null);

		logger.log(
			ModuleID.NETWORKING,
			LogLevel.INFO,
			"Free to create any type of Communicator object"
		);


	}
}