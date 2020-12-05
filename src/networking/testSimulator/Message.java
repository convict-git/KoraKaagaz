package networking.testSimulator;

import java.util.ArrayList;

public class Message {
	//Array to store processingMessages
	public ArrayList<String> processingMessage= null;
	//Array to store contentMessages
	public ArrayList<String> contentMessage= null;	
	
	public Message() {
		processingMessage = new ArrayList<String>();
		contentMessage = new ArrayList<String>();	
	}
	
	/**'
	 * Method to check If two Message object are equal or not
	 * @param m, a message object
	 * @return boolean, returns if the param object is equal to current object or not
	 */
	public boolean equals(Message m) { 
		  
		  if(m.processingMessage.equals(this.processingMessage) && 
		     m.contentMessage.equals(this.contentMessage)){
		  	return true;
		  }
		  
	      return false;
	} 
	
}
