package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


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
	
	/**
	 *  
	 */
	protected FileLogger() {
		
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {

		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);
		
		String logTimeStamp = "["+formatDateTime+"]";
		
	}

}
