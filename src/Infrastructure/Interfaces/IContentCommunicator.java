package contentManager;

public interface IContentCommunicator {
	void initialiseUser (String json_new);
	void sendMessageToContent (String json_message);
	void notifyUserExit ();
    void subscribeForNotifications(String identifier, IContentNotification handler);
    
}