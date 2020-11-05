package infrastructure.validation.logger;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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
	
	/**
	 * 
	 */
	protected ConsoleLogger() {
		
		timeStampFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(timeStampFormat);

		String logTimeStamp = "["+formatDateTime+"]";

		String logModulePart = "["+moduleIdentifier.toString()+"]";
		
		String logLevelPart = "["+level.toString()+"]";
		
		String logMessage = logTimeStamp+logModulePart+logLevelPart+message;

	}

}
