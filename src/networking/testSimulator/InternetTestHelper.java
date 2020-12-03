package networking.testSimulator;



import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.InternetCommunicator;
import networking.LanCommunicator;
import networking.utility.ClientInfo;

public class InternetTestHelper {
	
	public boolean run(int numMessages, int msgLength) {
		ClientInfo client1 = CommunicatorFactory.getClientInfo();
		ClientInfo client2 = CommunicatorFactory.getClientInfo();
		Message clientOneInput = new Message();
		Message clientOneOutput= new Message();
		Message clientTwoInput = new Message();
		Message clientTwoOutput = new Message();
		
		 ICommunicator communicator1 = new InternetCommunicator(client1.getPort());
		 ICommunicator communicator2 = new InternetCommunicator(client2.getPort());
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
