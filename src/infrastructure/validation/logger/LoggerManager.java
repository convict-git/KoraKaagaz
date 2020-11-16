package infrastructure.validation.logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
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
	
	/**
	 * constructor for LoggerManager class
	 */
	protected LoggerManager() {
		
		File logConfigFile = new File(loggerConfigFilePath);
		List<LogLevel> enabledLogLevelsList = null;
				
		if(logConfigFile.isFile()) {
			
			String fileToParse = configFileToParse(loggerConfigFilePath);
			
			File configFileToParse = new File(fileToParse);
			
			if(configFileToParse.isFile()) {
				enabledLogLevelsList = parse(fileToParse);
			} else {
				enabledLogLevelsList = parse(loggerConfigFilePath);
			}
			
		}
		
		if(allowFileLogging) {
			fileLogger = new FileLogger(enabledLogLevelsList);
		}
		
		if(allowConsoleLogging) {
			consoleLogger = new ConsoleLogger(enabledLogLevelsList);
		}
	}
	
	/** 
	 *  helper method that checks whether the logger is to be initialized in test mode,
	 *  works by the testMode tag in the default XML file
	 *  If no such tag exists, then the file is considered to be not in test mode and so,
	 *  the default configuration XML file itself is used.
	 * 
	 * @param defaultFilePath location of the default XML file configuration
	 * @return a string that will be the location of the XML configuration file to be parsed further
	 */
	private String configFileToParse(String defaultFilePath) {
		
		String fileToParse = defaultFilePath;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		try {
			
			XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(defaultFilePath));
			while(reader.hasNext()) {
				
				XMLEvent nextEvent = reader.nextEvent();
				if(nextEvent.isStartElement()) {
					StartElement startElement = nextEvent.asStartElement();
					if(startElement.getName().getLocalPart().equalsIgnoreCase("testMode")) {
						Attribute filePath = startElement.getAttributeByName(new QName("filePath"));
						fileToParse = filePath.getValue();
					}
				}
			}
		} catch (XMLStreamException xse) {
			// do nothing and skip to default values
		} catch (FileNotFoundException fnfe) {
			// do nothing and skip to default values
		}
		
		return fileToParse;
	}

	/**
	 *  helper method to parse an XML file to set the various settings in the logging framework
	 *  settings include enabling/disabling file or console logger, enabling/disabling log levels globally
	 *  
	 * @param filePath String pointing to path of the XML file.
	 * @return list of LogLevel enums. Members of this List correspond to LogLevels to be enabled. 
	 */
	private List<LogLevel> parse(String filePath) {
		
		List<LogLevel> enabledLogLevelsList = new ArrayList<LogLevel>();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
		boolean isInLoggerOptions = false;
		boolean isInLogLevels = false;
		
		try {
			
			XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
			while(reader.hasNext()) {
				
				XMLEvent nextEvent = reader.nextEvent();
				if(nextEvent.isStartElement()) {
					StartElement startElement = nextEvent.asStartElement();
					switch(startElement.getName().getLocalPart()) {
					case "loggerOptions":
						isInLoggerOptions = true;
						break;
					case "loggerOption":
						
						if(!isInLoggerOptions) {
							break;
						}
						
						Attribute loggerTypeFile = startElement.getAttributeByName(new QName("FileLogger"));
						if(loggerTypeFile != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								allowFileLogging = true;
							}
						}
						
						Attribute consoleTypeFile = startElement.getAttributeByName(new QName("ConsoleLogger"));
						if(consoleTypeFile != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								allowConsoleLogging = true;
							}
						}
						break;
					case "logLevels":
						isInLogLevels = true;
						break;
					case "logLevel":
						
						if(!isInLogLevels) {
							break;
						}
						
						Attribute logTypeError = startElement.getAttributeByName(new QName("ERROR"));
						if(logTypeError != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								enabledLogLevelsList.add(LogLevel.ERROR);
							}
						}

						Attribute logTypeWarning = startElement.getAttributeByName(new QName("WARNING"));
						if(logTypeWarning != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								enabledLogLevelsList.add(LogLevel.WARNING);
							}
						}
						
						Attribute logTypeSuccess = startElement.getAttributeByName(new QName("SUCCESS"));
						if(logTypeSuccess != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								enabledLogLevelsList.add(LogLevel.SUCCESS);
							}
						}
						
						Attribute logTypeInfo = startElement.getAttributeByName(new QName("INFO"));
						if(logTypeInfo != null) {
							if(nextEvent.asCharacters().getData().equalsIgnoreCase("true")) {
								enabledLogLevelsList.add(LogLevel.INFO);
							}
						}
						break;
					default:
						break;
					}
				}
				if(nextEvent.isEndElement()) {
					EndElement endElement = nextEvent.asEndElement();
					switch(endElement.getName().getLocalPart()) {
					case "loggerOptions":
						isInLoggerOptions = false;
						break;
					case "logLevels":
						isInLogLevels = false;
						break;
					default:
						break;
					}
				}
			}
		} catch (XMLStreamException xse) {
			// do nothing and skip to default values
		} catch (FileNotFoundException fnfe) {
			// do nothing and skip to default values
		}
		
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