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
    
    /** Delete Button, allows one to delete the selected object */
    @FXML
    private Button deleteButton;
    
    /** 
     * Rotate ComboBox Button, allows one to select
     * angle of	rotation
     */
    @FXML
    private ComboBox<String> rotateButton;
    
    /** The Canvas Object */
	@FXML
	private Canvas canvas;
	
    @FXML
    private ColorPicker colorPicker;
	
    private GraphicsContext gcF, gcB;
    
    private double x1, y1, x2, y2, x3, y3;
    
    private Color color;
    
    /** Current Mode of the UI */
    private CurrentMode currentMode = CurrentMode.NO_MODE;
    
    /** Is an Object Selected ? */
    private boolean isObjectSelected = false;
    
    /**
     * Selected Pixel's Previous Pixel Values, If no object is
     * selected, then this would be null
     */
    private ArrayList<Pixel> selPrevPixels = null;
    
    /** Dimension of the UI Canvas */
    private final Dimension dimension = new Dimension(720, 1200);

    @FXML
	public void eraserClicked(ActionEvent e) {
		
	}
    
    /**
     * When the cursor button is clicked, we enter the Cursor Mode
     * and the cursor changes to '+' (Crosshair Cursor)
     * 
     * @param cursorButtonClick
     */
    @FXML
   	public void cursorClicked(ActionEvent cursorButtonClick) {
    	synchronized(this) {
	   		currentMode = CurrentMode.CURSOR_MODE;
	   		canvas.setCursor(Cursor.CROSSHAIR);
    	}
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
    
    /**
     * Choose a Color from the Color Picker Object, if in cursor mode,
     * and an object was selected, then updates the color of the object
     * 
     * @param actionEvent Event representing the picking of a color
     */
    @FXML
    void chooseColor(ActionEvent actionEvent) {
    	
    	synchronized(this) {

	    	// Get the logger
	    	ILogger logger = LoggerFactory.getLoggerInstance();
	    	
	    	// Get the chosen color
	    	Color color = colorPicker.getValue();
	    		
	    	// If in Cursor Mode, then perform the color
	    	// change operation
	    	if(
				currentMode == CurrentMode.CURSOR_MODE
				&&
				isObjectSelected
	    	) {
	    		
	    		logger.log(
					ModuleID.UI, 
					LogLevel.INFO, 
					"Cursor Mode Active: Updating Color of Selected Object"
				);
	    		
	    		// Construct an Intensity object using the chosen color
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
	    		
	    		// Perform Color Change
	    		ProcessingFactory
	    			.getProcessor()
	    			.colorChange(intensity);
	    		
	    		logger.log(
					ModuleID.UI, 
					LogLevel.INFO, 
					"Color Change Performed"
				);
	    		
	    		// Unselect currently selected object
	        	updateSelectedPixels(null);
	    	}
	    	else // Not in cursor Mode or object not selected
	    		logger.log(
					ModuleID.UI, 
					LogLevel.INFO, 
					"Not in Cursor Mode or Object not selected"
				);
    	}
    }

    /** 
     * This is used for constructing a small square around the selected point
     * since it is possible that the user may make slight errors in choosing
     * a point. We assume that the user selected the entire small square, not
     * just a single point
     */
    private final int[] selRange = new int[] {-1, 0, 1};
    
    /**
     * When a point in the canvas in clicked, then this method is called.
     * If the UI is in Cursor Mode, we pass a small square around the
     * selected point to the processor so that we may get the object
     * corresponding to the selected pixels. We highlight the pixels of the
     * object by representing them with a specific color
     * 
     * @param canvasMouseClickEvent Mouse Click Event which lead to this
     * function being called
     */
    @FXML
    void clickCanvas(MouseEvent canvasMouseClickEvent) {
    	
    	synchronized(this) {
    	
	    	// Get the logger
	    	ILogger logger = LoggerFactory.getLoggerInstance();
	    	
	    	// If current mode is not the Cursor Mode, return
	    	if(currentMode != CurrentMode.CURSOR_MODE) {
	    		logger.log(
					ModuleID.UI, 
					LogLevel.INFO, 
					"Unable to Select Object: Not in Cursor Mode"
				);
		    	return;
	    	}
	    	
	    	// Get approximate selected row and column coordinate
	    	int rowCoord = (int) Math.round(canvasMouseClickEvent.getY());
	    	int colCoord = (int) Math.round(canvasMouseClickEvent.getX());
	    	
	    	// selPosition would represent a small region around the
	    	// approximate selected position
	    	ArrayList<Position> selPosition = new ArrayList<Position>();
	    	
	    	// Construct selPosition by building a small square as the
	    	// selected region
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
	    	
	    	// Get Selected Object Pixels from the Processor
	    	ArrayList<Pixel> objectPixels = 
	    		ProcessingFactory
					.getProcessor()
					.select(selPosition);
	    	
	    	logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Got Pixels from Processing Module"
			);
	    	
	    	// Update Selected Pixels
	    	updateSelectedPixels(objectPixels);
    	}
    }
    
    /**
     * When the delete button is clicked, this method is called. If the
     * UI is in Cursor Mode, then the selected object is deleted (if any
     * object was selected)
     * 
     * @param deleteButtonClickEvent Event corresponding to the clicking
     * of the delete button
     */
    @FXML
    void deleteObject(ActionEvent deleteButtonClickEvent) {
    	
    	synchronized(this) {
    	
	    	// Get the logger
	    	ILogger logger = LoggerFactory.getLoggerInstance();
	    	
	    	// If current mode is not the Cursor Mode, return
	    	if(currentMode != CurrentMode.CURSOR_MODE) {
	    		logger.log(
	    			ModuleID.UI, 
	    			LogLevel.INFO, 
	    			"Deletion cannot be performed: Not in Cursor Mode"
	    		);
	    		return;
	    	}
	    	
	    	// If no object is selected, return
	    	if(!isObjectSelected) {
	    		logger.log(
	    			ModuleID.UI, 
	    			LogLevel.INFO, 
	    			"Deletion cannot be performed: No object Selected"
	    		);
	    		return;
	    	}
	    	
	    	logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Performing Deletion"
			);
	    	
	    	// Perform Delete Operation
	    	ProcessingFactory
	    		.getProcessor()
	    		.delete();
	    	
	    	logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Deletion Performed"
			);
	    	
	    	// Unselect currently selected object
	    	updateSelectedPixels(null);
    	}
    }
    
    /**
     * When the Rotate Button (ComboBox) is clicked, this method
     * is called. It rotates the object which is selected (if an
     * object is selected)
     * 
     * @param rotateButtonClickEvent Event corresponding to the clicking
     * of the rotate button (ComboBox)
     */
    @FXML
    void rotateObject(ActionEvent rotateButtonClickEvent) {
    	
    	synchronized(this) {
    	
	    	// Get the logger
	    	ILogger logger = LoggerFactory.getLoggerInstance();
	    	
	    	// If Current Mode is not Cursor Mode, then return
	    	if(currentMode != CurrentMode.CURSOR_MODE) {
	    		logger.log(
	    			ModuleID.UI, 
	    			LogLevel.INFO, 
	    			"Rotate cannot be performed: Not in Cursor Mode"
	    		);
	    		return;
	    	}
	    	
	    	// If no object is selected, return
	    	if(!isObjectSelected) {
	    		logger.log(
	    			ModuleID.UI, 
	    			LogLevel.INFO, 
	    			"Deletion cannot be performed: No object Selected"
	    		);
	    		return;
	    	}
	    	
	    	// Chosen Angle String
	    	String chosenAngleString = rotateButton.getValue();
	    	
	    	// Chosen Angle as a double value (to be filled)
	    	double angleCCW;
	    	
	    	// Get angle from String
	    	if(chosenAngleString == "90")
	    		angleCCW = 90;
	    	
	    	else if(chosenAngleString == "180")
	    		angleCCW = 180;
	    	
	    	else
	    		angleCCW = 270;
	    	
	    	logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Performing Rotation with Chosen Angle: " + angleCCW
			);
	    	
	    	// Perform rotation using the selected angle
			ProcessingFactory
				.getProcessor()
				.rotate(new Angle(angleCCW));	
			
			logger.log(
				ModuleID.UI, 
				LogLevel.INFO, 
				"Rotation Performed: " + angleCCW
			);
			
			// Unselect currently selected object
	    	updateSelectedPixels(null);
    	}
    }
    
    /** Selected Object's pixels would be made into this color */
    private final Color HIGHLIGHT_COLOR = Color.color(0.0, 1.0, 1.0);
    
    /**
     * Updates the Selected Pixels by highlighting them, and replaces
     * previous selected (highlighted) pixels with their original value
     * 
     * @param selectedPixels The Selected Pixels
     */
    public void updateSelectedPixels(
    	ArrayList<Pixel> selectedPixels
    ) {
    	synchronized(this) {
    	
	    	// Update previous selected pixels to their original value
	    	updatePrevSelectedPixels();
	    	
	    	// If no pixels are selected currently, then set the
	    	// members accordingly
	    	if(selectedPixels == null || selectedPixels.size() == 0) {
	    		isObjectSelected = false;
	    		selPrevPixels = null;
	    	}
	    	else { // Else update selected prev pixel values to current
	    		   // and write highlighted object pixels to canvas
	    		
	    		isObjectSelected = true;
	    		selPrevPixels = new ArrayList<Pixel>(selectedPixels);
	    		
	    		// Highlight currently selected pixels 
	    		for(Pixel pixel : selectedPixels) {
	    			Position position = pixel.position;
	    			canvas
	    				.getGraphicsContext2D()
	    				.getPixelWriter()
	    				.setColor(
	    					position.c, 
	    					position.r,
	    					HIGHLIGHT_COLOR
	    				);
	    		}
	    	}
    	}
    }
    
    /** Replaces Previous Selected Pixels with their original values */
    public void updatePrevSelectedPixels() {
    	
    	synchronized(this) {
    		
	    	// If no object was selected, return
	    	if(!isObjectSelected)
	    		return;
	    	
	    	// Set previous selected pixels to their original value
	    	for(Pixel pixel : selPrevPixels) {
	    		
	    		// Convert to double value between 0.0 and 1.0
	    		Color color = Color.color(
	    			(double) pixel.intensity.r / 255.0,
	    			(double) pixel.intensity.g / 255.0,
	    			(double) pixel.intensity.b / 255.0
	    		);
	    		
	    		// Get position from pixel
	    		Position position = pixel.position;
	    		
	    		// Update the canvas
				canvas
					.getGraphicsContext2D()
					.getPixelWriter()
					.setColor(
						position.c, 
						position.r,
						color
					);
	    	}
    	}
    }

    /**
     * Initialize the Canvas Controller
     * 
     * @see <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/Initializable.html">
     * Initializable Interface</a>
     * 
     * @param url The location used to resolve relative paths for the root object,
     * or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null
     * if the root object was not localized.
     */
	@Override
	public void initialize(
		URL url, 
		ResourceBundle resourceBundle
	) {
		
		// Get the logger
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		logger.log(
			ModuleID.UI, 
			LogLevel.INFO, 
			"Initializing Canvas Controller"
		);
		
		// Add drop down angles to the rotate Button ComboBox 
		rotateButton
			.getItems()
			.addAll("90", "180", "270");
	}
}
