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
	
	/** string that holds the file name */
	private String filename;
	
	/**
	 *  
	 */
	protected FileLogger() {
		
		// set up DateTime format
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {

		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);
		
		String logTimeStamp = "["+formatDateTime+"] ";
		
		String logModulePart = "["+moduleIdentifier.toString()+"] ";
		
		String logLevelPart = "["+level.toString()+"] ";
		
		String logMessage = logTimeStamp+logModulePart+logLevelPart+message;

	}

	private static PrintWriter openFile(String filename) throws IOException {
		
		FileWriter fileWriter = new FileWriter(filename);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		return printWriter;
	}
	
	private static void closeFile(PrintWriter p) {
		
		try {
			if( p != null) {
				p.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
