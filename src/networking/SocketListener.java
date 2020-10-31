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

	public String getIdFromPacket(String packet){
		// convert String to char[] array
        char[] chars = packet.toCharArray();
		StringBuilder idBuilder = new StringBuilder();
        // iterate over char[] array using enhanced for loop
        for (char ch : chars) {
            if(ch == ':'){
				break;
			}
			idBuilder.append(ch);
		}
		String id = idBuilder.toString();
		return id;
	}

	public String getMsgFromPacket(String packet){
		// convert String to char[] array
        char[] chars = packet.toCharArray();
		StringBuilder idBuilder = new StringBuilder();
		// iterate over char[] array using enhanced for loop
		boolean flag = false;
        for (char ch : chars) {
			if(flag){
				idBuilder.append(ch);
			}
			if(ch == ':'){
				flag = true;
			}
		}
		String id = idBuilder.toString();
		return id;
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