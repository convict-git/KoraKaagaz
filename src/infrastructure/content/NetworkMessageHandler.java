import networking.INotificationHandler;

/*
 * NetworkMessageHandler class includes one method i.e @onMessageReceived()
 */
public class NetworkMessageHandler implements INotificationHandler {
	
	/*
	 * @param: json string message (Meta fields are newUser or message or userExit)
	 * if meta field is newUser, then remaining field would only be username
	 * if meta field is message, then remaining fields would be username, message and time
	 * if meta field is exitUser, then remaining field would only be username
	 * Networking module will be calling this method with json string message to send data to the content module
	 */
	public void onMessageReceived(String message) {
		
	}
}
