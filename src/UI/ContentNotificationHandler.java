/**
 * @Author	 : Jaya Madhav
 * File name : ContentNotificationHandler
 * File Type : Java
 *
 * This file contains the class ContentNotificationHandler in which the definitions for the functions which the content
 * module will use are written
 */
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
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Region;

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
	 * @return : none
	 */

	@Override
	public void onNewUserJoined (String username) {
		logger.log(
 				ModuleID.UI,
 				LogLevel.INFO,
 				"called onNewUserJoined by content"
 			);
		model temp = new model(UpdateMode.CONTENT_JOINED,username);
		CanvasController.updateList.setAll(temp);		
}

	/**
	 * Funct	ion onMessageReceived() will be called by the content module. It displays the received
	 * messages.
	 * @param : messageDetails - It is a json string whose meta fields are username (identifer of the
	 * user who sent the message), message (actual message sent by user), image (profile picture of
	 * user who sent the message) and time (the time the user sent this message).
	 * @return : none
	 */
	@Override
	public  void onMessageReceived (String messageDetails) {
		logger.log(
 				ModuleID.UI,
 				LogLevel.INFO,
 				"called onMessageReceived by content"
 			);
		model temp = new model(UpdateMode.CONTENT_MESSAGE,messageDetails);
		CanvasController.updateList.setAll(temp);
	}

	/**
	 * The function onNewUserJoined() displays an alert when a new user joins the canvas
	 * @param : username- A JSON String of the user name of the user entered
	 * @return : none
	 */
	@Override
	public void onUserExit (String username) {
		logger.log(
 				ModuleID.UI,
 				LogLevel.INFO,
 				"caleed onUserExit by content"
 			);
		model temp = new model(UpdateMode.CONTENT_LEAVE,username);
		CanvasController.updateList.setAll(temp);		
	}
}
