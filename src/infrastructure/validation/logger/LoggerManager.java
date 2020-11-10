package infrastructure.validation.logger;

import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.namespace.QName;


/**
 * LoggerManager class that will be referenced by other modules,
 * for logging via the LoggerFactory
 * Implements ILogger interface and composes File and Console Loggers. 
 * 
 * Instantiation of FileLogger and ConsoleLogger indicated by,
 * allowFileLogging and allowConsoleLogging,
 * with defaults false, false.
 * 
 * @author Navaneeth M Nambiar
 */
public class LoggerManager implements ILogger {

	/** path to config file */
	private String loggerConfigFilePath = "resources/infrastructure_logger.xml";
	
	/** object that holds an instance of FileLogger */
	private ILogger fileLogger;

	/** object that holds an instance of ConsoleLogger */
	private ILogger consoleLogger;
	
	/** 
	 * boolean value indicating whether File Logger is created or not
	 * defaults to false 
	 */
	private boolean allowFileLogging = false;
	
	/** 
	 * boolean value indicating whether Console Logger is to be created or not
	 * defaults to False 
	 */
	private boolean allowConsoleLogging = false;
	
	protected LoggerManager() {
		
		List<ModuleID> enabledLogLevelsList = parse(loggerConfigFilePath);
		
		if(allowFileLogging) {
			fileLogger = new FileLogger();
		}
		
		if(allowConsoleLogging) {
			consoleLogger = new ConsoleLogger();
		}
	}

	private List<ModuleID> parse(String filePath) {
		
		List<ModuleID> enabledLogLevelsList = new ArrayList<ModuleID>();
		
		return enabledLogLevelsList;
	}

	@Override
	synchronized public void log(ModuleID moduleIdentifier, LogLevel level, String message) {
		
		if(allowFileLogging) {
			fileLogger.log(moduleIdentifier, level, message);			
		}
		
		if(allowConsoleLogging) {
			consoleLogger.log(moduleIdentifier, level, message);
		}
		
	}

}