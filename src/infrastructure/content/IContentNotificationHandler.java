package infrastructure.content;

public interface IContentNotificationHandler {
	/*
	 * Content will call this method with one json formatted string argument to pass the username of new user joined
	 * @param : username - It is a json string whose meta field is username of a new user
	 */
	void onNewUserJoined (String username);
	
	/*
	 * Content will call this method with one json formatted string argument to pass the details to UI of every clients
	 * @param : messageDetails - It is a json string whose meta fields are username (identifer of the user who sent the message), message (actual message sent by user), image (profile picture of user who sent the message) and time (the time the user sent this message) 
	 */
	void onMessageReceived (String messageDetails);
	
	/*
	 * Content will call this method with one json formatted string argument to pass the username
	 * @param : username - It is a json string whose meta field is username of a user who want to exit
	 */
	void onUserExit (String username);
}
