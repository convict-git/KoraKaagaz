/**
 * @Author   : Jaya Madhav
 * File name : Chatbox
 * File Type : Java
 *
 * This file contains the function to display the text message entered in the text area of the chatbox
 * Then, messages entered by the user will be sent to the content module as a JSON String
 */

/**
 * Importing all the necessary packages
 */
package UI;

import infrastructure.content.*;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

import java.util.Stack;

import org.json.*;


/**
 * Chatbox class
 */
public class Chatbox implements Initializable{

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

    /**
     * Defining the logger to log the messages
     */
	static ILogger logger = LoggerFactory.getLoggerInstance();

	/*
	* Function buttonClick() will be called in the CnvasController.java, when a user clicks the send
	* button in the chatbox.
	* @param : e-ActionEvent of button click
	* @param : userMessage-message entered in the text area,
	* @param :chatDisplayBox-chat displaying box,
	* @param :chatScroll-scroll pane of the chatbox
	*/
    public static void buttonClick(
		ActionEvent e,
		String userMessage,
		VBox chatDisplayBox,
		ScrollPane chatScroll
	) {
    	//checking whether the user has entered any message or not
    	if (userMessage != null)
    	{

    		//Setting the chat scroll pane width
            chatScroll.setFitToWidth(true);

            //Creating a label to store the message entered by the user
            Label sendMessageLabel=new Label(userMessage);
            sendMessageLabel.setMinHeight(Region.USE_PREF_SIZE);

			//Adding styling to the label
            sendMessageLabel.setStyle(" -fx-font: 14pt 'Corbel'; -fx-text-fill: black; -fx-background-color: orange;-fx-border-color: black;-fx-background-radius: 10; -fx-border-radius: 10 10 10 10");

			//Making sure that the label created is wrapped around the text inside
            sendMessageLabel.setWrapText(true);

			//Setting the alignment of the message label
            sendMessageLabel.setTextAlignment(TextAlignment.JUSTIFY);

			//creating a HBox to store the label
            HBox sendMessagehBox=new HBox();

			//Keeping the message label inside the HBox
            sendMessagehBox.getChildren().add(sendMessageLabel);

			//Aligning the position of the HBox
            sendMessagehBox.setAlignment(Pos.BASELINE_RIGHT);

			//Adding the HBox to the chat display box
            chatDisplayBox.getChildren().add(sendMessagehBox);

			//setting spacing between each element in the chat display box
            chatDisplayBox.setSpacing(10);

			//Making sure that the scroll bar is always at the bottom
            chatScroll.setVvalue(1);

			//Creating a JSON object to send the message to the content module
            JSONObject sendMessageObject=new JSONObject();

			//Assigning the entered message as value to the key : "message"
            sendMessageObject.put("message",userMessage);

			//Converting the JSON object to JSON string
            String message=sendMessageObject.toString();

			//Sending the message to the content module
            IContentCommunicator communicator = ContentFactory.getContentCommunicator();
            communicator.sendMessageToContent(message);

			//Logger to log the messages
            logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"Message from user has been displayed on chatbox and sent to content module"
			);

    	}
    	else
    	{
    		//Logger to log the messages
        	logger.log(ModuleID.UI, LogLevel.INFO, "User did not enter any message");
    	}

    }

}
