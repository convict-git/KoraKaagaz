package infrastructure.validation.logger;

/**
 * LoggerManager class that will be referenced by other modules,
 * for logging via the LoggerFactory
 * Implements ILogger interface and composes File and Console Loggers
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerManager implements ILogger {

	/** object that holds an instance of FileLogger */
	private ILogger fileLogger;

	/** object that holds an instance of ConsoleLogger */
	private ILogger consoleLogger;
	
	private boolean allowFileLogging = false;
	
	private boolean allowConsoleLogging = false;
	
	protected LoggerManager() {
		
		fileLogger = new FileLogger();
		consoleLogger = new ConsoleLogger();
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		fileLogger.log(moduleIdentifier, level, message);			
		
		consoleLogger.log(moduleIdentifier, level, message);
		
	}

}