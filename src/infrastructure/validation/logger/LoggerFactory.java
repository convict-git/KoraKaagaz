package infrastructure.validation.logger;

/**
 * LoggerFactory class follows the factory pattern
 * The method getLoggerInstance() returns a singleton reference to the object,
 * that implements ILogger interface and is to used for logging
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerFactory {

	/**
	 * singleton object that will be used for logging
	 * accessible only using getLoggerInstance()
	 */
	private static ILogger loggerManager;
	
	/**
	 * Private constructor
	 * Clients shouldn't directly instantiate another LoggerFactory
	 * loggerManager should be made available only via getLoggerInstance()
	 */
	private LoggerFactory() {}

	/**
	 * Returns a reference to ILogger object of LoggerManager class 
	 * If not already instantiated, create a new instance
	 *  
	 * @return object implementing ILogger interface
	 */
	synchronized public static ILogger getLoggerInstance() {
		if(loggerManager == null) {
			loggerManager = new LoggerManager();
		}
		
		return loggerManager;
	}
}