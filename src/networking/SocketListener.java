package networking;

import java.io.*;
import java.net.*;
import org.json.*;

import networking.queueManagement.*;
import networking.utility.IncomingPacket;
import infrastructure.validation.logger.*;

/**
 * 
 * This file contains the SocketListener Class that is a runnable class, which means it 
 * has the functionality of threads. This thread keeps listening on a port for the client requests. 
 * Whenever there is a request, It accepts(In a blocking manner) them and connects them to another 
 * socket that receives the message. After receiving the message from the client, these messages are 
 * pushed into either the content module queue or processing module queue based on the identifier. 
 * 
 * @author Marella Shiva Sai Teja
 */

public class SocketListener implements Runnable {
	
	/**
	 * Port number on which the socket will be listening for the client requests to connect with.
	 */
	private int port;

	/**
	 * socket object which keeps listening for the client requests.
	 */
	private ServerSocket serverSocket;

	/**
	 * Queue into which message from content module will be pushed
	 */
	private IQueue<IncomingPacket> contModuleQueue;

	/**
	 * Queue into which message from processing module will be pushed
	 */
	private IQueue<IncomingPacket> procModuleQueue;

	/** 
	 * logger object from the LoggerFactory to log messages
	*/
	ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	 * Flag variable to exit loop in run method
	 */
	boolean isRunning;

	/**
	 * 
	 * This method is the constructor of the class which initializes the params
	 * @param port
	 * @param contModuleQueue
	 * @param procModuleQueue
	 * 
	 * There won't be any return type as it is a constructor of the class
	 */
	public SocketListener(
		int port, 
		IQueue<IncomingPacket> contModuleQueue, 
		IQueue<IncomingPacket> procModuleQueue
		) {

		this.port = port;
		this.contModuleQueue = contModuleQueue;
		this.procModuleQueue = procModuleQueue;
		this.isRunning = true;

	}

	/**
	 * This method parses the identifier within packet received from client.
	 * @param packet Data received from the client
	 * @return Identifier
	 */
	public String getIdFromPacket(String packet){

		/**
		 * Converts a String into json object to retreive the required values
		 */
		JSONObject json = new JSONObject(packet);

		/**
		 * Retreives the identifier from the json object
		 */
		String identifier = json.getString("identifier");

		// Returns the identifier
		return identifier;
	}

	/**
	 * This method parses the message within the packet received from client.
	 * @param packet Data received from the client
	 * @return Message
	 */
	public String getMsgFromPacket(String packet){

		/**
		 * Converts a String into json object to retreive the required values
		 */
		JSONObject json = new JSONObject(packet);

		/**
		 * Retreives the message from the json object
		 */
		String message = json.getString("message");

		// Returns the message
		return message;
	}

	/**
	 * This method pushes the message into respective queues based on their identifier.
	 * @param id Identifier of the message
	 * @param msg Message that is to be transported over the network.
	 */
	public void pushToQueue(
		String id, 
		String msg
		) {

		/**
		 * Creates a object of queue type.
		 */
		IncomingPacket queuePacket = new IncomingPacket(msg, id);

		/**
		 * Checks whether to push the object into content module queue or processing module queue
		 */
		if(id.equals("content")){
			contModuleQueue.enqueue(queuePacket);
		}else{
			procModuleQueue.enqueue(queuePacket);
		}
		logger.log(
			ModuleID.NETWORKING, 
			LogLevel.SUCCESS, 
			"Message pushed into appropriate queue by server based on the identifier"
		);
	}
	
	/**
	 * 
	 * This is the default method that would be executed when the thread is started
	 * as the class implements Runnable interface.
	 * This is the method where the server starts listening for the client requests and 
	 * when it finds one it connects it to a socket through which it receives the client message.
	 * 
	 */
	@Override
	public void run(){

		/**
		 * This block of code inside the try would try to execute instructions inside it and when it 
		 * receives any exceptions it would look for appropriate catch block.
		 */
		try {

			/**
			 * creates a socket which keeps listening on the port for client requests
			 */
			serverSocket = new ServerSocket(port);
			logger.log(
				ModuleID.NETWORKING, 
				LogLevel.INFO, 
				"Server started listening for client requests.."
			);

			/**
			 * socket keeps listening based on the variable isRunning
			 */
			while(this.isRunning) {
				Socket socket = null;
				DataInputStream input = null;

				/**
				 * This block of code inside the tries to accept the client requests if any exception occurs
				 * it checks for appropriate catch.
				 */
				try{
					
					/**
					 * creates a socket which connects with the client for message transfer.
					 */
					socket = serverSocket.accept();
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.INFO, 
						"Server has accepted a client request for data transfer"
					);

					/**
					 * Receives the data input stream from socket. "Remember getInputStream is Blocking type.."
					 */
					input = new DataInputStream(socket.getInputStream());

					/**
					 * Variable to store the entire message from the client
					 */
					String recvMsg = "";

					/**
					 * Loops until it finds the end of file in the message
					 */
					while(true){
						String newMsg = input.readUTF();
						if(newMsg.equals("EOF")) break;

						/**
						 * If it is not EOF it concatnates the message with the recvMsg
						 */
						recvMsg += newMsg;
					}

					/**
					 * Calls to the respective functions to parse the Identifier from the message
					 */
					String id = getIdFromPacket(recvMsg);
					String msg = getMsgFromPacket(recvMsg);

					/**
					 * Calls the push function
					 */
					pushToQueue(
						id, 
						msg
					);
				}

				/**
				 * This block gets executed  if this stream reaches the end before reading all the bytes.
				 */
				catch(EOFException exp){
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.ERROR, 
						exp.toString()
					);
				}

				/**
				 * This block gets executed if the bytes do not represent a valid modified UTF-8 encoding of a string.
				 */
				catch(UTFDataFormatException exp){
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.ERROR, 
						exp.toString()
					);
				}

				/**
				 * This block gets executed the stream has been closed and the underlying input stream does not support 
				 * reading after close, or another I/O error occurs.
				 */
				catch(IOException exp){
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.ERROR, 
						exp.toString()
					);
				}

				/**
				 * This block gets executed whether there is an exception or not in try block
				 */
				finally{
					try{
						/**
						 * Closes the input stream 
						 */
						if(input != null){
							input.close();
							logger.log(
								ModuleID.NETWORKING, 
								LogLevel.INFO, 
								"DataInputStream of client has been closed"
							);
						}

						/**
						 * Closes the socket connection
						 */
						if(socket != null){
							socket.close();
							logger.log(
								ModuleID.NETWORKING, 
								LogLevel.INFO, 
								"socket connection has been closed"
							);
						}
					}
					/**
					 * This block gets executed when an exception arises while closing the input stream
					 */
					catch(IOException exp){
						logger.log(
							ModuleID.NETWORKING, 
							LogLevel.ERROR, 
							exp.toString()
						);
					}
				}
			}
		}
		
		/**
		 * This block gets executed when a exception arises in try block
		 */
		catch(IOException exp){
			logger.log(
				ModuleID.NETWORKING, 
				LogLevel.ERROR, 
				exp.toString()
			);
		}

		/**
		 * This block gets executed whether there is an exception or not in try block
		 */
		finally{
			try{
				/**
				 * Closes the socket which keeps listening on the port 
				 */
				if(serverSocket != null){
					serverSocket.close();
					logger.log(
						ModuleID.NETWORKING, 
						LogLevel.INFO, 
						"ServerSocket has been closed"
					);
				}
			}
			/**
			 * This block gets executed when an exception arises while closing the socket.
			 */
			catch(IOException exp){
				logger.log(
					ModuleID.NETWORKING, 
					LogLevel.ERROR, 
					exp.toString()
				);
			}
		}
	}
	
	/**
	 * This method stops the server socket. This is called by LanCommunicator's stop method.
	 */	
	public void stop(){
		try{
			this.isRunning = false;
			/**
			 * Closes the socket which keeps listening on the port 
			 */
			if(serverSocket != null){
				serverSocket.close();
				logger.log(
					ModuleID.NETWORKING, 
					LogLevel.INFO, 
					"Server has been closed"
				);
			}
		}
		/**
		 * This block gets executed when an exception arises while closing the socket.
		 */
		catch(IOException exp){
			logger.log(
				ModuleID.NETWORKING, 
				LogLevel.ERROR, 
				exp.toString()
			);
		}
	}

}