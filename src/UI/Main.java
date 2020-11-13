package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = 
			FXMLLoader.load(getClass().getResource("canvas.fxml"));
		
		
		Scene scene = new Scene(root, 500, 800);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] argList) {
		launch(argList);
	}
}
