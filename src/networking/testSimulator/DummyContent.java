package networking.testSimulator;


import java.util.Random;
import networking.ICommunicator;
import networking.utility.ClientInfo;


/**
 * This Class represents the DummyContentManager 
 * @author Dasari Sravan Kumar
 */
public class DummyContent extends Thread {
	ICommunicator communicator=null;
	ClientInfo srcClient=null;
	ClientInfo destClient=null;
	Message input = null;
	Message output = null;
	private int numMsgs;
	private int msgLength;
	
	/**
	 * @param msgLength, Length of string to be generated
	 * @return String, randomly generated string of alphabets of length  msgLength
	 */
	public static String randomString(int msgLength) {
		int leftLimit = 97; 
	    int rightLimit = 122;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(msgLength);
	    for (int i = 0; i < msgLength; i++) {
	        int asciiValue = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) asciiValue);
	    }
	    String generatedString = buffer.toString();	    
	    return(generatedString);
	}

	public DummyContent(
			ICommunicator communicator, 
			ClientInfo src,
			ClientInfo dest,
			Message input,
			Message output,
			int numMessages,
			int msgLength,
			Stopper stopper
		) {
		this.srcClient = src;
		this.destClient = dest;
		this.communicator = communicator;
		this.input = input;
		this.output = output;
		this.numMsgs = numMessages;
		this.msgLength = msgLength;
		DummyContentHandler handler = new DummyContentHandler(this.output,numMsgs,stopper);
		communicator.subscribeForNotifications("content", handler);
	}
	/**
	 * Driver code of this thread
	 */
	@Override
	public void run() {
		/** Send numMsgs messages of length msgLength to the destination client */
		for (int i = 0; i < numMsgs; i++) {
			String message = randomString(this.msgLength);
			communicator.send(destClient.getIp()+":"+destClient.getPort(), message, "content");
			
			input.contentMessage.add(message);
		 }
		
	}
	
	
}
