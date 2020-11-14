package UI;

/***
 * @Author	 : Shiva Dhanush
 * This file brings up the initial startSession page.
 ***/
 
import javafx.application.Application;
import infrastructure.validation.logger.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.simple.JSONObject;

import infrastructure.content.*;

public class Main extends Application {
	static ILogger logger = LoggerFactory.getLoggerInstance();
	@Override
	public void start(Stage primaryStage) {
		try {
			/** Loading the startSession page */
			Parent root = FXMLLoader.load(getClass().getResource("StartSession.fxml"));
			Scene scene = new Scene(root,600,800);
			primaryStage.setScene(scene);
			primaryStage.show();
	    	logger.log(ModuleID.UI, LogLevel.SUCCESS, "Startsession page has been opened.");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	public static void main(String[] args) {
		launch(args);
	}
}
