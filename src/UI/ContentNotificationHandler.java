/***
 * @Author	 : Jaya Madhav
 * File name : ContentNotificationHandler
 * File Type : Java
 * 
 * This file contains the class ContentNotificationHandler in which the definitions for the functions which the content module will use are written
 ***/
package UI;
import java.util.Base64;
import java.util.Map;
import java.util.jar.JarException;


import org.json.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import infrastructure.content.*;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/*
 * ContentNotificationHandler class
 */
public class ContentNotificationHandler implements IContentNotificationHandler {
	
	/*
	 * Defining the logger to log the messages
	 */
		static ILogger logger = LoggerFactory.getLoggerInstance();
		
	/*
	 * The function onNewUserJoined() displays an alert when a new user joins the canvas
	 * @param : username- A JSON String of the user name of the user entered
	 */

	@Override
	public void onNewUserJoined (String username) {

		//To display an alert on user joined.
		//The function is written in the controller
		if (username != null) {
			CanvasController controller = new CanvasController();
			controller.userJoined(username);
		}
		
		
	}
	
	/*
	 * Function onMessageReceived() will be called by the content module. It displays the received messages.
	 * @param : messageDetails - It is a json string whose meta fields are username (identifer of the user who sent the message), message (actual message sent by user), image (profile picture of user who sent the message) and time (the time the user sent this message)
	 */
	@Override
	public  void onMessageReceived (String messageDetails) {

		//message recieved function is written in the controller and is called here
		if (messageDetails != null) {
			CanvasController controller = new CanvasController();
			controller.messageRecieved(messageDetails);
		}
	}
	
	/*
	 * The function onNewUserExit() displays an alert when a new user leaves the canvas
	 * @param : username- A JSON String of the user name of the user entered
	 */
	
	@Override
	public void onUserExit (String username) {
	
		//To display an alert on user left.
		//The function is written in the controller
	    if (username != null) {
			CanvasController controller = new CanvasController();
			controller.userLeft(username);
		}
	}
	

	
		
}


