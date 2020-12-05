package networking.testSimulator;
import java.io.IOException;
import java.net.ServerSocket;

import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;

/**
 * This  is helper class for testing the LAN communication
 * @author sravan
 *
 */
public class LanTestHelper  {
	
	/**
	 * This method is used to run the test for LAN Communication 
	 * for a given number of messages and given message length
	 * 
	 * @param numMessages
	 * @param msgLength
	 * @return boolean, returns whether this ran successfully or not
	 */
	public boolean run(int numMessages,int msgLength) {
		
		//Get the client IP and ports of two clients
		ClientInfo info = CommunicatorFactory.getClientInfo();
		ClientInfo client1 = null;
		ClientInfo client2 =  null;
		
		try {
			ServerSocket s = new ServerSocket(0);
			client1 = new ClientInfo(info.getIp(), s.getLocalPort());
			s.close();
			s = new ServerSocket(0);
			client2 = new ClientInfo(info.getIp(), s.getLocalPort());
			s.close();
		} catch (IOException e1) {
			
		}
		
		//These objects are used to hold the incoming and outgoing messages of processing and content of a client
		Message clientOneInput = new Message();
		Message clientOneOutput= new Message();
		Message clientTwoInput = new Message();
		Message clientTwoOutput = new Message();
			
		//Create a LAN communicators using the ports obtained above
		ICommunicator communicator1 = new LanCommunicator(client1.getPort());
		ICommunicator communicator2 = new LanCommunicator(client2.getPort());
		 
		//Start the communicators
		communicator1.start();
		communicator2.start();
		//Creates a stopper objects which indicates the status of the communication
		Stopper stopper = new Stopper();
		 
		//Create two clients
		Client clientOne = new Client(communicator1,client1,client2,clientOneInput,clientOneOutput,numMessages,msgLength,stopper);
		Client clientTwo = new Client(communicator2,client2,client1,clientTwoInput,clientTwoOutput,numMessages,msgLength,stopper);
		 
		//Start the two clients
		clientOne.start();
		clientTwo.start();
		 
		//While the communication is happening
		while(true) {
			if(stopper.getStatus()==false) {
				break;
			}
		}
		//Stop the communicators
		communicator1.stop();
		communicator2.stop();
		
		
		//If the incoming and outgoing message of both the clients are equal then return true else return false
		if(clientOneInput.equals(clientTwoOutput) && clientTwoInput.equals(clientOneOutput)) {
			return true;
		}
		return false;
	}

	
}
