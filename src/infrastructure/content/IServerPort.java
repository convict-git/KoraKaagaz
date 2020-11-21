package infrastructure.content;

/**
 * This Interface is used for sending port of board server to content module.
 * Moreover, that port value can be accessed too with a separate method. 
 * @author Badal Kumar (111701008)
 */
public interface IServerPort {
	/**
	 * the processing module calls this method to send port of board server to each new client
	 * @param port - This is the port of board server in int datatype
	 */
	void sendPort(int port);
	
	/**
	 * This method will be used by content module itself to fetch the port value of server.
	 * @return server's port value as integer
	 */
	int getPort();
}