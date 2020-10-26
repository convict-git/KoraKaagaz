package networking;

import java.io.*;
import java.net.*;

/**
*
* @author Marella Shiva Sai Teja
*/

public class SocketListener implements Runnable {
	
	int port;
	private ServerSocket serverSocket;
    
    public SocketListener(int port){
    	this.port = port;
    }

    public void push(String id, String msg) {
    	if(id.equals("processing")){
    		processingReceiveQueue.enqueue(msg);
    	}else if(id.equals("content")) {
    		contentReceiveQueue.enqueue(msg);
    	}
    }
    
    public void run(){
		try {
    		serverSocket = new ServerSocket(port);
    		while(true) {
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