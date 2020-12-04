package networking.testSimulator;


import networking.INotificationHandler;
/**
 * This class represents DummyContentHandler which notifies of any message to DummyContent
 * @author Dasari sravan Kumar
 *
 */
public class DummyContentHandler implements INotificationHandler {
	Message output;
	int numMessages;
	int numMessagesReceived;
	Stopper stopper= null;
	public DummyContentHandler(
			Message output,
			int numMsgs,
			Stopper stopper
		) {
		this.output = output;
		this.numMessages = numMsgs;
		this.numMessagesReceived = 0;
		this.stopper = stopper;
	}
	@Override
	public void onMessageReceived(String message) {
		this.numMessagesReceived++;
		output.contentMessage.add(message);
		if(this.numMessagesReceived ==this.numMessages ) {
			this.stopper.incrementStop();
		}
	}
	
}
