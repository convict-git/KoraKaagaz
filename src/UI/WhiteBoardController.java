package UI;

/***
 * @Author	 : Shiva Dhanush
 * This controller handles operations on going from startsession page to canvas page.
 ***/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
public class WhiteBoardController {
   
	
	
	
	public void startSession (ActionEvent event) throws Exception
	{   ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("canvas.fxml"));
		Scene scene = new Scene(root,600,800);
	//	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		 
		
	} 
		
	
	
	
	
}
