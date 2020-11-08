package networking.server;
import networking.queueManagement.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerManager {
    public static void main(String[] args) throws IOException 
	{
        int client = 0;
		// server is listening on port 5000 
		ServerSocket ss = new ServerSocket(5000); 

		HashMap<String, IQueue<String>>map = new HashMap<String, IQueue<String> >(); 
		// running infinite loop for getting 
		// client request 
		while (true)
		{ 
			Socket s = null; 
			
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				String msg = dis.readUTF();
				if(msg == "GET_CLIENT_INFO"){
					String id = client+"";
					dos.writeUTF(id);
					client++;
				}
				else{
					String[] arr = msg.split(":");
					String id = arr[0];
					IQueue<String> q = new ConcurrentBlockingQueue<String>();
					map.put(id,q);
					// create a new thread object 
					Thread t1 = new ReceiveThread(dis,map);
					Thread t2 = new SendThread(dos,q);
	
					// Invoking the start() method 
					t1.start();
					t2.start();
				}
	
			}
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
		try{
			ss.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	} 
}
