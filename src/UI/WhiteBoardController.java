package UI;

import infrastructure.validation.logger.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import javax.imageio.ImageIO;
import processing.*;
import org.json.*;
import infrastructure.content.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @Author	 : Shiva Dhanush
 * This controller handles operations on going from startsession page to canvas page.
 * It takes the image selected by user and encodes it into a base64 string.
 * It sends user details to processing module and content module.
 */

public class WhiteBoardController {

	@FXML
	private TextField userName;

	@FXML
	private TextField ipAddress;

	@FXML
	private TextField boardId;

	@FXML
	private Button imageChoose;
	private String encodedImage;
	static ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	 * This function takes jpg image and converts it to base64 string.
	 * @param event The event is selecting image.
	 * @returns nothing
	 */
	public void getImage (ActionEvent event) throws Exception
	{
		try {
			/**
			 * This method takes in the image of jpg type and encodes it into a string.
			 */
			FileChooser fc = new FileChooser();
			File f = fc.showOpenDialog(null);
			if(f != null) {
				/**
				 * Byte array of image file taken as input is encoded with Base64.
				 */
				BufferedImage originalImage = ImageIO.read(f);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( originalImage, "jpg", baos );
				baos.flush();
				byte[] imagebyte = baos.toByteArray();
				encodedImage = Base64.getEncoder().encodeToString(imagebyte);
				baos.close();
			}
		} catch (Exception e) {
			logger.log(
					ModuleID.UI,
					LogLevel.ERROR,
					"Error in encoding image"
			);
		}
	}

	/**
	 * This function sends data to processing,content modules and opens canvas.
	 * @param event The event is clicking start session button.
	 * @returns nothing
	 */
	public void startSession (ActionEvent event) throws Exception {
		try {
			/**
			 * Opening the canvas fxml page.
			 */
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("canvas.fxml"));
			CanvasController controller = new CanvasController();
			loader.setController(controller);
			Parent root = loader.load();
			Scene scene = new Scene(root,600,800);
			primaryStage.setScene(scene);
			primaryStage.show();
			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"Opening the canvas fxml page."
			);

			/**
			 * Sending the data filled to the processing module
			 */
			String getIp = ipAddress.getText();
			String getBoardId =  boardId.getText();
			if (getBoardId.isEmpty()) {
				getBoardId=null;
			}
			Processor processor = ProcessingFactory.getProcessor() ;
			IUser user = processor;
			String returnval= user.giveUserDetails(userName.getText(),ipAddress.getText(),getBoardId);
			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"Userdetails to processing module have been sent successfully"
			);

			/**
			 * Keeping the userdetails information into json object and sending it to content module.
			 */
			String splitIp [] = getIp.split(":");
			JSONObject obj=new JSONObject();
			obj.put("ipAddress",splitIp[0]);
			obj.put("username",userName.getText());
			obj.put("image",encodedImage);
			String userDetails = obj.toString();
			IContentCommunicator communicator = ContentFactory.getContentCommunicator();
			communicator.initialiseUser(userDetails);
			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"Userdetails and image have been sent to content module to initialise user"
			);
			
			/**
			 * Subscribing for notifications from processing and content module.
			 */
			IContentNotificationHandler contentSubscribe = new ContentNotificationHandler();
			IChanges processingSubscribe = new PixelListener();
			user.subscribeForChanges("UI", processingSubscribe);
			communicator.subscribeForNotifications("UI",contentSubscribe );
			
			/**
			 * Sending the boardId to canvas controller.
			 */
			controller.setBoardId(returnval);
			
			/**
			 * Closing the Start session window.
			 */
			((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

		} catch (Exception e) {
			logger.log(
				ModuleID.UI,
				LogLevel.ERROR,
				"Error in opening canvas fxml page"
			);
		}
	}
}
