package infrastructure.content;

public final class ContentFactory {
	
	private static IContentCommunicator instance1;
	private static IServerPort instance2;
	
	private ContentFactory() {}
	
	public static IContentCommunicator getContentCommunicator() {
		if (instance1 == null) {
			instance1 = new ContentCommunicator();
		}
		return instance1;
	}
	
	public static IServerPort getServerPort() {
		if (instance2 == null) {
			instance2 = new ServerPort();
		}
		return instance2;
	}
}