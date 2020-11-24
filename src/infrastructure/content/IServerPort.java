package infrastructure.content;

/**
 * This Interface is used for sending port of board server to content module.
 * Moreover, that port value can be accessed too with a separate method. 
 * @author Badal Kumar (111701008)
 */
public interface IServerPort {
	/**
	 * The processing module calls this method to set port of board server on every client.
	 * After the port is set, client can generate the full address of board server.
	 * @param port - This is the port of board server in int datatype
	 */
	void setPort(int port);
	
	/**
	 * This method will be used by content module itself to fetch the port value of server.
	 * @return server's port value as integer
	 */
	int getPort();
}