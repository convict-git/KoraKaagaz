package UI;

import javafx.application.Application;
import infrastructure.validation.logger.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.*;
import infrastructure.content.*;

/**
 * @Author	 : Shiva Dhanush
 * This file brings up the initial startSession page.
 */

public class Main extends Application {
	static ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	 * This function opens the start session page.
	 * @param PrimaryStage This is Stage for showing fxml page.
	 * @returns nothing
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			/**
             * Loading the startSession page
             */
			Parent root = FXMLLoader.load(getClass().getResource("StartSession.fxml"));
			Scene scene = new Scene(root,600,800);
			primaryStage.setScene(scene);
			primaryStage.show();
	    	logger.log(
	    			ModuleID.UI,
	    			LogLevel.SUCCESS,
	    			"Startsession page has been opened."
				);

		}
		catch(Exception e) {
			logger.log(
					ModuleID.UI,
					LogLevel.ERROR,
					"Opening Start session page has failed."
				);
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
