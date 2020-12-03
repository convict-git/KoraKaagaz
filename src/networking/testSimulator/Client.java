package networking.testSimulator;


import networking.ICommunicator;
import networking.utility.ClientInfo;

public class Client extends Thread {
	ICommunicator communicator=null;
	ClientInfo srcClient=null;
	ClientInfo destClient=null;
	Message  input = null;
	Message output = null; 
	int numMessages;
	int msgLength;
	Stopper stopper = null;
 	public Client(ICommunicator communicator,ClientInfo client1,ClientInfo client2,Message input,Message output,int numMessages,int msgLength,Stopper stopper) {
		this.communicator = communicator;
		this.srcClient = client1;
		this.destClient = client2;
		this.numMessages =numMessages;
		this.msgLength = msgLength;
		this.input = input;
		this.output = output;
		this.stopper = stopper;
	}
	public void run() {
		DummyProcessor processor = new DummyProcessor(communicator,srcClient,destClient,input,output,numMessages,msgLength,stopper);
		DummyContent content = new DummyContent(communicator,srcClient,destClient,input,output,numMessages,msgLength,stopper);
		processor.start();
		content.start();
	}
}
