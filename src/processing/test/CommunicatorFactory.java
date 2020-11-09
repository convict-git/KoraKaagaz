package processing.test;

public class CommunicatorFactory {

	private static NetworkCommunicator network = null;
	
	private CommunicatorFactory() {};
	
	public static NetworkCommunicator getCommunicator() {
		if (network == null)
			network = new NetworkCommunicator();
		return network;
	}
}
