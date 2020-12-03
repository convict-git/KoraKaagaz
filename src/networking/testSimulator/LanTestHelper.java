package networking.testSimulator;



import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;

public class LanTestHelper  {
	
	public static int over=0;
	public boolean run(int numMessages,int msgLength) {
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
		Message clientOneInput = new Message();
		Message clientOneOutput= new Message();
		Message clientTwoInput = new Message();
		Message clientTwoOutput = new Message();
		

		 ICommunicator communicator1 = new LanCommunicator(client1.getPort());
		 ICommunicator communicator2 = new LanCommunicator(client2.getPort());
		 communicator1.start();
		 communicator2.start();
		 Stopper stopper = new Stopper();
		 Client clientOne = new Client(communicator1,client1,client2,clientOneInput,clientOneOutput,numMessages,msgLength,stopper);
		 Client clientTwo = new Client(communicator2,client2,client1,clientTwoInput,clientTwoOutput,numMessages,msgLength,stopper);
		 
		 clientOne.start();
		 clientTwo.start();
			while(true) {
				if(stopper.getStatus()==false) {
					break;
				}
			}
			communicator1.stop();
			communicator2.stop();
			
			if(clientOneInput.equals(clientTwoOutput) && clientTwoInput.equals(clientOneOutput)) {
				return true;
			}
		return false;
	}

	
}
