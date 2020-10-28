package infrastructure.validation.logger;

/**
 * The ILogger interface depends on the ModuleID enum
 * The ModuleID enum helps specify which module is calling the log
 * Currently supported module names are:
 *   NETWORKING     - the Networking module
 *   PROCESSING     - the Processing module
 *   UI             - the UI module
 *   INFRASTRUCTURE - the Infrastructure module
 *   
 * @author Navaneeth M Nambiar
 */

public enum ModuleID {
	NETWORKING,
	PROCESSING,
	UI,
	INFRASTRUCTURE
}