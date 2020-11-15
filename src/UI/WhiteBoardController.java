package UI;
import infrastructure.validation.logger.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import javax.imageio.ImageIO;
import processing.*;

/***
 * @Author	 : Shiva Dhanush
 * This controller handles operations on going from startsession page to canvas page.
 * It takes the image selected by user and encodes it into a base64 string.
 * It sends user details to processing module and content module.
 ***/

import org.json.simple.JSONObject;
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

	public void getImage (ActionEvent event) throws Exception
	{
		/** This method takes in the image of jpg type and encodes it into a string. */
		FileChooser fc = new FileChooser();
		File f = fc.showOpenDialog(null);
		if(f!=null)
		{   
			/** Byte array of image file taken as input is encoded with Base64. */
			BufferedImage originalImage = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( originalImage, "jpg", baos );
			baos.flush();
			byte[] imagebyte = baos.toByteArray();
			encodedImage = Base64.getEncoder().encodeToString(imagebyte);
			baos.close();
		}
	}
	
	public void startSession (ActionEvent event) throws Exception
	{   
		/** Sending the data filled to the processing module */
		Processor processor = ProcessingFactory.getProcessor() ;
		IUser user = processor;  
		String returnval= user.giveUserDetails(userName.getText(),ipAddress.getText(),boardId.getText());
		logger.log(ModuleID.UI, LogLevel.SUCCESS, "Userdetails to processing module have been sent successfully");
  
		/** Keeping the userdetails information into json object and sending it to content module. */
		JSONObject obj=new JSONObject();    
		obj.put("ipAddress",ipAddress.getText());  
		obj.put("username",userName.getText());
		obj.put("image",encodedImage);
		String userDetails = obj.toString();
		IContentCommunicator communicator = new ContentCommunicator();
		communicator.initialiseUser(userDetails);
		logger.log(ModuleID.UI, LogLevel.SUCCESS, "Userdetails and image have been sent to content module to initialise user");
        
		/** Closing the Start session window and opening the canvas.fxml page. */
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("canvas.fxml"));
		Scene scene = new Scene(root,600,800);
		primaryStage.setScene(scene);
		primaryStage.show(); 
		logger.log(ModuleID.UI, LogLevel.SUCCESS, "Opening the canvas fxml page.");
	} 
}
