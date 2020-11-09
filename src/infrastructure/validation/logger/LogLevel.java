package infrastructure.validation.logger;

/**
 * The ILogger interface depends on the LogLevel enum
 * The LogLevel enum is used to specify the type of log used
 * 
 * @author Navaneeth M Nambiar
 */
public enum LogLevel {
	
	/** ERROR is a significant error has occurred and the program can halt */
	ERROR,
	
	/** WARNING is a warning that might indicate possible fault */
	WARNING,
	
	/** SUCCESS is a successful completion of a particular procedure */
	SUCCESS,
	
	/** INFO is an informative message. Eg: start/end of a procedure/transaction */
	INFO
}