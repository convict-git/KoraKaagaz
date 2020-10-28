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
	
	int port;
	private ServerSocket serverSocket;
	private ConcurrentBlockingQueue<IncomingPacket> contModuleQueue;
	private ConcurrentBlockingQueue<IncomingPacket> procModuleQueue;

    public SocketListener(int port, ConcurrentBlockingQueue<IncomingPacket> contModuleQueue, ConcurrentBlockingQueue<IncomingPacket> procModuleQueue){
		this.port = port;
		this.contModuleQueue = contModuleQueue;
		this.procModuleQueue = procModuleQueue;
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
    			String id = "";
    			if(recvMsg.charAt(0) == 'p') {
    				id = "processing";
    			}else if(recvMsg.charAt(0) == 'c') {
    				id = "content";
    			}else {
    				System.out.println("invalid identifier");
    			}
    			
    			String msg = recvMsg.substring(1);
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