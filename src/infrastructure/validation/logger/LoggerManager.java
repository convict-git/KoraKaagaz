package infrastructure.validation.logger;

// import java.time.format.DateTimeFormatter;
// import java.time.LocalDateTime;

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
	
	/** stores the format specified for the time-stamp */
	// private DateTimeFormatter timeStampFormat;
	
	private boolean allowFileLogging = false;
	
	private boolean allowConsoleLogging = false;
	
	protected LoggerManager() {
		
		fileLogger = new FileLogger();
		consoleLogger = new ConsoleLogger();
		
		// timeStampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		// LocalDateTime now = LocalDateTime.now();
		// String formatDateTime = now.format(timeStampFormat);
		
		// String logMessage = "["+formatDateTime+"]";
		
		fileLogger.log(moduleIdentifier, level, message);			
		
		consoleLogger.log(moduleIdentifier, level, message);
		
	}

}