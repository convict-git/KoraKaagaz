package networking;

import java.io.*;
import java.net.*;

import networking.queueManagement.ConcurrentBlockingQueue;
import networking.utility.IncomingPacket;

/**
 * 
 * This is the file which contains the SocketListener Class which is a runnable class, that means it
 * has the functionality of threads. This thread basically keeps listening on a port for the client requests, whenever
 * there is a request it accepts(In a blocking manner) them and connects them to another socket which basically
 * receives the message. After receiving the message from the client these messages are 
 * pushed into either content module queue or processing module queue based on the identifier. 
 * 
 * @author Marella Shiva Sai Teja
 */

public class SocketListener implements Runnable {
	
	/**
	 * Port number on which the socket will be listening for the client requests to connect with.
	 */
	int port;

	/**
	 * socket object which keeps listening for the client requests.
	 */
	private ServerSocket serverSocket;

	/**
	 * Queue into which message from content module will be pushed
	 */
	private ConcurrentBlockingQueue<IncomingPacket> contModuleQueue;

	/**
	 * Queue into which message from processing module will be pushed
	 */
	private ConcurrentBlockingQueue<IncomingPacket> procModuleQueue;

	/**
	 * 
	 * This method is the constructor of the class which initializes the params
	 * @param port
	 * @param contModuleQueue
	 * @param procModuleQueue
	 * 
	 * There won't be any return type as it is a constructor of the class
	 */
	public SocketListener(int port, ConcurrentBlockingQueue<IncomingPacket> contModuleQueue, ConcurrentBlockingQueue<IncomingPacket> procModuleQueue){
		this.port = port;
		this.contModuleQueue = contModuleQueue;
		this.procModuleQueue = procModuleQueue;
	}

	/**
	 * This method parses the identifier within packet received from client.
	 * @param packet Data received from the client
	 * @return Identifier
	 */
	public String getIdFromPacket(String packet){
		/**
		 * converts String to char[] array
		 */
		char[] chars = packet.toCharArray();
		
		/**
		 * String Builder to build a string for the identifier
		 */
		StringBuilder idBuilder = new StringBuilder();
		
		/**
		 * Iterates over char[] array and appends to the Identifier Builder
		 */
		for (char ch : chars) {
			if(ch == ':'){
				break;
			}
			idBuilder.append(ch);
		}

		/**
		 * Converts the String Builder to String
		 */
		String id = idBuilder.toString();
		return id;
	}

	/**
	 * This method parses the message within the packet received from client.
	 * @param packet Data received from the client
	 * @return Message
	 */
	public String getMsgFromPacket(String packet){

		char[] chars = packet.toCharArray();
		
		/**
		 * String Builder to build required message
		 */
		StringBuilder msgBuilder = new StringBuilder();

		/**
		 * A Utility Boolean flag 
		 */
		boolean flag = false;
		for (char ch : chars) {
			if(flag){
				msgBuilder.append(ch);
			}
			if(ch == ':'){
				flag = true;
			}
		}
		String msg = msgBuilder.toString();
		return msg;
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
		IncomingPacket queuePacket = new IncomingPacket(id, msg);

		/**
		 * Checks whether to push the object into content module queue or processing module queue
		 */
		if(id.equals("content")){
			contModuleQueue.enqueue(queuePacket);
		}else{
			procModuleQueue.enqueue(queuePacket);
		}
	}
	
	/**
	 * 
	 * This is the default method that would be executed when the thread is started
	 * because the class implements Runnable interface.
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
			
			/**
			 * socket keeps listening based on the static variable isRunning
			 */
			while(LanCommunicator.isRunning) {

				/**
				 * creates a socket which connects with the client for message transfer.
				 */
				Socket socket = serverSocket.accept();
				
				/**
				 * Receives the input from socket. "Remember getInputStream is Blocking type.."
				 */
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				/**
				 * Converts the received input into UTF format
				 */
				String recvMsg = input.readUTF();
				String id = getIdFromPacket(recvMsg);
				String msg = getMsgFromPacket(recvMsg);

				/**
				 * Calls the push function
				 */
				push(id, msg);

				/**
				 * Closes the socket used to connect with client for the transfer
				 * and also input stream should be closed.
				 */
				socket.close();
				input.close();
			}

		}
		
		/**
		 * This block gets executed when a exception arises in try block
		 */
		catch(IOException exp){
        	System.out.println(exp);
		}

		/**
		 * This block gets executed whether there is an exception or not in try block
		 */
		finally{
			try{
				/**
				 * Closes the socket which keeps listening on the port 
				 */
				serverSocket.close();
			}
			/**
			 * This block gets executed when an exception arises while closing the socket.
			 */
			catch(IOException e){
				System.out.println(e);
			}
		}
    }
  
}