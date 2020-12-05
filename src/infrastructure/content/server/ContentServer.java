package infrastructure.content.server;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.INotificationHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.server.board.IClientIP;
import processing.server.board.ServerFactory;
import processing.utility.IpAddress;
import processing.utility.Username;

/**
 * This class represents the server part of content module and it is going to be
 * run only on server node. This class implements the INotificationHandler interface
 * and ultimately the method declared in that interface. This also contains an 
 * additional method which is useful for broadcasting to all other active clients.
 * @author Badal Kumar (111701008)
 */
public class ContentServer implements INotificationHandler{
	/**
	 * This String saves the value which is stored in "meta" key of JSON Object.
	 */
	private String meta;
	
	/**
	 * This String stores the current time as String.
	 */
	private String time;
	
	/**
	 * This String stores the value corresponding to "username" key of JSON object.
	 */
	private String userName;
	
	/**
	 * This String stores the value corresponding to "image" key of JSON object.
	 */
	private String userImage;
	
	/**
	 * The variable stores the IP Address of active clients which it gets from processing module.
	 * It is further used to send information to all clients via networking module.
	 */
	private IpAddress ipAddress;
	
	/**
	 * This HashMap stores the userName and userImage of all active clients of current
	 * board server. Both userNames and userImages are stored as Strings. 
	 */
	private HashMap<String, String> imageMap = new HashMap<String, String>();
	
	/**
	 * This HashMap stores the userName and ipAddress of all active clients. This 
	 * information is obtained from processing module by the interface IClientIP.
	 */
	private HashMap<Username, IpAddress> userIpMap = new HashMap<Username, IpAddress>();
	
	/**
	 * logger is the instance of the class which implements ILogger interface.
	 */
	private ILogger logger = LoggerFactory.getLoggerInstance();
	
	/**
	 * This variable will store the String which will be passed as argument to log method of logger
	 */
	private String logMessage;
	
	/**
	 * This object will store the communicator of networking module to send information over the network.
	 */
	private ICommunicator communicator = CommunicatorFactory.getCommunicator(0);
	
	/**
	 * This variable stores the object of the class which implements IClientIP interface.
	 */
	private IClientIP clientIP = ServerFactory.getIPHandler();
	
	/**
	 * This method is called by networking module with the argument message, a String.
	 * This method is internally divided into 3 categories based on the meta key value
	 * from the JSON object derived from argument message. If the meta value is newUser
	 * then imageMap is updated with new userName and userImage and then the map is
	 * stored in a JSON Array and sent to all active clients. If the meta value is 
	 * message then current time is added to it and send to all active clients. If 
	 * the meta value is userExit then the corresponding userImage from imageMap of
	 * the userName which is exiting from is removed and informed to all other clients.
	 * @param message - A String which actually represents a JSON object
	 */
	@Override
	public void onMessageReceived(String message) {
		// logging to logger
		logMessage = "onMessageReceived method of ContentServer class is going to be executed";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		// Obtaining the userName to ipAddress map from processing
		userIpMap = (HashMap<Username, IpAddress>) clientIP.getClientIP();
		// storing message in the form of JSON Object
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(message);
		} catch(Exception e) {
			// logging to logger
			logMessage = "the argument message cannot be parsed as a JSON Object";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		try {
			meta = jsonObject.getString("meta");
		} catch(Exception e) {
			// logging to logger
			logMessage = "the value of meta key in argument message is not a String";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		if (meta.equals("newUser")) {
			// logging to logger
			logMessage = "New User case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			try {
				userName = jsonObject.getString("username");
			} catch(Exception e) {
				// logging to logger
				logMessage = "the value of username key in argument message is not a String";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			try {
				userImage = jsonObject.getString("image");
			} catch(Exception e) {
				// logging to logger
				logMessage = "the value of image key in argument message is not a String";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			// updating imageMap
			imageMap.put(userName, userImage);
			jsonObject.remove("userName");
			jsonObject.remove("image");
			// declaring a JSON Array and a temporary JSON Object
			JSONArray jsonArray = new JSONArray();
			// This loop adds all userName-userImage as a JSON object to a JSON Array
			for (String name : imageMap.keySet()) {
				userName = name;
				userImage = imageMap.get(userName);
				
				JSONObject tempJsonObject = new JSONObject();
				tempJsonObject.put("username", userName);
				tempJsonObject.put("image", userImage);
				jsonArray.put(tempJsonObject);
			}
			jsonObject.put("imageMap", jsonArray);
			// logging to logger
			logMessage = "The JSON Array is created which contains the userName to userImage Map";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			sendToAll(jsonObject.toString());
			// logging to logger
			logMessage = "The JSON Array is now being broadcasted to all clients";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			// logging to logger
			logMessage = "The onMessageReceived method is successfully implemented";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
		}
		else if (meta.equals("message")) {
			// logging to logger 
			logMessage = "Message case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			// saving the local time
			LocalTime now = LocalTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			try {
				time = dtf.format(now).toString();
			} catch(Exception e) {
				// logging to logger
				logMessage = "Error has occured. The current time cannot be formatted.";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			jsonObject.put("time",time);
			// logging to logger
			logMessage = "The message is now being broadcasted to all clients";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			sendToAll(jsonObject.toString());
			// logging to logger
			logMessage = "The onMessageReceived method is successfully implemented";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
		}
		else if (meta.equals("userExit")) {
			// logging to logger
			logMessage = "User Exit case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			try {
				userName = jsonObject.getString("username");
			} catch(Exception e) {
				// logging to logger
				logMessage = "the value of username key in argument message is not a String";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			imageMap.remove(userName);
			Username userNameP = new Username(userName);
			if (userIpMap.containsKey(userNameP)) {
				userIpMap.remove(userNameP);
			}
			// logging to logger
			logMessage = "The message is now being broadcasted to all clients";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			sendToAll(message);
			// logging to logger
			logMessage = "The onMessageReceived method is successfully implemented";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
		}
		else {
			// logging to logger
			logMessage = "The value of meta key of argument message is an invalid String";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
	}
	
	/**
	 * This method is used to send message to each active client of the board. It first gets the latest
	 * userName to ipAddress map from processing and uses the ipAddress to send message to all clients. 
	 * @param message - This String argument is going to be sent to all active clients.
	 */
	private void sendToAll(String message) {
		// logging to logger
		logMessage = "sendToAll method of ContentServer class is going to be executed";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		for (Username name: userIpMap.keySet()) {
			userName = name.toString();
			ipAddress = userIpMap.get(name);
			//sending message to ipAdress via networking
			communicator.send(ipAddress.toString(), message, "content");
			//logging to logger
			logMessage = "The message is sent to userName " + userName;
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		}
		//logging to logger
		logMessage = "The message is successfully sent to all client";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
	}
}
