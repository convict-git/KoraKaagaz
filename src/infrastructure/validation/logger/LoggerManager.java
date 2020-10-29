package infrastructure.validation.logger;

/**
 * LoggerManager class that will be referenced by other modules,
 * for logging via the LoggerFactory
 * Implements ILogger interface and composes File and Console Loggers
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerManager implements ILogger {

	protected LoggerManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		// TODO Auto-generated method stub

	}

}