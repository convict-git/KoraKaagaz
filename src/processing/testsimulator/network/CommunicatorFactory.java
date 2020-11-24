package processing.testsimulator.network;

import infrastructure.validation.logger.*;
/**
 * Factory class of network. Follows the same design pattern as the 
 * Network module. To be used for testing purpose only.
 * 
 * @author Sakshi Rathore
 * 
 */

public class CommunicatorFactory {
	/** Initialize ICommunicator variable **/
	private static Communicator network = null;
	
	private CommunicatorFactory() {};
	
	static ILogger logger = LoggerFactory.getLoggerInstance();
	
	/**
	 * 
	 * @return Communicator(which implements ICommunicator interface) object.
	 * 
	 */
	public static Communicator getCommunicator() {
		
		if (network == null)
			network = new Communicator();
		
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Test: Successfully get the Communicator of the network."
		);
		
		return network;
	}
}
