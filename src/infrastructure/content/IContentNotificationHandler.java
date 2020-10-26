package infrastructure.content;

public interface IContentNotificationHandler {
	// Meta field is username of a new user
	void onNewUserJoined (String username);
	
	// Meta fields of JSON file messageDetails are userid, message and image
	void onMessageReceived (String messageDetails);
	
	// Meta field is username of a new user
	void onUserExit (String username);
}
