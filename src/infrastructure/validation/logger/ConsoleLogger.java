package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.util.List;
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
	
	/** boolean that enables/disables ERROR logs, 
	 *  defaults to false (no ERROR logs are logged)
	 */
	private boolean enableErrorLog = false;
	
	/** boolean that enables/disables WARNING logs, 
	 *  defaults to false (no WARNING logs are logged)
	 */
	private boolean enableWarningLog = false;

	/** boolean that enables/disables SUCCESS logs, 
	 *  defaults to false (no SUCCESS logs are logged)
	 */
	private boolean enableSuccessLog = false;

	/** boolean that enables/disables INFO logs, 
	 *  defaults to false (no INFO logs are logged)
	 */
	private boolean enableInfoLog = false;

	/**
	 *  boolean to enable/disable testMode while logging
	 *  If in testMode, logs won't have module or timestamp information attached with it. 
	 */
	private boolean inTestMode = false;
	
	/**
	 *  Creates an object that logs to the console, if enabled and available
	 */
	protected ConsoleLogger(List<LogLevel> enabledLogLevelsList, boolean enableTestMode) {
		
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		
		console = System.console();
		
		// set testMode to enabled/disabled
		// variable enableTestMode is a boolean which is a primitive data type and,
		// has a default value of false if unassigned
		inTestMode = enableTestMode;
		
		if(null == console) {
			
			// no console to print found
			// disable all the log levels i.e. have no messages logged
			enableErrorLog = false;
			enableWarningLog = false;
			enableSuccessLog = false;
			enableInfoLog = false;
		
		} else if (null == enabledLogLevelsList) {
		
			// no info specified
			// use default value
			enableErrorLog = false;
			enableWarningLog = false;
			enableSuccessLog = false;
			enableInfoLog = false;
		
		} else {
			
			// check the List to see which LogLevel enum are sent
			// for every member of the List, set the corresponding boolean to true i.e. enabled
			if (enabledLogLevelsList.contains(LogLevel.ERROR)) {
				enableErrorLog = true;
			}
			if (enabledLogLevelsList.contains(LogLevel.WARNING)) {
				enableWarningLog = true;
			}
			if (enabledLogLevelsList.contains(LogLevel.SUCCESS)) {
				enableSuccessLog = true;
			}
			if (enabledLogLevelsList.contains(LogLevel.INFO)) {
				enableInfoLog = true;
			}
			
		}
		
	}

	@Override
	synchronized public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);

		String logTimeStamp = "";

		String logModulePart = "";
		
		String logLevelPart = "";
		
		if(!inTestMode) {

			logTimeStamp = "["+formatDateTime+"]";
			
			logModulePart = "["+moduleIdentifier.toString()+"]";
			
			logLevelPart = "["+level.toString()+"]";
	
		} else {
			
			/**
			 *  logger is in testMode
			 *  as per infrastructure lead and test harness requirement,
			 *  disable logs from all other modules
			 */			
			switch(moduleIdentifier) {
			case TEST:
				break;
			default:
				return;
			}
		}
		
		String logMessage = logTimeStamp+logModulePart+logLevelPart+message;

		printToConsole(level, logMessage);
	}

	/**
	 *  private helper method that can print the log messages to Console,
	 *  if the corresponding log level is enabled
	 *  
	 * @param level LogLevel enum that specifies the kind of log message
	 * @param message String that holds the value to be printed to console
	 */
	private void printToConsole(LogLevel level, String message) {
		
		String ANSI_COLOR = getColorFromLevel(level);
		
		switch(level) {
		case ERROR:
			if(enableErrorLog) {
				System.out.println(ANSI_COLOR+message+ANSI_RESET);
			}
			break;
		case WARNING:
			if(enableWarningLog) {
				System.out.println(ANSI_COLOR+message+ANSI_RESET);
			}
			break;
		case SUCCESS:
			if(enableSuccessLog) {
				System.out.println(ANSI_COLOR+message+ANSI_RESET);
			}
			break;
		case INFO:
			if(enableInfoLog) {
				System.out.println(ANSI_COLOR+message+ANSI_RESET);
			}
			break;
		}

	}
	
	/**
	 *  private helper method to match the color with the log level
	 * @param level LogLevel enum that specifies the type of log
	 * @return String corresponding to the ANSI color that is set for that particular color
	 */
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
