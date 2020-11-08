package infrastructure.validation.logger;

/**
 * LoggerManager class that will be referenced by other modules,
 * for logging via the LoggerFactory
 * Implements ILogger interface and composes File and Console Loggers. 
 * 
 * Instantiation of FileLogger and ConsoleLogger indicated by,
 * allowFileLogging and allowConsoleLogging,
 * with defaults false, false.
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerManager implements ILogger {

	/** object that holds an instance of FileLogger */
	private ILogger fileLogger;

	/** object that holds an instance of ConsoleLogger */
	private ILogger consoleLogger;
	
	/** 
	 * boolean value indicating whether File Logger is created or not
	 * defaults to false 
	 */
	private boolean allowFileLogging = false;
	
	/** 
	 * boolean value indicating whether Console Logger is to be created or not
	 * defaults to False 
	 */
	private boolean allowConsoleLogging = false;
	
	protected LoggerManager() {
		
		if(allowFileLogging) {
			fileLogger = new FileLogger();
		}
		
		if(allowConsoleLogging) {
			consoleLogger = new ConsoleLogger();
		}
	}

	@Override
	synchronized public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		if(allowFileLogging) {
			fileLogger.log(moduleIdentifier, level, message);			
		}
		
		if(allowConsoleLogging) {
			consoleLogger.log(moduleIdentifier, level, message);
		}
		
	}

}