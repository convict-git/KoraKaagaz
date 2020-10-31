package networking;

import java.io.*;
import java.net.*;

import networking.queueManagement.ConcurrentBlockingQueue;
import networking.utility.IncomingPacket;

/**
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
	 * This method parses the identifier within packet received from the client
	 * @param packet	Data received from the client
	 * @return	Identifier
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

    public void push(String id, String msg) {
		IncomingPacket queuePacket = new IncomingPacket(msg);
    	if(id.equals("processing")){
    		procModuleQueue.enqueue(queuePacket);
    	}else if(id.equals("content")) {
    		contModuleQueue.enqueue(queuePacket);
    	}
    }
    
    public void run(){
		try {
    		serverSocket = new ServerSocket(port);
    		while(LanCommunicator.isRunning) {
    			Socket socket = serverSocket.accept();
    			DataInputStream input = new DataInputStream(socket.getInputStream());
    			String recvMsg = input.readUTF();
				String id = getIdFromPacket(recvMsg);
				String msg = getMsgFromPacket(recvMsg);

				push(id, msg);

				socket.close();
				input.close();
			}
    	}
        catch(IOException exp){
        	System.out.println(exp);
		}
		finally{
			try{
				serverSocket.close();
			}catch(IOException e){
				System.out.println(e);
			}
		}
    }
  
}