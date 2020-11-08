package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.Console;

/**
 * ConsoleLogger class that is part of loggerManager,
 * and used for logging to the command line
 * Implements ILogger interface
 * 
 * @author Navaneeth M Nambiar
 */
public class ConsoleLogger implements ILogger {

	/** stores the format specified for the time-stamp */
	private DateTimeFormatter timeStampFormat;
	
	/** stores a reference to the console object 
	 *  If null, then no logs should be enabled 
	 */
	private static Console console;
	
	/** ANSI escape codes for using color in output */
	
	// reset to return to using console default
	public static final String ANSI_RESET = "\u001B[0m";
	
	// ERROR log level with color RED
	public static final String ANSI_RED = "\u001B[31m";
	
	// WARNING log level with color YELLOW
	public static final String ANSI_YELLOW = "\u001B[33m";
	
	// SUCCESS log level with color GREEN
	public static final String ANSI_GREEN = "\u001B[32m";
	
	/**
	 *  Creates an object that logs to the console, if enabled and available
	 */
	protected ConsoleLogger() {
		
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		
		console = System.console();
		
		if(console == null) {
			// Houston, we need to disable all the log level filters and have no messages
		}
	}

	@Override
	synchronized public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);

		String logTimeStamp = "["+formatDateTime+"]";

		String logModulePart = "["+moduleIdentifier.toString()+"]";
		
		String logLevelPart = "["+level.toString()+"]";
		
		String logMessage = logTimeStamp+logModulePart+logLevelPart+message;

		printToConsole(level, logMessage);
	}

	private void printToConsole(LogLevel level, String message) {
		
		if(console != null) {
			
			String ANSI_COLOR = getColorFromLevel(level);
			
			System.out.println(ANSI_COLOR+message+ANSI_RESET);
			
		}
	}
	
	private String getColorFromLevel(LogLevel level) {
		
		String ANSI_COLOR;
		
		switch(level) {
		case ERROR:
			ANSI_COLOR = ANSI_RED;
			break;
		case WARNING:
			ANSI_COLOR = ANSI_YELLOW;
			break;
		case SUCCESS:
			ANSI_COLOR = ANSI_GREEN;
			break;
		default:
			ANSI_COLOR = ANSI_RESET;
		}
		
		return ANSI_COLOR;
	}
}
