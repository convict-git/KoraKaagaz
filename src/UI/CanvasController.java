/***
 * @Author	 : Jaya Madhav, Sajith Kumar,Shiva Dhanush
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

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import infrastructure.validation.logger.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import processing.ProcessingFactory;
import processing.utility.Angle;
import processing.utility.Dimension;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;


public class CanvasController implements Initializable {
	
	@FXML
	public ComboBox<Integer> brushSize;
	
	@FXML
	private Button eraser;
	
	@FXML
	private Button brush;
	
	@FXML
	private Button cursorButton;
	
	@FXML
	private Button undo;
	
	@FXML
	private Button redo;
	
	@FXML
	private Button sendButton;
	
	@FXML
	private TextArea sendMessage;
	
	@FXML
	private VBox chatDisplayBox;
	
	@FXML
	private ScrollPane chatScroll;
	
    @FXML
    private Button line, rect, square, triangle, circle, oval;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private ComboBox<String> rotateButton;
    
	@FXML
	private Canvas canvas;
	
    @FXML
    private ColorPicker colorPicker;
	
    private GraphicsContext gcF, gcB;
    
    private double x1, y1, x2, y2, x3, y3;
    
    private Color color;
    
    private CurrentMode currentMode = CurrentMode.NO_MODE;
    
    private final Dimension dimension = new Dimension(720, 1200);

    @FXML
	public void eraserClicked(ActionEvent e) {
		
	}
    
    @FXML
   	public void cursorClicked(ActionEvent e) {
   		currentMode = CurrentMode.CURSOR_MODE;
   		canvas.setCursor(Cursor.CROSSHAIR);
   	}
    
    @FXML
   	public void undoClicked(ActionEvent e) {
   		
   	}
    
    @FXML
   	public void redoClicked(ActionEvent e) {
   		
   	}
    
    @FXML
   	public void brushClicked(ActionEvent e) {
   		
   	}
    @FXML
   	public void brushSizeChanged(ActionEvent e) {
   		
   	}
    
	@FXML
	public void sendButtonClicked(ActionEvent e) {
		
	}
	
    @FXML
    void circleSelected(ActionEvent event) {

    }

    @FXML
    void lineSelected(ActionEvent event) {

    }

    @FXML
    void ovalSelected(ActionEvent event) {

    }

    @FXML
    void rectSelected(ActionEvent event) {

    }

    @FXML
    void squareSelected(ActionEvent event) {

    }

    @FXML
    void triangleSelected(ActionEvent event) {

    }
    
    @FXML
    void mousePressed(MouseEvent e) {
    	
    }

    @FXML
    void mouseReleased(MouseEvent e) {
    	
    }
    
    @FXML
    void mouseDragged(MouseEvent e) {
    	
    }
    
    @FXML
    void chooseColor(ActionEvent e) {
    	ILogger logger = LoggerFactory.getLoggerInstance();
    	
    	Color color = colorPicker.getValue();
    		
    	if(currentMode == CurrentMode.CURSOR_MODE) {
    		
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Cursor Mode Active: Updating Color of Selected Object"
			);
    		
    		Intensity intensity =
	    		new Intensity(
	    			(int) Math.round(255 * color.getRed()), 
	    			(int) Math.round(255 * color.getGreen()), 
	    			(int) Math.round(255 * color.getBlue())
	    		);
    		
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Color chosen - "
				+ "red: " + intensity.r
				+ "green: " + intensity.g
				+ "blue: " + intensity.b
			);
    		
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Performing Color Change"
			);
    		
    		ProcessingFactory
    			.getProcessor()
    			.colorChange(intensity);
    		
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Color Change Performed"
			);
    	}
    	else
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Not in Cursor Mode"
			);
    }
    
    private final Intensity HIGHLIGHT_COLOR = new Intensity(0, 255, 255);
    
    private int[] selRange = new int[] {-1, 0, 1};
    
    @FXML
    void clickCanvas(MouseEvent mouseEvent) {
    	ILogger logger = LoggerFactory.getLoggerInstance();
    	
    	if(currentMode != CurrentMode.CURSOR_MODE) {
    		logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Unable to Select Object: Not in Cursor Mode"
			);
	    	return;
    	}
    	
    	int rowCoord = (int) Math.round(mouseEvent.getY());
    	int colCoord = (int) Math.round(mouseEvent.getX());
    	
    	ArrayList<Position> selPosition = new ArrayList<Position>();
    	
    	for(int r : selRange) {
    		for(int c : selRange) {
    			int row = rowCoord + r;
    			int col = colCoord + c;
    			
    			if(
					0 <= row && row < dimension.numRows
					&&
					0 <= col && col < dimension.numCols
    			)
    				selPosition.add(new Position(row, col));
    		}
    	}
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Getting Selected Object's Pixels from Processing Module"
		);
    	
    	ArrayList<Position> objectPixels = 
    		ProcessingFactory
				.getProcessor()
				.select(selPosition);
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Got Pixels from Processing Module"
		);
    	
    	if(objectPixels.size() == 0)
    		return;
    }
    
    @FXML
    void deleteObject(ActionEvent e) {
    	ILogger logger = LoggerFactory.getLoggerInstance();
    	
    	if(currentMode != CurrentMode.CURSOR_MODE) {
    		logger.log(
    			ModuleID.UI, 
    			LogLevel.INFO, 
    			"Deletion cannot be performed: Not in Cursor Mode"
    		);
    		return;
    	}
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Performing Deletion"
		);
    	
    	ProcessingFactory
    		.getProcessor()
    		.delete();
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Deletion Performed"
		);
    }
    
    @FXML
    void rotateObject(ActionEvent e) {
    	ILogger logger = LoggerFactory.getLoggerInstance();
    	
    	if(currentMode != CurrentMode.CURSOR_MODE) {
    		logger.log(
    			ModuleID.UI, 
    			LogLevel.INFO, 
    			"Rotate cannot be performed: Not in Cursor Mode"
    		);
    		return;
    	}
    		
    	String chosenAngleString = rotateButton.getValue();
    	double angleCCW;
    	
    	if(chosenAngleString == "90")
    		angleCCW = 90;
    	
    	else if(chosenAngleString == "180")
    		angleCCW = 180;
    	
    	else
    		angleCCW = 270;
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Chosen Angle: " + angleCCW
		);
    	
    	logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Performing Rotation: " + angleCCW
		);
    		
		ProcessingFactory
			.getProcessor()
			.rotate(new Angle(angleCCW));	
		
		logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Rotation Performed: " + angleCCW
		);
    }

	@Override
	public void initialize(
		URL url, 
		ResourceBundle resourceBundle
	) {
		rotateButton
			.getItems()
			.addAll("90", "180", "270");
	}
}
