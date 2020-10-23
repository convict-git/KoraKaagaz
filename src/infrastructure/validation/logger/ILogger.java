/**
 * 
 */
package infrastructure.validation.logger;

/**
 * @author Navaneeth M Nambiar
 */
public interface ILogger {
	/**
	 * 
	 * @param moduleIdentifier
	 * @param level
	 * @param message
	 */
	void log(ModuleID moduleIdentifier, LogLevel level, String message);
}