package infrastructure.content;

public interface IServerPort {
	/*
	 * the processing module calls this method to send port of board server to each new client
	 * parameters : port, which is the port of board server in int datatype
	 */
	void sendPort(int port);

}