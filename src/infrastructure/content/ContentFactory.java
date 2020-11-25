package infrastructure.content;

import infrastructure.content.server.ContentServer;
import networking.CommunicatorFactory;
import networking.ICommunicator;

/**
 * This is a factory class of content module which contains instantiation of
 * ContentCommunicator class and ServerPort class in separate methods from which
 * other modules can get the object of those classes. There is also instantiation
 * of ContentServer and it is used for subscribing to networking module.
 * @author Badal Kumar (111701008)
 */
public final class ContentFactory {
	
	private static IContentCommunicator contentCommunicator;
	private static IServerPort serverPort;
	private static ContentServer contentServer;
	
	/**
	 * This variable will store the communicator of networking module to subscribe for ContentServer.
	 */
	private static ICommunicator communicator = CommunicatorFactory.getCommunicator(0);
	
	/**
	 * A private constructor so that another instance of ContentFactory can't be created.
	 */
	private ContentFactory() {}
	
	/**
	 * This method will create an instance of ContentCommunicator class and returns it.
	 * @return contentCommunicator - An instance of ContentCommunicator class
	 */
	public static IContentCommunicator getContentCommunicator() {
		if (contentCommunicator == null) {
			contentCommunicator = new ContentCommunicator();
		}
		return contentCommunicator;
	}
	
	/**
	 * This method will create an instance of ServerPort class and returns it.
	 * @return serverPort - An instance of ServerPort class
	 */
	public static IServerPort getServerPort() {
		if (serverPort == null) {
			serverPort = new ServerPort();
		}
		return serverPort;
	}
	
	/**
	 * This method will create an instance of ContentServer class and subscribe it to networking.
	 */
	public static void startContentServer() {
		if (contentServer == null) {
			contentServer = new ContentServer();
		}
		communicator.subscribeForNotifications("content", contentServer);
	}
}