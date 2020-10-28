package infrastructure.validation.logger;

/**
 * The ILogger interface depends on the LogLevel enum
 * The LogLevel enum is used to specify the type of log used:
 *   1. ERROR   - a significant error has occurred and the program can halt
 *   2. WARNING - a warning that might indicate possible fault
 *   3. SUCCESS - a successful completion of a particular procedure
 *   4. INFO    - an informative message. Common example would be start/end
 *        of a procedure/transaction
 * 
 * @author Navaneeth M Nambiar
 */
public enum LogLevel {
	ERROR,
	WARNING,
	SUCCESS,
	INFO
}