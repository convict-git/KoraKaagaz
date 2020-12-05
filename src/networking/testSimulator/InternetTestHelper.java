package networking.testSimulator;


import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.InternetCommunicator;
import networking.utility.ClientInfo;

/**
 * This class is helper class for testing Internet communication
 * @author Sravan
 *
 */
public class InternetTestHelper {
	
	/**
	 * This method is used to run the test for Internet Communication 
	 * for a given number of messages and given message length
	 * 
	 * @param numMessages
	 * @param msgLength
	 * @return boolean, returns whether this ran successfully or not
	 */
	public boolean run(int numMessages, int msgLength) {
		
		//Get info of two clients
		ClientInfo client1 = CommunicatorFactory.getClientInfo();
		ClientInfo client2 = CommunicatorFactory.getClientInfo();
	
		//These objects are used to hold the incoming and outgoing messages of processing and content of a client
		Message clientOneInput = new Message();
		Message clientOneOutput= new Message();
		Message clientTwoInput = new Message();
		Message clientTwoOutput = new Message();
			
		//Create a Internet communicators using the ports obtained above
		ICommunicator communicator1 = new InternetCommunicator(client1.getPort());
		ICommunicator communicator2 = new InternetCommunicator(client2.getPort());
		 
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
		if(clientOneInput.equals(clientTwoOutput) 
			&& clientTwoInput.equals(clientOneOutput)) {
			return true;
		}
		return false;
	}
}
