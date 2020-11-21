package infrastructure.content;

import networking.INotificationHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;

/**
 * NetworkMessageHandler class implements the INotificationHandler interface.
 * this includes one method i.e onMessageReceived()
 * this class is used to notify and to send data to the UI module 
 * as new user joined or as a user exit or as message sent by a user
 * 
 * @author Talha Yaseen
 */
public class NetworkMessageHandler implements INotificationHandler {
		
	/**
	* Creating instance of class which implements ILogger interface
	*/
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	/**
	 * Accessing handlerMap hashmap from ContentCommunicator class to use it for sending message to UI
	 */
	private static HashMap<String, IContentNotificationHandler> handlerMap = ContentCommunicator.getHandlerMap();
	
	/**
	 * Accessing imageMap hashmap from ContentCommunicator class to access image corresponding to username
	 */
	private static HashMap<String, String> imageMap = ContentCommunicator.getImageMap();
	
	/**
	 * This variable will store the meta field of json string sent by the Networking module
	 */
	private static String metafield;
	
	/**
	 * This array will store the jsonObject whose fields would be username and image
	 */
	private static JSONArray jsonObjectArray;
	 
	/**
	 * This json object will store the elements of jsonObjectArray array
	 */
	private static JSONObject object;
	
	/**
	 * This variable will store the username of client
	 */
	private static  String username;
	
	/**
	 * This variable will store the image accessed by imageMap corresponding to username 
	 */
	private static String userimage;
	
	/**
	 * Creating a HashMap to store userName and userImage of all active clients of a particular board.
	 * Both userName and userImage are of String type
	 */
	private static HashMap<String, String> imagemap;
	
	/**
	* This variable will store a string which will contains log message that to be passed to log method of logger
	*/
	private static String logMessage;
	
	/**
	 * Getting handler of the UI module to send the data
	 */
	private static IContentNotificationHandler handler;
	 
	/**
	 * @param message - JSON string message (Meta fields are newUser or message or userExit)
	 *
	 * if meta field is newUser, then remaining field of parameter message would only be imageMap (it is a string of json array whose elements are json object having fields username and corresponding image). 
	 *		onNewUserJoined(String message) - message fields are username and image  
	 * if meta field is message, then remaining fields of parameter message would be username, message and time. onMessageReceived(String message) - message fields are username, message, time and image
	 * if meta field is exitUser, then remaining field of parameter message would only be username. onUserExit(String message) - message fields are username and image
	 * Networking module will be calling this method with json string message to send data to the content module
	 *
	 * creating error log message if message in not converting into json object,
	 * or if not able extract field from object, or if not able to extract handler from hashmap (handlerMap)
	 * if message will be successfully sent to UI, then creating success log message
	 */
	@Override
	public void onMessageReceived(String message) {
		logMessage = "onMessageReceived method of NetworkMessageHandler class is executing";
		logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		JSONObject jsonObject;
		
		try {
			jsonObject = new JSONObject(message);
		} 
		catch(Exception e) {
			logMessage = "converting JSON string to JSON object process failed";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		
		try {
			metafield = jsonObject.getString("meta");
		}
		catch(Exception e) {
			logMessage = "failed to get metafield value of argument message";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		
		try {
			handler = handlerMap.get("UI");
		}
		catch(Exception e) {
			logMessage = "failed to get IContentNotification handler from handlerMap";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
		
		jsonObject.remove("meta");
		
		if(metafield.equals("newUser")) {
			logMessage = "New User case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			
			imagemap = new HashMap<String, String>();
			jsonObjectArray = new JSONArray();
			
			try {
				jsonObjectArray = jsonObject.getJSONArray("imageMap");
			}
			catch(Exception e) {
				logMessage = "failed to get imageMap field value of argument message";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			
			jsonObject.remove("imageMap");
			for(int i = 0; i < jsonObjectArray.length(); i++) {
				try {
					object = jsonObjectArray.getJSONObject(i);
				}
				catch(Exception e) {
					logMessage = "failed to get element of json array";
					logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
					return;
				}
				
				try {
					username = object.getString("username");
				}
				catch(Exception e) {
					logMessage = "failed to get username field value from object";
					logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
					return;
				}
				
				try {
					userimage = object.getString(username);
				}
				catch(Exception e) {
					logMessage = "failed to get image from imageMap";
					logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
					return;
				}
				imagemap.put(username, userimage);
				if(!imageMap.containsKey(username)) {
					jsonObject.put("username", username);
					jsonObject.put("image", userimage);
				}
			}
			
			ContentCommunicator.setImageMap(imagemap);
			logMessage = "imageMap updated successfully";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
			
			handler.onNewUserJoined(jsonObject.toString());
			logMessage = "onNewUserJoined called";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		}
		else if(metafield.equals("message")) {
			logMessage = "Message case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			
			try {
				username = jsonObject.getString("username");
			}
			catch(Exception e) {
				logMessage = "failed to get username field value of argument message";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			
			try {
				userimage = imageMap.get(username);
			}
			catch(Exception e) {
				logMessage = "failed to get image from imageMap";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			jsonObject.put("image", userimage);
			handler.onMessageReceived(jsonObject.toString());
			logMessage = "onMessageReceived called";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		}
		else if (metafield.equals("userExit")) {
			logMessage = "User Exit case";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
			
			try {
				username = jsonObject.getString("username");
			}
			catch(Exception e) {
				logMessage = "failed to get username field value of argument message";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			
			try {
				userimage = imageMap.get(username);
			}
			catch(Exception e) {
				logMessage = "failed to get image from imageMap";
				logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
				return;
			}
			jsonObject.put("image", userimage);
			imageMap.remove(username);
			ContentCommunicator.setImageMap(imageMap);
			logMessage = "imageMap updated successfully";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.SUCCESS, logMessage);
			
			handler.onUserExit(jsonObject.toString());
			logMessage = "onUserExit called";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.INFO, logMessage);
		}
		else {
			logMessage = "no method with this name exists";
			logger.log(ModuleID.INFRASTRUCTURE, LogLevel.ERROR, logMessage);
			return;
		}
	}
}
