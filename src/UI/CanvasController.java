/***
 * Author	 : Jaya Madhav
 * File name : CanvasController
 * File Type : Java
 * 
 * This file is the controller for the UI module
 * The UI Package is imported into the controller
 * The functions in classes of UI package can now be used here.
 * Add all your created classes in UI package and use the functions in them here.
 * Do not make changes to the already existing code.
 * Import all the necessary javafx packages, if required.
 ***/
package UI;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class CanvasController extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	/***
	 * Declare and use all the javafx components and events here.
	 * sendButton is the id of 'SEND' button in Chatbox
	 * sendMessage is the id of text entered in the text area of Chatbox
	 * chatDisplayBox is the id of the component which displays the Chatbox
	 ***/
	@FXML
	private Button sendButton;
	
	@FXML
	private TextArea sendMessage;
	
	@FXML
	private VBox chatDisplayBox;
	
	/***
	 * Javafx event handling the changes after clicking the 'SEND' button in the ChatBox.
	 ***/
	
	@FXML
	public void sendButtonClicked(ActionEvent e ) {
		
		String message = sendMessage.getText();
		Chatbox.buttonClick(e,message,chatDisplayBox);
		sendMessage.setText(null);
	}
	/***
	 * 
	 * Call and use other javfx events here...
	 * 
	 ***/
	
	/***
	 * This is the method to load the javafx file and display it.
	 ***/
	@Override
	public void start(Stage primaryStage){
		try {
			
				Parent root = FXMLLoader.load(getClass().getResource("canvas.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setTitle("Whiteboard Application"); //setting the title of the application
				primaryStage.setScene(scene);
				primaryStage.show(); //displaying the user page to draw on the canvas and message using Chatbox

			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		

}
