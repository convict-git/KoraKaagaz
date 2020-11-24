package infrastructure.content;

/**
 * This interface is given to UI module to interact with content module using four methods.
 * @author Badal Kumar (111701008)
 */

public interface IContentCommunicator {
	/*
	 * UI will call this method with one json formatted string argument
	 * @param userDetails - It is json string which will contain server ipAddress, username, image
	 */
	void initialiseUser(String userDetails);
	
	/*
	 * UI will call this to paas the message of client to everyone else
	 * @param message - it is the actual message as a json string, more fields can be accomodated in future
	 */
	void sendMessageToContent(String message);
	
	/*
	 * UI will call this to notify that the current user is quitting the session to all other users
	 */
	void notifyUserExit();
	
	/*
	 * UI will subscribe to us in order to receive any updates coming from other users
	 * @param identifier - This will be a string and unique too
	 * @param handler - By using this handler, methods of IContentNotificationHandler will be called
	 */
	void subscribeForNotifications(String identifier, IContentNotificationHandler handler);
}
