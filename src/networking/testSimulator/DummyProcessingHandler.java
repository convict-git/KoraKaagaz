package networking.testSimulator;


import networking.INotificationHandler;
/**
 * This class represents dummy Processing Handler
 * @author sravan
 *
 */
public class DummyProcessingHandler implements INotificationHandler {
	Message output;
	int numMessages;
	int numMessagesReceived;
	private Stopper stopper=  null;
	public DummyProcessingHandler(
			Message output,
			int numMsgs,
			Stopper stopper
	) {
		this.output= output;
		this.numMessages = numMsgs;
		this.numMessagesReceived=0;
		this.stopper = stopper;
	}
	@Override
	public void onMessageReceived(String message) {
		this.numMessagesReceived++;
		output.processingMessage.add(message);
		if(this.numMessagesReceived ==this.numMessages ) {
			this.stopper.incrementStop();
		}
		
	}
	
}
