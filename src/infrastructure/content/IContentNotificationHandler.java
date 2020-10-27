package infrastructure.content;

public interface IContentNotificationHandler {
	/*
	 * Content will call this method with one json formatted string argument to pass the username of new user joined
	 * @param : username - It is a json string whose meta field is username of a new user
	 */
	void onNewUserJoined (String username);
	
	/*
	 * Content will call this method with one json formatted string argument to pass the details to UI of every clients
	 * @param : messageDetails - It is a json string whose meta fields are userid, message and image
	 */
	void onMessageReceived (String messageDetails);
	
	/*
	 * Content will call this method with one json formatted string argument to pass the username
	 * @param : username - It is a json string whose meta field is username of a user who want to exit
	 */
	void onUserExit (String username);
}
