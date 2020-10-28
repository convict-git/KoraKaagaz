/**
 * 
 */
package infrastructure.validation.logger;

/**
 * LoggerFactory class provides the method getLoggerInstance()
 * This method will return a singleton reference that can be used for logging
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerFactory {

	/**
	 *  private instance. To be accessed only via getLoggerInstance()
	 */
	private static ILogger loggerManager;
	
	/**
	 * 
	 */
	private LoggerFactory() {
		// TODO Auto-generated constructor stub
	}

	synchronized public static ILogger getLoggerInstance() {
		if(loggerManager == null) {
			loggerManager = new LoggerManager();
		}
		
		return loggerManager;
	}
}