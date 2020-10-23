package content;

public interface IContentCommunicator {
	void initialiseUser (String userDetails);
	void sendMessageToContent (String message);
	void notifyUserExit ();
    void subscribeForNotifications(String identifier, IContentNotification handler);
    
}
