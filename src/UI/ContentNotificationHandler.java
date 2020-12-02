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

		//Creating a JSON Object
		JSONObject obj = new JSONObject(username);

		//Obtaining the username string
		String userName = obj.getString("username");

		//new user joined alert.
		Alert alert = new Alert(
							AlertType.CONFIRMATION,
							"The user" + userName +  "joined the canvas! ",
							ButtonType.OK
						);

		//Displaying the alert
		alert.show();

		//Creating a new thread to display the alert for some seconds and dismiss
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);  // seconds * 100
                } catch (InterruptedException e) {
                    if (alert.isShowing())
                    {
                    	 alert.close();
                    }
                    e.printStackTrace();

					//log message
                    logger.log(
                    		ModuleID.UI,
                    		LogLevel.ERROR,
                    		e.toString()
						);

                } finally {
                    if (alert.isShowing())
                    {
                    	alert.close();
                    }
                }
           }
        }).run();

		//log message on entry of user
		logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"User"+ username +"successfully entered the canvas"
			);
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

		//Creating the CanvasController object
		CanvasController canvas = new CanvasController();

		//Getting the chatDisplayBox and the chatScroll pane from the controller
		VBox chatDisplayBox = canvas.getChatDisplayBox();
		ScrollPane chatScroll = canvas.getChatScroll();

		//Creating a JSON Object to store the message details
		JSONObject obj = new JSONObject(messageDetails);

		//Getting the username from messageDetails string
		String username = obj.getString("username");

		//Getting the message from messageDetails string
		String message = obj.getString("message");

		//Getting the image from messageDetails string
		String image= obj.getString("image");

		//Getting the time from messageDetails string
		String time= obj.getString("time");

		//Converting the image string to image bytes
		byte[] imageByte = Base64.getDecoder().decode(image);

		if (message!= null)
    	{
			//Converting the image bytes to javafx image
			Image imageJavafx = new Image(new ByteArrayInputStream(imageByte));

			//Creating a view for the javafx image
			ImageView viewImage = new ImageView(imageJavafx);

			//Setting the dimensions of the image
			viewImage.setFitHeight(25);
			viewImage.setFitWidth(25);
			viewImage.setPreserveRatio(true);

			//Setting the styling of the image
			viewImage.setStyle("-fx-border-color: black;-fx-background-radius: 10; -fx-border-radius: 10 10 10 10");
			chatScroll.setFitToWidth(true);

			//Creating a label to display the username
			Label userNameDisplay = new Label(username);

			//Setting styling for the username label
			userNameDisplay.setStyle(" -fx-font: 10pt 'Corbel';-fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: orange;-fx-border-color: black;-fx-background-radius: 10 10 0 0; -fx-border-radius: 10 10 0 0");

			//Creating a label to display the message received
			Label msgLabel=new Label(message);
			msgLabel.setMinHeight(Region.USE_PREF_SIZE);

			//Setting the styling for the image
			msgLabel.setStyle(" -fx-font: 14pt 'Corbel'; -fx-text-fill: black; -fx-background-color: orange;-fx-border-color: black;-fx-background-radius: 0 10 10 10; -fx-border-radius: 0 10 10 10");
			msgLabel.setWrapText(true);
			msgLabel.setTextAlignment(TextAlignment.JUSTIFY);

			//Creating a VBox to store the labels created to display username and message
			VBox usernameMessageDisplayBox = new VBox();
			usernameMessageDisplayBox.getChildren().addAll(userNameDisplay,msgLabel);

			//Creating a HBox to store the image and usernameMessageDisplayBox
			HBox imageUsernameMessageDisplayBox=new HBox();
			imageUsernameMessageDisplayBox.setSpacing(10);
			imageUsernameMessageDisplayBox.getChildren().addAll(viewImage,usernameMessageDisplayBox);

			//Setting the position of imageUsernameMessageDisplayBox
			imageUsernameMessageDisplayBox.setAlignment(Pos.BASELINE_LEFT);

			//Adding the imageUsernameMessageDisplayBox to the chatDisplayBox
			chatDisplayBox.getChildren().add(imageUsernameMessageDisplayBox);

			//Setting the spacing between each element in the chatDisplayBox
			chatDisplayBox.setSpacing(10);

			//Making sure the chatScroll is always at the bottom
			chatScroll.setVvalue(1);

			//log message on receiving message
			logger.log(
					ModuleID.UI,
					LogLevel.SUCCESS,
					"Message received from the user and displayed on the Chatbox"
				);

    	}
		else
		{
			logger.log(
					ModuleID.UI,
					LogLevel.INFO,
					"No message is received from the user");
		}
	}

	/**
	 * The function onNewUserJoined() displays an alert when a new user joins the canvas
	 * @param : username- A JSON String of the user name of the user entered
	 * @return : none
	 */
	@Override
	public void onUserExit (String username) {

		//Creating a JSON Object
		JSONObject obj = new JSONObject(username);

		//Obtaining the username string
		String userName = obj.getString("username");

		//new user joined alert.
		Alert alert = new Alert(AlertType.CONFIRMATION, "The user" + userName +  "left the canvas! ", ButtonType.OK);

		//Displaying the alert
		alert.show();

		//Creating a new thread to display the alert for some seconds and dismiss
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);  // seconds * 100
				}
            	catch (InterruptedException e) {
					if (alert.isShowing())
					{
				 		alert.close();
					}
					e.printStackTrace();

					//log message
					logger.log(
							ModuleID.UI,
							LogLevel.ERROR,
							e.toString());

				}
				finally {
					if (alert.isShowing())
					{
						alert.close();
					}
				}
			}
		}).run();

		//log message on exit of user
		logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"User"+ username +"successfully left the canvas");
	}
}
