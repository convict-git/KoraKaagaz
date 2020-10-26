package infrastructure.content;

public interface IContentCommunicator {
	/*
	 * UI will call this method with one json formatted string argument
	 * parameter : userDetails - It is json string which will contain server ip_address, username, image
	 */
	void initialiseUser (String userDetails);
	
	/*
	 * UI will call this to paas the message of client to everyone else
	 * parameter : message - it is the actual message as a string
	 */
	void sendMessageToContent (String message);
	
	/*
	 * UI will call this to notify that the current user is quitting the session to all other users
	 */
	void notifyUserExit ();
	
	/*
	 * UI will subscribe to us in order to receive any updates coming from other users
	 */
    void subscribeForNotifications(String identifier, IContentNotificationHandler handler);    
}
