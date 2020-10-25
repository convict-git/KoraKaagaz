package content;

public final class ContentFactory {
	
    public static IContentCommunicator getContentCommunicator() {
        IContentCommunicator communicator = new contentCommunicator();
        return communicator;
    }
}