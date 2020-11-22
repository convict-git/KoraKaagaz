package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * FileLogger class that is part of loggerManager,
 * and used for writing log entries to a log file
 * Implements ILogger interface
 * 
 * @author Navaneeth M Nambiar
 */
public class FileLogger implements ILogger {

	/** stores the format specified for the time-stamp */
	private DateTimeFormatter timeStampFormat;
		
	/** string that holds the location of the log file */
	private static String logFile;
	
	/** boolean that enables/disables ERROR logs, 
	 *  defaults to false (no ERROR logs are logged)
	 */
	private boolean enableErrorLog = false;
	
	/** boolean that enables/disables WARNING logs, 
	 *  defaults to false (no WARNING logs are logged)
	 */
	private boolean enableWarnLog = false;

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
	 *  constructor for FileLogger.
	 *  Protected type since it needs to be only invoked by LoggerManager class
	 *  @see LoggerManager
	 */
	protected FileLogger(List<LogLevel> enabledLogLevelsList, boolean enableTestMode) {
		
		// sets DateTime format as per the spec
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		
		// sets the path to the file
		String logFilePath = "";
		
		try {
			String home = System.getProperty("user.home");
			logFilePath = home+"/.config/";
		} catch (SecurityException se) {
			// in case a security manager is present, it's checkRead method can deny read access to the file
			// if it occurs, logFilePath reverts to the current directory where it is run
			logFilePath = "./.config/";
		}
		
		// sets the logFilename as per the spec
		DateTimeFormatter logFilenameFormat;
		logFilenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		
		String logTimeStamp = "";
		
		try {
			LocalDateTime now = LocalDateTime.now();
			logTimeStamp = now.format(logFilenameFormat);
		} catch (DateTimeException dte) {
			// format method failed due to error that occurred during printing
			// nothing can be done, stick to the default
		}
		
		// set the path to the log file
		logFile = logFilePath+logTimeStamp+"-release.log";
	
		// set testMode to enabled/disabled
		// variable enableTestMode is a boolean which is a primitive data type and,
		// has a default value of false if unassigned
		inTestMode = enableTestMode;
		
		// set booleans that enable/disable Log Level
		if (null == enabledLogLevelsList) {
			// nothing to be done
			// default values will be used
		} else {
			
			// check which LogLevel enum are sent
			// if an enum is available, set the corresponding boolean to true i.e. enabled
			if (enabledLogLevelsList.contains(LogLevel.ERROR)) {
				enableErrorLog = true;
			}
			if (enabledLogLevelsList.contains(LogLevel.WARNING)) {
				enableWarnLog = true;
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

		String formatDateTime = "";
		
		try {
			LocalDateTime now = LocalDateTime.now();
			formatDateTime = now.format(timeStampFormat);
		} catch (DateTimeException dte) {
			// format method failed due to error that occurred during printing
			// nothing can be done, stick to the default
		}
		
		String logTimeStamp = "";
		
		String logModulePart = "";
		
		String logLevelPart = "";
		
		if(!inTestMode) {

			logTimeStamp = "["+formatDateTime+"] ";
			
			logModulePart = "["+moduleIdentifier.toString()+"] ";
			
			logLevelPart = "["+level.toString()+"] ";
		
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
		
		switch(level) {
		case ERROR:
			if(enableErrorLog) {
				writeToFile(logMessage);				
			}
			break;
		case WARNING:
			if(enableWarnLog) {
				writeToFile(logMessage);				
			}
			break;
		case SUCCESS:
			if(enableSuccessLog) {
				writeToFile(logMessage);				
			}
			break;
		case INFO:
			if(enableInfoLog) {
				writeToFile(logMessage);				
			}
			break;
		default:
			// do nothing
			break;
		}
	}

	/** 
	 * Private helper method that returns an object that can write content into a file 
	 * Throws IOException error upon failure which is to be handled by the parent
	 * 
	 * @param filename name of the file which has to be opened
	 * @return object of PrintWriter class that opens the file
	 * @throws IOException
	 */
	private static PrintWriter openFile(String filename) throws IOException {
		
		FileWriter fileWriter = new FileWriter(filename, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		return printWriter;
	}

	/**
	 *  private helper method that opens the logFile, 
	 *  appends the message to the file and closes the file
	 *  Handles IOException errors
	 * 
	 * @param logMessage message string to be written as log
	 */
	private static void writeToFile(String logMessage) {
		
		PrintWriter printWriter = null;
		try {
			printWriter = openFile(logFile);
			printWriter.printf(logMessage);
		} catch (IOException e) {
			// check for console to print to
			// if unavailable, do nothing
			if(System.console() != null) {
				System.err.println(" Caught IOException: " + e.getMessage());
			}
		}
		finally {
			closeFile(printWriter);
		}
	}
	
	/** 
	 * private helper method that is used to properly close a file,
	 * if it is open.
	 *  
	 * @param p object of class PrintWriter that is to be opened
	 */
	private static void closeFile(PrintWriter p) {
		
		if(p != null) {
			p.close();
		}
		else {
			// check for console to print to
			// if unavailable, do nothing
			if(System.console() != null) {
				System.out.println(" PrintWriter not open ");
			}
		}
	}
}
