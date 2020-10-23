package contentManager;

public class ContentCommunicator implements IContentCommunicator{
    
	public void initialiseUser(String json_new) {
    	//UI will call this to send any message to server
		//this method will be implemented by content team.
    }
	
	public void sendMessageToContent(String json_message) {
    	//UI will call this to send any message to server
		//this method will be implemented by content team.
    }
	
	public void notifyUserExit() {
    	//UI will call this to send any message to server
		//this method will be implemented by content team.
    }
    
	public void subscribeForNotifications(String identifier, IContentNotification handler) {
		//this method will be implemented by content team.
    }

}