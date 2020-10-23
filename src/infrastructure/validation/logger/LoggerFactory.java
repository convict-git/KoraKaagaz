/**
 * 
 */
package infrastructure.validation.logger;

/**
 * @author Navaneeth M Nambiar
 *
 */
public class LoggerFactory {

	/**
	 *  private instance. To be accessed only via getLoggerInstance()
	 */
	private static ILogger loggerManager;
	
	/**
	 * 
	 */
	public LoggerFactory() {
		// TODO Auto-generated constructor stub
	}

	synchronized public static ILogger getLoggerInstance() {
		if(loggerManager == null) {
			loggerManager = new LoggerManager();
		}
		
		return loggerManager;
	}
}