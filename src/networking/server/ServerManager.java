package networking.server;

/**
 * This file contains the class ServerManager which will be running on the server
 * and used for checking if client is connected to internet, to get id for a client
 * and to create two threads (for each client) for listening the messages from a client 
 * and another for sending it back.
 *
 * @author Madaka Srikar Reddy
 * 
 */

import java.io.*;
import java.net.*;
import java.util.HashMap;
import networking.queueManagement.*;
import infrastructure.validation.logger.*;

public class ServerManager {
	
	/**
	 * This is the main method that will be running in the server that is used as
	 * intermediate between clients
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		/** Logger for logging errors and activities */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/**
		 * Creating a socket and clientID contains the id that we will be giving the
		 * clients
		 */
		int clientID = 1;
		ServerSocket ss = new ServerSocket(5000);

		/** This hashmap contains the map of the client id and their queue */
		HashMap<String, IQueue<String>> map = new HashMap<String, IQueue<String>>();

		/** Continously listening for connections from client */
		while (true) {
			Socket s = null;

			try {
				/** Accepting a connection from a client */
				s = ss.accept();
				/** Used for reading the data from client */
				DataInputStream dis = new DataInputStream(s.getInputStream());
				/** Used for writing data to the client */
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				/** Reading message from the client */
				String msg = dis.readUTF();
				/**
				 * If the message is CHECK_INTERNET it is used to check if client can be
				 * connected to the server
				 */
				if (msg.trim().equals("CHECK_INTERNET")) {
					dos.writeUTF("CONNECTED");
					s.close();
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.INFO, 
						"successfully returned that client is connected to internet"
					);
				}
				/**
				 * If the message is GET_CLIENT_INFO we give the client the id which is unique
				 * to them
				 */
				else if (msg.trim().equals("GET_CLIENT_INFO")) {
					String id = clientID + "";
					dos.writeUTF(id);
					clientID++;
					s.close();
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.INFO, 
						"successfully returned clientid to the client"
					);
				}
				/**
				 * If the message is like clientID:msg we create a queue and two threads for the
				 * client to read message and send message to the client
				 */
				else {
					String[] arr = msg.split(":");
					String id = arr[0];
					IQueue<String> q = new ConcurrentBlockingQueue<String>();
					map.put(id, q);
					Thread t1 = new ReceiveThread(dis, map);
					Thread t2 = new SendThread(dos, q);
					t1.start();
					t2.start();
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.INFO, 
						"Successfully created queue and two threads for the client"
					);
				}

			} catch (Exception e) {
				s.close();
				logger.log(
					ModuleID.NETWORKING, 
					LogLevel.ERROR, 
					e.toString()
				);
			}
		}
	}
}
