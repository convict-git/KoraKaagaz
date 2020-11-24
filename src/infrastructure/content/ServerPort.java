package infrastructure.content;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;

/**
 * This class ServerPort implements IServerPort interface and ultimately, the 
 * methods inside it.The port value of board server is locally saved and an another
 * method is defined to provide the port value to other classes of this package.
 * @author Badal Kumar (111701008)
 */

public class ServerPort implements IServerPort {
	
	private int port = 0;
	
	/**
	 * logger is the instance of the class which implements ILogger interface.
	 */
	private ILogger logger = LoggerFactory.getLoggerInstance();
	
	/**
	 * This method will save the port of the board server locally to generate full address of board server.
	 */
	@Override
	public void setPort(int port) {
		this.port = port;
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.SUCCESS,
			"Port of Board Server successfully set"
		);
	}
	
	/**
	 * This method will provide the port to its caller
	 */
	@Override
	public int getPort() {
		if (port == 0) {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.WARNING,
				"Port Value not yet sent by processing module"
			);
		}
		else {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.SUCCESS,
				"Port of Board Server successfully sent"
			);
		}
		return port;
	}
}