package infrastructure.validation.logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

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
	
	private boolean enableTestMode = false;
	
	/**
	 * constructor for LoggerManager class
	 */
	protected LoggerManager() {
		
		File logConfigFile = new File(loggerConfigFilePath);
		List<LogLevel> enabledLogLevelsList = null;
				
		if(logConfigFile.isFile()) {
			
			String fileToParse = configFileToParse(loggerConfigFilePath);
			
			try {
				
				File configFile = new File(fileToParse);
				if(configFile.isFile()) {
					enabledLogLevelsList = parse(fileToParse);
				} else {
					enabledLogLevelsList = parse(loggerConfigFilePath);
				}
				
			} catch (NullPointerException npe) {
				// occurs in the case where the pathname argument becomes null
				// load the loggerConfigFilePath
				enabledLogLevelsList = parse(loggerConfigFilePath);
			} catch (SecurityException se) {
				// a security manager, if it exists can deny read access to the file
				// equivalent to the case if isFile method returns False and so, same can be done
				enabledLogLevelsList = parse(loggerConfigFilePath);
			}

		}
		
		if(allowFileLogging) {
			fileLogger = new FileLogger(enabledLogLevelsList, enableTestMode);
		}
		
		if(allowConsoleLogging) {
			consoleLogger = new ConsoleLogger(enabledLogLevelsList, enableTestMode);
		}
	}
	
	/** 
	 *  helper method that checks whether the logger is to be initialized in test mode,
	 *  works by the testMode tag in the default XML file
	 *  If the tag exists and is set to true, then the test mode is considered enabled and,
	 *  a different XML file is to be used.
	 *  Otherwise, the file is considered to be not in test mode and so,
	 *  the default configuration XML file itself is used.
	 * 
	 * @param defaultFilePath location of the default XML file configuration
	 * @return a string that will be the location of the XML configuration file to be parsed further
	 */
	private String configFileToParse(String defaultFilePath) {
		
		String fileToParse = defaultFilePath;
		
		try {
			
			File inputFile = new File(defaultFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			// System.out.println("Root Element :"+doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("testMode");
			
			for(int temp=0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				// System.out.println("Current Element :" + nNode.getNodeName());
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					// System.out.println("testMode attribute :"+eElement.getAttribute("filePath"));
					// System.out.println("testMode value :"+eElement.getTextContent());
					if(eElement.getTextContent().equalsIgnoreCase("true")) {
						// System.out.println("there's a new dawn");
						enableTestMode = true;
						fileToParse = eElement.getAttribute("filePath");
					}
				}
			}

		} catch (FileNotFoundException fnfe) {
			// do nothing and skip to default values
		} catch (SecurityException se) {
			// in the presence of a security manager, it's checkRead method can deny read access to the file
			// if it occurs, do nothing and skip to default values
		} catch (ClassCastException cce) {
			// thrown by startEvent.asCharacters() method if it fails 
			// if it occurs, do nothing and skip to default values
		} catch (ParserConfigurationException pce) {
			// thrown when parser cannot be configured
			// if it occurs, do nothing and skip to default values
		} catch (SAXException e) {
			// thrown when parser fails to parse the input file
			// if it occurs, do nothing and skip to default values
		} catch (IOException e) {
			// thrown when parser method cannot open the input file
			// if it occurs, do nothing and skip to default values
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
		
		parseLoggerOptions(filePath);

		enabledLogLevelsList = parseLogLevels(filePath);
		
		return enabledLogLevelsList;
	}

	/**
	 * helper method that parses XML file,
	 * looks for loggerOptions tags that enable/disables file and console loggers
	 * 
	 * @param filePath path to the XML config file
	 */
	private void parseLoggerOptions(String filePath) {
		
		try {
			
			File inputFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("loggerOption");
			
			for(int temp=0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					if(eElement.getTextContent().equalsIgnoreCase("true")) {

						if(eElement.getAttribute("LoggerName").equalsIgnoreCase("FileLogger")) {
							allowFileLogging = true;
						}
						
						if(eElement.getAttribute("LoggerName").equalsIgnoreCase("ConsoleLogger")) {
							allowConsoleLogging = true;
						}
					}
				}
			}

		} catch (FileNotFoundException fnfe) {
			// do nothing and skip to default values
		} catch (SecurityException se) {
			// in the presence of a security manager, it's checkRead method can deny read access to the file
			// if it occurs, do nothing and skip to default values
		} catch (ClassCastException cce) {
			// thrown by startEvent.asCharacters() method if it fails 
			// if it occurs, do nothing and skip to default values
		} catch (ParserConfigurationException pce) {
			// thrown when parser cannot be configured
			// if it occurs, do nothing and skip to default values
		} catch (SAXException saxe) {
			// thrown when parser fails to parse the input file
			// if it occurs, do nothing and skip to default values
		} catch (IOException ioe) {
			// thrown when parser method cannot open the input file
			// if it occurs, do nothing and skip to default values
		}

	}

	/**
	 * helper method that parses XML file,
	 * looks for logLevels tags that enable/disables logLevels.
	 * logLevels that are checked are: ERROR, WARNING, SUCCESS, INFO
	 * 
	 * @param filePath path to the XML config file
	 * @return list of LogLevel enums. Members of this List correspond to LogLevels to be enabled.
	 */
	private List<LogLevel> parseLogLevels(String filePath) {

		List<LogLevel> enabledLogLevelsList = new ArrayList<LogLevel>();
		
		try {
			
			File inputFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("logLevel");
			
			for(int temp=0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					if(eElement.getTextContent().equalsIgnoreCase("true")) {

						if(eElement.getAttribute("level").equalsIgnoreCase(LogLevel.ERROR.toString())) {
							enabledLogLevelsList.add(LogLevel.ERROR);
						}
						
						if(eElement.getAttribute("level").equalsIgnoreCase(LogLevel.WARNING.toString())) {
							enabledLogLevelsList.add(LogLevel.WARNING);
						}
						
						if(eElement.getAttribute("level").equalsIgnoreCase(LogLevel.SUCCESS.toString())) {
							enabledLogLevelsList.add(LogLevel.SUCCESS);
						}
						
						if(eElement.getAttribute("level").equalsIgnoreCase(LogLevel.INFO.toString())) {
							enabledLogLevelsList.add(LogLevel.INFO);
						}
					}
				}
			}
		} catch (FileNotFoundException fnfe) {
			// do nothing and skip to default values
		} catch (SecurityException se) {
			// in the presence of a security manager, it's checkRead method can deny read access to the file
			// if it occurs, do nothing and skip to default values
		} catch (ClassCastException cce) {
			// thrown by startEvent.asCharacters() method if it fails 
			// if it occurs, do nothing and skip to default values
		} catch (ParserConfigurationException pce) {
			// thrown when parser cannot be configured
			// if it occurs, do nothing and skip to default values
		} catch (SAXException saxe) {
			// thrown when parser fails to parse the input file
			// if it occurs, do nothing and skip to default values
		} catch (IOException ioe) {
			// thrown when parser method cannot open the input file
			// if it occurs, do nothing and skip to default values
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