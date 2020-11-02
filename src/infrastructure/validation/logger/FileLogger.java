package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * FileLogger class that is part of loggerManager,
 * and used for logging to the a file (the log file)
 * Implements ILogger interface
 * 
 * @author Navaneeth M Nambiar
 */
public class FileLogger implements ILogger {

	/** stores the format specified for the time-stamp */
	private DateTimeFormatter timeStampFormat;
		
	/** string that holds the location of the log file */
	private static String logFile;
	
	/**
	 *  
	 */
	protected FileLogger() {
		
		// sets DateTime format as per the spec
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		
		// sets the path to the file
		String home = System.getProperty("user.home");
		String logFilePath = home+"/.config/";
		
		// sets the logFilename as per the spec
		DateTimeFormatter logFilenameFormat;
		logFilenameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		
		LocalDateTime now = LocalDateTime.now();
		String logTimeStamp = now.format(logFilenameFormat);
		
		logFile = logFilePath+logTimeStamp+"-release.log";
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {

		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);
		
		String logTimeStamp = "["+formatDateTime+"] ";
		
		String logModulePart = "["+moduleIdentifier.toString()+"] ";
		
		String logLevelPart = "["+level.toString()+"] ";
		
		String logMessage = logTimeStamp+logModulePart+logLevelPart+message;

		writeToFile(logMessage);
	}

	private static PrintWriter openFile(String filename) throws IOException {
		
		FileWriter fileWriter = new FileWriter(filename, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		return printWriter;
	}

	private static void writeToFile(String logMessage) {
		
		PrintWriter printWriter = null;
		try {
			printWriter = openFile(logFile);
			printWriter.printf(logMessage);
		} catch (IOException e) {
			System.err.println(" Caught IOException: " + e.getMessage());
		}
		finally {
			closeFile(printWriter);
		}
	}
	
	private static void closeFile(PrintWriter p) {
		
		if( p != null) {
				p.close();
		}
		else {
			System.out.println(" PrintWriter not open ");
		}
	}
}
