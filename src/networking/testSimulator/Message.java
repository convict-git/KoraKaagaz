package networking.testSimulator;

import java.util.ArrayList;

public class Message {
	public ArrayList<String> processingMessage= null;
	public ArrayList<String> contentMessage= null;	
	public Message() {
		processingMessage = new ArrayList<String>();
		contentMessage = new ArrayList<String>();
		
	}
	  public boolean equals(Message m) { 
		  if(m.processingMessage.equals(this.processingMessage) && m.contentMessage.equals(this.contentMessage))return true;
	      return false;
	  } 
	
}
