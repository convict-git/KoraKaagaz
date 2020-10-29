package infrastructure.validation.logger;

/**
 * Interface for interacting with the Logger instance
 * @author Navaneeth M Nambiar
 */
public interface ILogger {
	
	/**
	 * log method will be be used to specify to the Logger instance to log, 
	 * the particular event
	 * 
	 * @param moduleIdentifier the module which is calling the logger. @see logger.ModuleID
	 * @param level            the level of the log to be used. @see logger.LogLevel
	 * @param message          message to be printed in the log
	 */
	void log(ModuleID moduleIdentifier, LogLevel level, String message);
}