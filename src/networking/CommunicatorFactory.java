package networking;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.net.DataInputStream;
//import java.net.DataOutputStream;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.*;

import networking.ICommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;
//import infrastructure.validation.logger.*;

/**
* This file contains information about CommunicatorFactory class, This class is used for implementation of creation logic 
* of LanCommunicator object, which uses Singleton factory design pattern.
*
* @author Pulagam Prudhvi Vardhan Reddy
*
*/

public class CommunicatorFactory{

	/** initialising ICommunicator variable to store the instance of LanCommunicator class to implement singleton design pattern. */
	private static ICommunicator communicatorInstance=null;

	/** saving logger object from LoggerFactory class to log messages */
	//static ILogger logger=LoggerFactory.getLoggerInstance();

	private static String str=null;

	private static String getState(){
		return str;
	}

	private static void setState(String temp){
		str=temp;
	}
	
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
	public synchronized static ICommunicator getCommunicator(int port){

		/** 
		* checking for communicatorinstance if LanCommunicator is already created or not 
		*/
		if(communicatorInstance==null){
			if(getState().equals("INTERNET"))
				communicatorInstance=new InternetCommunicator(port);
			/** 
			* creating Lan communicator object which is going to be used for communication in future. 
			*/
			else	communicatorInstance=new LanCommunicator(port);
			//logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, "LanCommunicator object created successfully"); 
		}
		//else
			//logger.log(ModuleID.NETWORKING, LogLevel.WARNING, "LanCommunicator object already created");
		/** 
		* returning new or already created LanCommunicator object 
		*/
		return communicatorInstance;
	}
	private static String[] getServerInfo(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		 
		/** Building Document */
		Document document = builder.parse(new File("data.xml"));
		 
		/** Normalizing the XML Structure */
		document.getDocumentElement().normalize();
		 
		Element e = document.getDocumentElement();
		System.out.println("ip"+e.getElementsByTagName("ip").item(0).getTextContent());
		System.out.println("port"+e.getElementsByTagName("port").item(0).getTextContent());
		return new String[]{e.getElementsByTagName("ip").item(0).getTextContent().toString(),e.getElementsByTagName("port").item(0).getTextContent().toString()};
		}
		catch(Exception e){
			System.out.println(e);
			return new String[2];
			} 
	}

	private static int getClientId(){

		/** getting aws server info */
		String[] str=getServerInfo();

		/** This variable is used to store the client id */
		String from="";
		try{
			/** Creates a stream socket and connects it to the specified port number at the specified IP address. */
			//Socket sock=new Socket(str[0],Integer.parseInt(str[1]));
			Socket sock=new Socket("127.0.0.1",5000);

			/** Creating DataInputStream that uses underlying input stream of the socket through BufferedInputStream. */
			DataInputStream in=new DataInputStream(new BufferedInputStream(sock.getInputStream()));

			/** Creating DataOutputStream that uses underlying output stream of socket. */
			DataOutputStream out=new DataOutputStream(sock.getOutputStream());
			
			/** Sending message to server to get Client id of respective client */
			out.writeUTF("GET_CLIENT_INFO");

			int i=0;
			/** reading only client id and nothing else */
			while(i!=1 && (from=in.readUTF())!=null){
				i++;
			}
		}
		catch(ConnectException e){
			System.out.println("Check with server, Error in establishing connection "+e.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(from.equals(""))
				return -1;
			else return Integer.parseInt(from);
		}
	}

	/**
	* This method provides information about private IP address of the client and free port available at client without 
	* creating the object of CommunicatorFactory class.
	*
	* @return ClientInfo object which consists of local or private IP address and free port number at client.
	*/
	public synchronized static ClientInfo getClientInfo(){

		/*if(str==null){
			ch
		}*/
		//System.out.println(getClientId());
		/** This try block code tries to establish Datagram socket connection and if it face any exception in its way 
		* it catches appropriate exception and executes that catch block code.
		*/
		try{
			/** 
			* using datagram oriented protocol over connection oriented protocol because it is faster, simpler and
			* more efficient than connection oriented.
			*/
			DatagramSocket sock =new DatagramSocket();

			//logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, "Datagram socket created");
			
			/** 
			* This try block code tries to establish Datagram socket connection in Local Area Network machine and 
			* if it face any exception in its way it catches appropriate exception and executes that catch block code.
			*/
			try{
				/**
				* tries to connect to the specified private IP and port to the Local Area Network machine 
				*/
				sock.connect(InetAddress.getByName("10.0.0.0"),1024);
				//logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, "Datagram set to send");
				
				/**
				* obtaining the private IP address of the client.
				*/
				String ip=sock.getLocalAddress().getHostAddress().toString();

				int port;
				if(getState()==null){
					port =getClientId();
					if(port==-1){
						setState("LAN");
					}
					else setState("INTERNET");
				}
				else if(getState().equals("INTERNET"))
					port=getClientId();
				/**
				* obtaining the port used here which can be used further after closing this socket here in this file
				*/ 
				else port = sock.getLocalPort();

				/** disconnecting and closing the datagram socket.So now port used in this file for finding IP is free */
				sock.disconnect();
				sock.close();
				//logger.log(ModuleID.NETWORKING,LogLevel.SUCCESS,"closed the Datagram socket");

				/** returning the private IP and free port available */
				return new ClientInfo(ip,port);
			}

			/** This catch block is executed after raising the exception when there is given host is unkown. */
			catch(UnknownHostException e){
				//logger.log(ModuleID.NETWORKING, LogLevel.ERROR,"UnknownHost "+e.toString());
				return new ClientInfo();
			}

			/**
			* This catch block code is executed after facing IllegalArgumentException at try block, This exception raises
			* if there is wrong format in arguments given.
			*/
			catch (IllegalArgumentException e) {
				//logger.log(ModuleID.NETWORKING, LogLevel.ERROR,"Invalid arguments in connetion method"+e.toString());
				return new ClientInfo();
			}

			/** 
			* This catch block code is executed after facing SecurityException at try block, This exception raises
			* if a security manager exists and its checkListen method doesn't allow the operation
			*/
			catch (SecurityException e) {
				//logger.log(ModuleID.NETWORKING, LogLevel.ERROR,"Security Manager exists "+e.toString());
				return new ClientInfo();
			}
		}

		/**
		* This catch block code is executed after facing SocketException at try block, This exception raises
		*  if the socket could not be opened, or the socket could not bind to the specified local port.
		*/
		catch(SocketException e){
				//logger.log(ModuleID.NETWORKING, LogLevel.ERROR,"connetion to other host failed"+e.toString());
				return new ClientInfo();
				
		}

		/** 
		* This catch block code is executed after facing SecurityException at try block, This exception raises
		* if a security manager exists and its checkListen method doesn't allow the operation
		*/
		catch (SecurityException e) {
			//logger.log(ModuleID.NETWORKING, LogLevel.ERROR,"Security Manager exists "+e.toString());
			return new ClientInfo();
		}
	}

	/**
	* This method freeCommunicator frees or releases the LanCommunicator instance stored at CommunicatorFactory class.
	*/
	public static void freeCommunicator(){

		/** 
		* releasing the communicatorinstance which makes it eligible for garbage collection after processing module
		* releases the given communicator.
		*/
		communicatorInstance=null;
		//logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, "freed Communicator at CommunicatorFactory");
		setState("");
	}
}