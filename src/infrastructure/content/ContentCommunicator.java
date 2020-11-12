package infrastructure.content;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import java.util.HashMap;
import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.INotificationHandler;
import org.json.JSONObject;

/**
 * This class ContentCommunicator implements the IContentCommunicator interface and all the 
 * methods inside it. The ideal purpose of this class is to initialise any new client, send 
 * message from UI to networking module, handle client exit with respect to content module
 * and handle the subscribers of content module.
 * 
 * @author Badal Kumar (111701008)
 */
public class ContentCommunicator implements IContentCommunicator{
	/**
	 * This variable will store the username/userid of client as a String.
	 */
	private static String userName;
	
	/**
	 * This variable will store the image of client in String format.
	 */
	private String userImage;
	
	/**
	 * This variable will store the server's ipAddress as a String.
	 */
	private String serverIPAddress;
	
	/**
	 * Creating a HashMap to store userName and userImage of all active clients of a particular board.
	 * Both userName and userImage are of String type
	 */
	private static HashMap<String, String> imageMap = new HashMap<String, String>();
	
	/**
	 * Creating a HashMap to store the identifier and handler of subscriber of this module,
	 * identifier is of type String while handler is of type IContentNotificationHandler.
	 */
	private static HashMap<String, IContentNotificationHandler> handlerMap = new HashMap<String, IContentNotificationHandler>();
	
	/**
	 * An instance of ServerPort class is fetched and stored in the variable serverPort.
	 */
	private IServerPort serverPort = ContentFactory.getServerPort();
	/**
	 * The variable port will store the port number of the board which the client is accessing.
	 */
	private int port;
	
	/**
	 * This variable will store the communicator of networking module to send data over the network.
	 */
	private ICommunicator communicator = CommunicatorFactory.getCommunicator(0);
	
	/**
	 * logger is the instance of the class which implements ILogger interface.
	 */
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	/**
	 * This variable will store the String which will be passed to log method of logger
	 */
	private static String logMessage;
	
	/**
	 * Constructor. Inside this, an instance of NetworkMessageHandler class is created and then,
	 * it is used for subscribing to the networking module to receive messages related to content.
	 */
	protected ContentCommunicator() {
		INotificationHandler nmh = new NetworkMessageHandler();
		communicator.subscribeForNotifications("content", nmh);
	}
	
	/**
	 * This method provides userName to other classes within this package.
	 * @return userName - username of the client.
	 */
	protected static String getUserName() {
		if (userName != null) {
			logMessage = "getUserName method of ContentCommunicator class is executing";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		}
		else {
			logMessage = "value of userName yet to be set in ContentCommunicator class";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.WARNING, logMessage);
		}
		return userName;
	}
	
	/**
	 * This method provides HashMap imageMap to other classes within this package.
	 * @return imageMap - The HashMap which stores the userName and userImage.
	 */
	protected static HashMap<String, String> getImageMap() {
		logMessage = "getImageMap method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		return imageMap;
	}
	
	/**
	 * This method allows other classes within this package to set the HashMap imageMap.
	 * @param imagemap - The HashMap which stores the userName and userImage of all clients.
	 */
	protected static void setImageMap(HashMap<String, String> imagemap) {
		logMessage = "setImageMap method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		imageMap = imagemap;
	}
	
	/**
	 * This method provides HashMap handlerMap to other classes within this package.
	 * @return handlerMap - The HashMap which stores the identifier and handler of subscriber of this module.
	 */
	protected static HashMap<String, IContentNotificationHandler> getHandlerMap() {
		logMessage = "getHandlerMap method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		return handlerMap;
	}
	
	/**
	 * This method initializes a user by storing its username and userimage in a map as Strings.
	 * This method stores these values locally too and generates server's ipAddress by using port.
	 * Finally, some manipulation on JSON string is done and then sent to server via networking.
	 * @param userDetails - This is a JSON string which contains a JSON object with three keys, 
	 * namely ipAddress, username and image.
	 */
	@Override
	public void initialiseUser(String userDetails) {
		logMessage = "initialiseUser method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userDetails);
		} catch(Exception e) {
			logMessage = "Argument userDetails cannot be converted to JSON object";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		try {
			serverIPAddress = jsonObject.getString("ipAddress");
		} catch(Exception e) {
			logMessage = "the value of ipAddress key in argument userDetails is not a String";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		try {
			userName = jsonObject.getString("username");
		} catch(Exception e) {
			logMessage = "the value of username key in argument userDetails is not a String";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		try {
			userImage = jsonObject.getString("image");
		} catch(Exception e) {
			logMessage = "the value of image key in argument userDetails is not a String";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		port = serverPort.getPort();
		while (port == 0) {
			logMessage = "The port value is not yet updated, cannot create server's ipAddress";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			port = serverPort.getPort();
		}
		serverIPAddress += ":";
		serverIPAddress += String.valueOf(port);
		logMessage = "server's ipAddress in ContentCommunicator class created";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		imageMap.put(userName, userImage);
		jsonObject.put("meta", "newUser");
		jsonObject.remove("ipAddress");
		communicator.send(serverIPAddress, jsonObject.toString(), "content");
		logMessage = "Desired message passed to networking to send it to server";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		logMessage = "Successfully finished the initialiseUser method of ContentCommunicator class";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
	}
	
	/**
	 * This method gets the message from UI module and then send it via networking module.
	 * A meta field is added to it indicating that it is message, before sending to server.
	 * An username field is added indicating the owner of the actual message.
	 * @param message - This is a JSON string which contains a JSON object with key "message"
	 */
	@Override
	public void sendMessageToContent(String message) {
		logMessage = "sendMessageToContent method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(message);
		} catch(Exception e) {
			logMessage = "Argument message cannot be converted to JSON object";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		jsonObject.put("meta", "message");
		jsonObject.put("username",userName);
		communicator.send(serverIPAddress, jsonObject.toString(), "content");
		logMessage = "Desired message passed to networking to send it to server";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		logMessage = "Successfully finished the sendMessageToContent method of ContentCommunicator class";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
	}
	
	/**
	 * This method clears all the maps created by content module locally and informs the
	 * server that the client is leaving the board by sending a JSON String which contains
	 * a JSON object with two keys, meta and username via networking module.
	 */
	@Override
	public void notifyUserExit() {
		logMessage = "notifyUserExit method of ContentCommunicator is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		imageMap.clear();
		handlerMap.clear();
		logMessage = "imageMap and handlerMap of ContentCommunicator class are cleared";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("meta", "userExit");
		jsonObject.put("username", userName);
		communicator.send(serverIPAddress, jsonObject.toString(), "content");
		logMessage = "Desired message passed to networking to send it to server";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		logMessage = "Successfully finished the notifyUserExit method of ContentCommunicator class";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
	}
	
	/**
	 * UI will subscribe to us in order to receive any updates coming from other users.
	 * In this method, identifier and handler will be updated in the HashMap handlerMap.
	 * @param identifier - This will be a string and unique too
	 * @param handler - By using this handler, methods of IContentNotificationHandler will be called
	 */
	@Override
	public void subscribeForNotifications(String identifier, IContentNotificationHandler handler) {
		logMessage = "subscribeForNotification method of ContentCommunicator class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		handlerMap.put(identifier, handler);
		logMessage = "Added an element to handlerMap of ContentCommunicator class of identifier "+identifier;
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		logMessage = "Successfully finished the subscibeForNotifications method of ContentCommunicator class";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
	}
}