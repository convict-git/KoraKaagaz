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
	 * This variable will store the board server's ipAddress along with port as a String.
	 */
	private String boardServerFullAddress;
	
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
	 * A handler of INotificationHandler type
	 */
	public INotificationHandler nmh;
	
	/**
	 * Constructor. Inside this, an instance of NetworkMessageHandler class is created and then,
	 * it is used for subscribing to the networking module to receive messages related to content.
	 */
	protected ContentCommunicator() {
		nmh = new NetworkMessageHandler();
		communicator.subscribeForNotifications("content", nmh);
	}
	
	/**
	 * This method provides userName to other classes within this package.
	 * @return userName - username of the client.
	 */
	protected static String getUserName() {
		if (userName != null) {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"getUserName method of ContentCommunicator class is executing"
			);
		}
		else {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.WARNING,
				"value of userName yet to be set in ContentCommunicator class"
			);
		}
		return userName;
	}
	
	/**
	 * This method provides HashMap imageMap to other classes within this package.
	 * @return imageMap - The HashMap which stores the userName and userImage.
	 */
	protected static HashMap<String, String> getImageMap() {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"getImageMap method of ContentCommunicator class is executing"
		);
		return imageMap;
	}
	
	/**
	 * This method allows other classes within this package to set the HashMap imageMap.
	 * @param imagemap - The HashMap which stores the userName and userImage of all clients.
	 */
	protected static void setImageMap(HashMap<String, String> imagemap) {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"setImageMap method of ContentCommunicator class is executing"
		);
		imageMap = new HashMap<>(imagemap);
	}
	
	/**
	 * This method provides HashMap handlerMap to other classes within this package.
	 * @return handlerMap - The HashMap which stores the identifier and handler of subscriber of this module.
	 */
	protected static HashMap<String, IContentNotificationHandler> getHandlerMap() {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"getHandlerMap method of ContentCommunicator class is executing"
		);
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
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"initialiseUser method of ContentCommunicator class is executing"
		);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userDetails);
			try {
				boardServerFullAddress = jsonObject.getString("ipAddress");
			} catch(Exception e) {
				logger.log(
					ModuleID.INFRASTRUCTURE,
					LogLevel.ERROR,
					"No String value for ipAddress key in argument userDetails"
				);
				return;
			}
			try {
				userName = jsonObject.getString("username");
			} catch(Exception e) {
				logger.log(
					ModuleID.INFRASTRUCTURE,
					LogLevel.ERROR,
					"No String value for username key in argument userDetails"
				);
				return;
			}
			try {
				userImage = jsonObject.getString("image");
			} catch(Exception e) {
				logger.log(
					ModuleID.INFRASTRUCTURE,
					LogLevel.ERROR,
					"No String value for image key in argument userDetails"
				);
				return;
			}
			port = serverPort.getPort();
			while (port == 0) {
				logger.log(
					ModuleID.INFRASTRUCTURE,
					LogLevel.ERROR,
					"The port value is not yet updated, cannot create server's ipAddress"
				);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				port = serverPort.getPort();
			}
			boardServerFullAddress += ":" + String.valueOf(port);
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"server's ipAddress in ContentCommunicator class created"
			);
			imageMap.put(userName, userImage);
			jsonObject.put("meta", "newUser");
			jsonObject.remove("ipAddress");
			communicator.send(boardServerFullAddress, jsonObject.toString(), "content");
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"Desired message passed to networking to send it to server"
			);
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.SUCCESS,
				"Successfully finished the initialiseUser method of ContentCommunicator class"
			);
		}
		catch(Exception e) {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.ERROR,
				"Caught Exception: " + e.getMessage()
			);
		}
	}
	
	/**
	 * This method gets the message from UI module and then send it via networking module.
	 * A meta field is added to it indicating that it is message, before sending to server.
	 * An username field is added indicating the owner of the actual message.
	 * @param message - This is a JSON string which contains a JSON object with key "message"
	 */
	@Override
	public void sendMessageToContent(String message) {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"sendMessageToContent method of ContentCommunicator class is executing"
		);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(message);
			jsonObject.put("meta", "message");
			jsonObject.put("username",userName);
			communicator.send(boardServerFullAddress, jsonObject.toString(), "content");
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"Desired message passed to networking to send it to server"
			);
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.SUCCESS,
				"Successfully finished the sendMessageToContent method of ContentCommunicator class"
			);
		}
		catch(Exception e) {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.ERROR,
				"Catch Exception: " + e.getMessage()
			);
		}
	}
	
	/**
	 * This method clears all the maps created by content module locally and informs the
	 * server that the client is leaving the board by sending a JSON String which contains
	 * a JSON object with two keys, meta and username via networking module.
	 */
	@Override
	public void notifyUserExit() {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"notifyUserExit method of ContentCommunicator is executing"
		);
		try {
			imageMap.clear();
			handlerMap.clear();
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"imageMap and handlerMap of ContentCommunicator class are cleared"
			);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("meta", "userExit");
			jsonObject.put("username", userName);
			communicator.send(boardServerFullAddress, jsonObject.toString(), "content");
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.INFO,
				"Desired message passed to networking to send it to server"
			);
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.SUCCESS,
				"Successfully finished the notifyUserExit method of ContentCommunicator class"
			);
		}
		catch(Exception e) {
			logger.log(
				ModuleID.INFRASTRUCTURE,
				LogLevel.ERROR,
				"Catch Exception: " + e.getMessage()
			);
		}
	}
	
	/**
	 * UI will subscribe to us in order to receive any updates coming from other users.
	 * In this method, identifier and handler will be updated in the HashMap handlerMap.
	 * @param identifier - This will be a string and unique too
	 * @param handler - By using this handler, methods of IContentNotificationHandler will be called
	 */
	@Override
	public void subscribeForNotifications(String identifier, IContentNotificationHandler handler) {
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"subscribeForNotification method of ContentCommunicator class is executing"
		);
		handlerMap.put(identifier, handler);
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.INFO,
			"Added an element to handlerMap of ContentCommunicator class of identifier "+identifier
		);
		logger.log(
			ModuleID.INFRASTRUCTURE,
			LogLevel.SUCCESS,
			"Successfully finished the subscibeForNotifications method of ContentCommunicator class"
		);
	}
}
