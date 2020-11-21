package infrastructure.content;

import infrastructure.content.server.ContentServer;

/**
 * This is a factory class of content module which contains instantiation of
 * ContentCommunicator class and ServerPort class in two different methods from
 * which other modules can get the object of those classes.
 * @author Badal Kumar (111701008)
 */
public final class ContentFactory {
	
	private static IContentCommunicator contentCommunicator;
	private static IServerPort serverPort;
	private static ContentServer contentServer;
	
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
	 * This method will create an instance of ContentServer class and returns it.
	 * @return contentServer - An instance of ContentServer class
	 */
	public static ContentServer getContentServer() {
		if (contentServer == null) {
			contentServer = new ContentServer();
		}
		return contentServer;
	}
}