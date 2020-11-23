package networking;

import java.io.*;
import java.net.*;
import org.json.*;

import networking.queueManagement.*;
import networking.utility.IncomingPacket;
import infrastructure.validation.logger.*;

/**
 * 
 * This is the file which contains the clientMessageReceiver Class which is a runnable class, that means it
 * has the functionality of threads. This thread basically receives message from the server using DataInputStream.
 * After receiving the message it will push it into either conntent module queue or processing module queue based 
 * on the identifier. 
 * 
 * @author Marella Shiva Sai Teja
 */

public class ClientMessageReceiver implements Runnable {

	/**
	 * Queue into which message from content module will be pushed
	 */
	private IQueue<IncomingPacket> contModuleQueue;

	/**
	 * Queue into which message from processing module will be pushed
	 */
	private IQueue<IncomingPacket> procModuleQueue;

	/**
	 * Input stream via which we receive the messages from the server
	 */
	private DataInputStream dis;

	/** 
	 * logger object from the LoggerFactory to log messages
	*/
	ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	 * 
	 * This method is the constructor of the class which initializes the params
	 * @param contModuleQueue
	 * @param procModuleQueue
	 * @param dis
	 * There won't be any return type as it is a constructor of the class
	 */
	public ClientMessageReceiver(IQueue<IncomingPacket> contModuleQueue, IQueue<IncomingPacket> procModuleQueue, DataInputStream dis){
		this.contModuleQueue = contModuleQueue;
		this.procModuleQueue = procModuleQueue;
		this.dis = dis;
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
	public void push(String id, String msg) {

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
		logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, "Message pushed into appropriate queue by server based on the identifier");
	}
	
	/**
	 * 
	 * This is the default method that would be executed when the thread is started
	 * because the class implements Runnable interface.
	 * This method receives the message from the server and pushes it into the respective Queue.
	 * 
	 */
	@Override
    public void run(){

		/**
		 * Loops until the socket gets closed by the internet communicator, when the socket gets closed
		 * the appropriate catch would be executed.
		 */
		while(true){
			/**
			 * This block of code inside the try would try to execute instructions inside it and when it 
			 * receives any exceptions it would look for appropriate catch block.
			 */
			try{

				/**
				 * Receives the input message from the input stream
				 */
				String recvMsg = dis.readUTF();
				String id = getIdFromPacket(recvMsg);
				String msg = getMsgFromPacket(recvMsg);

				/**
				 * Calls the push function
				 */
				push(id, msg);
			}

			/**
			 * This block gets executed  if this stream reaches the end before reading all the bytes.
			 */
			catch(EOFException exp){
				//Logs exception
				logger.log(ModuleID.NETWORKING, LogLevel.WARNING, exp.toString());
				return;
			}

			/**
			 * This block gets executed if the bytes do not represent a valid modified UTF-8 encoding of a string.
			 */
			catch(UTFDataFormatException exp){
				//Logs exception
				logger.log(ModuleID.NETWORKING, LogLevel.WARNING, exp.toString());
			}

			/**
			 * This block gets executed the stream has been closed and the underlying input stream does not support 
			 * reading after close, or another I/O error occurs.
			 */
			catch(IOException exp){
				//Logs exception
				logger.log(ModuleID.NETWORKING, LogLevel.WARNING, exp.toString());
				return;
			}
		}
	}
}