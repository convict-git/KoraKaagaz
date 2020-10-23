package processing.server.board;

/***
 * This is a factory class. Its function getDetailsHandler() will be called by the
 * content team so that they can use the function getClientDetails defined in the 
 * IClientDetails interface.
 * 
 * @author recursed
 *
 */

public class ServerFactory {
	
	public static ClientIP ins = new ClientIP();
	
	private ServerFactory() {};

	public static ClientIP getIPHandler() {
		return ins;
	}
	
}
