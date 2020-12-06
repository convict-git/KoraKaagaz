/***
 * @Author	 : Jaya Madhav, Sajith Kumar, Shiva Dhanush, Anish Jain, Ahmed Z D, Devansh Rathore
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

import infrastructure.content.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

import org.json.JSONObject;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import processing.IChanges;
import processing.IUser;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.Dimension;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;
import processing.utility.Angle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class CanvasController implements Initializable{
	/***
	 * Declare and use all the javafx components and events here.
	 * sendButton is the id of 'SEND' button in Chatbox
	 * sendMessage is the id of text entered in the text area of Chatbox
	 * chatDisplayBox is the id of the component which displays the Chatbox
	 ***/

	int size;

	public static ILogger logger = LoggerFactory.getLoggerInstance();
	public static ObservableList<model> updateList = FXCollections.observableArrayList();			
	
	/**
	 * ComboBox below has a list of values for dropdown in brushSize.
	 */
	@FXML
	public ComboBox<Integer> brushSize;
	
	@FXML
	private Label boardId;
	public ObservableList<Integer> list = FXCollections.observableArrayList(2,4,6,8);
	
	@FXML
	private Button eraser;

	@FXML
	private Button brush;

	@FXML
	private Button undo;

	@FXML
	private Button redo;

	@FXML
	private Button reset;

	@FXML
	private Button sendButton;

	@FXML
	private TextArea sendMessage;

	@FXML
	private static VBox chatDisplayBox;

	@FXML
	private static ScrollPane chatScroll;

	@FXML
	private Button line,rect,square,triangle,circle,oval;

	@FXML
	private Canvas canvasF;

	@FXML
	private Canvas canvasB;

	private static GraphicsContext gc,gcForUpdate;

	private double x1,y1,x2,y2;

	private Color color;


    /**
	 * Delete Button, allows one to delete the selected object
	 */
    @FXML
    private Button deleteButton;

    /**
     * Rotate ComboBox Button, allows one to select
     * angle of	rotation
     */
    @FXML
    private ComboBox<String> rotateButton;

    @FXML
    private ColorPicker colorPicker;


    /**
	 * Current Mode of the UI
	 */
    private CurrentMode currentMode = CurrentMode.NO_MODE;

    /**
	 * Is an Object Selected ?
	 */
    private static boolean isObjectSelected = false;

    /**
     * Selected Pixel's Previous Pixel Values, If no object is
     * selected, then this would be null
     */
    private static ArrayList<Pixel> selPrevPixels = null;

    /**
	 * Dimension of the UI Canvas
	 */
    private final Dimension dimension = new Dimension(720, 1200);
    
    public void setBoardId(String id ) {
		synchronized(this) {
			boardId.setText(id);
		}
	}

    /**
	 * Function called when eraser is clicked.
	 */
	@FXML
	public void eraserClicked(ActionEvent e ) {
	    	synchronized(this) {
			Shapes.defaultSelected();
	    		Brush.defaultSelected();
	    		Brush.erasorSelected =true;
	    		currentMode = CurrentMode.ERASE_MODE;
				canvasF.setCursor(Cursor.DEFAULT);
	    	}
	}

	/**
	 * Function called when undo button is clicked.
	 */
	@FXML
	public void undoClicked(ActionEvent e) {

		try {

			Processor processor = ProcessingFactory.getProcessor();
			processor.undo();

			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Undo call succesful!"
			);
		} catch (Exception exp) {

			logger.log(
				ModuleID.UI,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Undo call failed!"
			);
		}

		return;
	}

	/**
	 * Function called when redo button is clicked.
	 */
	@FXML
	public void redoClicked(ActionEvent e) {

		try {

			Processor processor = ProcessingFactory.getProcessor();
			processor.redo();

			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Redo call succesful!"
			);
		} catch (Exception exp) {

			logger.log(
				ModuleID.UI,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Redo call failed!"
			);
		}

		return;
	}

	/**
	 * Function called when brush is clicked.
	 */
	@FXML
   	public void brushClicked(ActionEvent e) {
	    	synchronized(this) {
	    		Shapes.defaultSelected();
	    		Brush.defaultSelected();
	    		Brush.brushSelected =true;
	    		currentMode = CurrentMode.BRUSH_MODE;
				canvasF.setCursor(Cursor.DEFAULT);
	    	}
   	}

	/**
	 * Function called when reset button is clicked.
	 */
	public void resetClicked(ActionEvent e) {

		try {

			Processor processor = ProcessingFactory.getProcessor();
			processor.reset();

			logger.log(
				ModuleID.UI,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Reset call succesful!"
			);
		} catch (Exception exp) {

			logger.log(
				ModuleID.UI,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Reset call failed!"
			);
		}

		return;
   	}

	/**
	 * Function called when brush size is changed.
	 */
	@FXML
   	public void brushSizeChanged(ActionEvent e ) {
	    	synchronized(this) {
	    		size = (int)brushSize.getValue();
	    		Brush.sizeSelected =true;
	    		currentMode = CurrentMode.BRUSH_MODE;
				canvasF.setCursor(Cursor.DEFAULT);
	    	}
	}

	/**
	 * This function is called when leave session button is clicked.
	 * It notifies processing,content module about exit and closes the window.
	 * @param e This event is clicking Leave session button.
	 * @returns nothing
	 */
	@FXML
	public void leaveSession(ActionEvent e ) {
		synchronized(this) {
			infrastructure.content.IContentCommunicator communicator = ContentFactory.getContentCommunicator();

			//Notifying content module.
			communicator.notifyUserExit();
			logger.log(ModuleID.UI, LogLevel.SUCCESS, "Notified content module about exiting of user");

			//Notifying to Stop board session
			Processor processor = ProcessingFactory.getProcessor() ;
			IUser user = processor;
			user.stopBoardSession();
			logger.log(ModuleID.UI, LogLevel.SUCCESS, "Notified Processing module to stop board session.");
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
		}
	}

	/**
	 * Function to get the send button of the chatbox
	 * @param : none
	 * @return : sendButton - button clicked by user to send message
	 */
	public Button getSendButton() {
		synchronized(this) {
			return this.sendButton;
		}
	}
	
	/**
	 * Function to get the text area field of the chatbox
	 * @param : none
	 * @return : sendMessage - message entered by user in text box
	 */
	public TextArea getSendMessage() {
		synchronized(this) {
		return this.sendMessage;
		}
	}
	
	/**
	 * Function to get the chat display box of the chatbox
	 * @param : none
	 * @return : chatDisplayBox - chat display box to display message
	 */
	public VBox getChatDisplayBox() {
		synchronized(this) {
		return this.chatDisplayBox;
		}
	}

	/**
	 * Function to get the scroll pane of the chatbox
	 * @param : none
	 * @return : chatScroll - chat scroll pane to display message
	 */
	public ScrollPane getChatScroll() {
		synchronized(this) {
		return this.chatScroll;
		}
	}

	/**
	 * Javafx event handling the changes after clicking the 'SEND' button in the ChatBox.
	 * @param : e - ActionEvent e which is button click event
	 * @return : none
	 */
	@FXML
	public void sendButtonClicked(ActionEvent e ) {
		String message = sendMessage.getText();
		Chatbox.buttonClick(e,message,chatDisplayBox,chatScroll);
		sendMessage.setText(null);
	}

	/**
	 * This method will store the start position of mouse drag
	 * @param d
	 * @param e
	 */
	public void setStartPoint(double d, double e) {
		synchronized(this) {
	        x1 = (d);
	        y1 = (e);
		}
	}

	/**
	 * This method will store the end position of mouse drag
	 * @param d
	 * @param e
	 */
	public void setEndPoint(double d, double e) {
		synchronized(this) {
	        x2 = (d);
	        y2 = (e);
		}
	}

	/**
	 * This method will be called when circle is selected
	 * @param event
	 */
	@FXML
	void circleSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
			Shapes.defaultSelected();
			Shapes.circleselected = true;
			currentMode = CurrentMode.DRAWING_SHAPE_MODE;
			canvasF.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * This method will be called when line is selected
	 * @param event
	 */
	@FXML
	void lineSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
			Shapes.defaultSelected();
			Shapes.lineselected = true;
			currentMode = CurrentMode.DRAWING_SHAPE_MODE;
			canvasF.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * This method will be called when rectangle is selected
	 * @param event
	 */
	@FXML
	void rectSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
			Shapes.defaultSelected();
			Shapes.rectselected = true;
			currentMode = CurrentMode.DRAWING_SHAPE_MODE;
			canvasF.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * This method will be called when square is selected
	 * @param event
	 */
	@FXML
	void squareSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
			Shapes.defaultSelected();
			Shapes.squareselected = true;
			currentMode = CurrentMode.DRAWING_SHAPE_MODE;
			canvasF.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * This method will be called when triangle is selected
	 * @param event
	 */
	@FXML
	void triangleSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
			Shapes.defaultSelected();
			Shapes.triangleselected = true;
			currentMode = CurrentMode.DRAWING_SHAPE_MODE;
			canvasF.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * This method is called when the mouse is pressed on canvas.
	 * It records the position on canvas when the mouse is pressed
	 */
	@FXML
	void mousePressed(MouseEvent ev) {
		synchronized(this) {
			setStartPoint(ev.getX(), ev.getY());
		}
	}

	/**
	 * This method is called when the mouse is released on canvas.
	 * This method draws the shapes on front canvas when mouse is released after selecting a shape
	 */
	@FXML
	void mouseReleased(MouseEvent ev) {
		synchronized(this) {
			gc = canvasF.getGraphicsContext2D();
			setEndPoint(ev.getX(), ev.getY());
			color = colorPicker.getValue();

			if (Shapes.rectselected) {
				Shapes.drawPerfectRect(color,gc,x1, y1, x2, y2);
			}

			if (Shapes.circleselected) {
				Shapes.drawPerfectCircle(color,gc,x1, y1, x2, y2);
			}

			if (Shapes.lineselected) {
				Shapes.drawPerfectLine(color,gc,x1, y1, x2, y2);
			}

			if (Shapes.triangleselected) {
				Shapes.drawPerfectTriangle(color,gc,x1, y1, x2, y2);
			}

			if (Shapes.squareselected) {
				Shapes.drawPerfectSquare(color,gc,x1, y1, x2, y2);
			}

			if (Brush.brushSelected) {
				Shapes.defaultSelected();
				Brush.drawBrush(color,gc,x1, y1, x2, y2);
			}

			if (Brush.erasorSelected) {
				Shapes.defaultSelected();
				Brush.drawEraser(color,gc,x1, y1, x2, y2);
			}
		}
	}

	/**
	 * This method is called when the mouse is being dragged on canvas.
	 * This method creates the scaling effect on rear canvas for shapes when mouse is dragged
	 */
	@FXML
	void mouseDragged(MouseEvent ev) {
		synchronized(this) {
			gc = canvasB.getGraphicsContext2D();
			gc.setLineWidth(2);

			double x3=ev.getX();
			double y3=ev.getY();
			color = colorPicker.getValue();


			if (Shapes.rectselected) {
				Shapes.drawPerfectRectEffect(canvasB,color,gc,x1, y1, x3, y3);
			}

			if (Shapes.circleselected) {
				Shapes.drawPerfectCircleEffect(canvasB,color,gc,x1, y1, x3, y3);
			}

			if (Shapes.lineselected) {
				Shapes.drawPerfectLineEffect(canvasB,color,gc,x1, y1, x3, y3);
			}

			if (Shapes.triangleselected) {
				Shapes.drawPerfectTriangleEffect(canvasB,color,gc,x1, y1, x3, y3);
			}

			if (Shapes.squareselected) {
				Shapes.drawPerfectSquareEffect(canvasB,color,gc,x1, y1, x3, y3);
			}

			if (Brush.brushSelected) {
				ILogger logger = LoggerFactory.getLoggerInstance();

				if (Brush.sizeSelected==true) {
					logger.log(ModuleID.UI,LogLevel.SUCCESS,"Brush is selected");
				}
				else {
					logger.log(ModuleID.UI,LogLevel.INFO,"Select size of the brush");
				}
				if(Brush.sizeSelected==true) {
					gc = canvasF.getGraphicsContext2D();
					Shapes.defaultSelected();
					Brush.drawBrushEffect(canvasB,color, gc, x1, y1, x3, y3,size);
					gc.setLineWidth(2);
				}
				x1=x3;
				y1=y3;
			}

			if (Brush.erasorSelected) {
				ILogger logger = LoggerFactory.getLoggerInstance();

				if (Brush.sizeSelected==true) {
					logger.log(ModuleID.UI,LogLevel.SUCCESS,"Eraser is selected");
				}
				else {
					logger.log(ModuleID.UI,LogLevel.INFO,"Select size of the eraser");
				}
				gc = canvasF.getGraphicsContext2D();
				Shapes.defaultSelected();
				Brush.drawEraserEffect(canvasB,color, gc, x1, y1, x3, y3,size);
				gc.setLineWidth(2);
				x1=x3;
				y1=y3;
			}

		}
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
			Brush.defaultSelected();
			Shapes.defaultSelected();
			currentMode = CurrentMode.CURSOR_MODE;
			canvasF.setCursor(Cursor.CROSSHAIR);
		}
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
	    	if (
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
	    	else {
				// Not in cursor Mode or object not selected
	    		logger.log(
					ModuleID.UI,
					LogLevel.INFO,
					"Not in Cursor Mode or Object not selected"
				);
			}
    	}
    }

    /**
     * This is used for constructing a small square around the selected point
     * since it is possible that the user may make slight errors in choosing
     * a point. We assume that the user selected the entire small square, not
     * just a single point
     */
    private final int[] selRange = new int[] {-2,-1, 0, 1,2};

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
	    	if (currentMode != CurrentMode.CURSOR_MODE) {
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
	    	for (int r : selRange) {
	    		for (int c : selRange) {
	    			int row = rowCoord + r;
	    			int col = colCoord + c;

	    			if (
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
	    	ArrayList<Pixel> objectPixels = ProcessingFactory.getProcessor().select(selPosition);

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
	    	if (!isObjectSelected) {
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
	    	if (currentMode != CurrentMode.CURSOR_MODE) {
	    		logger.log(
	    			ModuleID.UI,
	    			LogLevel.INFO,
	    			"Rotate cannot be performed: Not in Cursor Mode"
	    		);
	    		return;
	    	}

	    	// If no object is selected, return
	    	if (!isObjectSelected) {
	    		logger.log(
	    			ModuleID.UI,
	    			LogLevel.INFO,
	    			"Rotation cannot be performed: No object Selected"
	    		);
	    		return;
	    	}

	    	// Chosen Angle String
	    	String chosenAngleString = rotateButton.getValue();

	    	// Chosen Angle as a double value (to be filled)
	    	double angleCCW;

	    	// Get angle from String
	    	if (chosenAngleString == "90")
	    		angleCCW = 90;

	    	else if (chosenAngleString == "180")
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
    private final static Color HIGHLIGHT_COLOR = Color.color(0.0, 1.0, 1.0);

    /**
     * Updates the Selected Pixels by highlighting them, and replaces
     * previous selected (highlighted) pixels with their original value
     *
     * @param selectedPixels The Selected Pixels
     */
    public static void updateSelectedPixels(ArrayList<Pixel> selectedPixels) {    	

    	// Update previous selected pixels to their original value
    	updatePrevSelectedPixels();

    	// If no pixels are selected currently, then set the
    	// members accordingly
    	if (selectedPixels == null || selectedPixels.size() == 0) {
    		isObjectSelected = false;
    		selPrevPixels = null;
    	}
    	else {
			// Else update selected prev pixel values to current
    		// and write highlighted object pixels to canvas
    		isObjectSelected = true;
    		selPrevPixels = new ArrayList<Pixel>(selectedPixels);

    		// Highlight currently selected pixels
    		for(Pixel pixel : selectedPixels) {
    			Position position = pixel.position;
    			gcForUpdate
    				.getPixelWriter()
    				.setColor(
    					position.c,
    					position.r,
    					HIGHLIGHT_COLOR
    				);
    		}
    	}
    }

    /**
	 * Replaces Previous Selected Pixels with their original values
	 */
    public static void updatePrevSelectedPixels() {

    	// If no object was selected, return
    	if (!isObjectSelected)
    		return;

    	// Set previous selected pixels to their original value
    	for (Pixel pixel : selPrevPixels) {

    		// Convert to double value between 0.0 and 1.0
    		Color color = Color.color(
    			(double) pixel.intensity.r / 255.0,
    			(double) pixel.intensity.g / 255.0,
    			(double) pixel.intensity.b / 255.0
    		);

    		// Get position from pixel
    		Position position = pixel.position;

    		// Update the canvas
    		gcForUpdate
				.getPixelWriter()
				.setColor(
					position.c,
					position.r,
					color
				);
    	}
    }
    
	/**
	 * updateChanges method updates the canvas with given pixels
	 * @param pixels:ArrayList of pixels that has to be updated on canvas
	 */
     public static void updateChanges(ArrayList<Pixel> pixels) {
		for(Pixel pix:pixels) {
			
			Position pos = pix.position;
			
			Color color = Color.color(
    			(double) pix.intensity.r / 255,
    			(double) pix.intensity.g / 255,
    			(double) pix.intensity.b / 255
    		);
			
			gcForUpdate.getPixelWriter().setColor(pos.c, pos.r, color);
		}
		
		logger.log(ModuleID.UI, LogLevel.SUCCESS, "Canvas Updated Successfuly");
	}

     public static void newChatMessage(String messageDetails) {
    	//Creating a JSON Object to store the message details
 		JSONObject obj = new JSONObject(messageDetails);

 		//Getting the username from messageDetails string
 		String username = obj.getString("username");

 		//Getting the message from messageDetails string
 		String message = obj.getString("message");

 		//Getting the image from messageDetails string
 		String image= obj.getString("image");

 		//Getting the time from messageDetails string
 		String time= obj.getString("time");

 		//Converting the image string to image bytes
 		byte[] imageByte = Base64.getDecoder().decode(image);

 		if (message != null) {
 			//Converting the image bytes to javafx image
 			Image imageJavafx = new Image(new ByteArrayInputStream(imageByte));

 			//Creating a view for the javafx image
 			ImageView viewImage = new ImageView(imageJavafx);

 			//Setting the dimensions of the image
 			viewImage.setFitHeight(25);
 			viewImage.setFitWidth(25);
 			viewImage.setPreserveRatio(true);

 			//Setting the styling of the image
 			viewImage.setStyle("-fx-border-color: black;-fx-background-radius: 10; -fx-border-radius: 10 10 10 10");
 			chatScroll.setFitToWidth(true);

 			//Creating a label to display the username
 			Label userNameDisplay = new Label(username);

 			//Setting styling for the username label
 			userNameDisplay.setStyle(" -fx-font: 10pt 'Corbel';-fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: orange;-fx-border-color: black;-fx-background-radius: 10 10 0 0; -fx-border-radius: 10 10 0 0");

 			//Creating a label to display the message received
 			Label msgLabel=new Label(message);
 			msgLabel.setMinHeight(Region.USE_PREF_SIZE);

 			//Setting the styling for the image
 			msgLabel.setStyle(" -fx-font: 14pt 'Corbel'; -fx-text-fill: black; -fx-background-color: orange;-fx-border-color: black;-fx-background-radius: 0 10 10 10; -fx-border-radius: 0 10 10 10");
 			msgLabel.setWrapText(true);
 			msgLabel.setTextAlignment(TextAlignment.JUSTIFY);

 			//Creating a VBox to store the labels created to display username and message
 			VBox usernameMessageDisplayBox = new VBox();
 			usernameMessageDisplayBox.getChildren().addAll(userNameDisplay,msgLabel);

 			//Creating a HBox to store the image and usernameMessageDisplayBox
 			HBox imageUsernameMessageDisplayBox=new HBox();
 			imageUsernameMessageDisplayBox.setSpacing(10);
 			imageUsernameMessageDisplayBox.getChildren().addAll(viewImage,usernameMessageDisplayBox);

 			//Setting the position of imageUsernameMessageDisplayBox
 			imageUsernameMessageDisplayBox.setAlignment(Pos.BASELINE_LEFT);

 			//Adding the imageUsernameMessageDisplayBox to the chatDisplayBox
 			chatDisplayBox.getChildren().add(imageUsernameMessageDisplayBox);

 			//Setting the spacing between each element in the chatDisplayBox
 			chatDisplayBox.setSpacing(10);

 			//Making sure the chatScroll is always at the bottom
 			chatScroll.setVvalue(1);

 			//log message on receiving message
 			logger.log(
 					ModuleID.UI,
 					LogLevel.SUCCESS,
 					"Message received from the user and displayed on the Chatbox"
 			);
 		}
 		else {
 			logger.log(
 					ModuleID.UI,
 					LogLevel.INFO,
 					"No message is received from the user"
 			);
 		}
     }
     
     public static void userExit(String username) {
    	//Creating a JSON Object
 		JSONObject obj = new JSONObject(username);

 		//Obtaining the username string
 		String userName = obj.getString("username");

 		//new user joined alert.
 		Alert alert = new Alert(AlertType.CONFIRMATION, "The user" + userName +  "left the canvas! ", ButtonType.OK);

 		//Displaying the alert
 		alert.show();

 		//Creating a new thread to display the alert for some seconds and dismiss
 		new Thread(new Runnable() {
 			@Override
 			public void run() {
 				try {
 					Thread.sleep(1000);  // seconds * 100
 				} catch (InterruptedException e) {
 					if (alert.isShowing()) {
 						alert.close();
 					}
 					e.printStackTrace();

 					//log message
 					logger.log(
 							ModuleID.UI,
 							LogLevel.ERROR,
 							e.toString());

 				} finally {
 					if (alert.isShowing()) {
 						alert.close();
 					}
 				}
 			}
 		}).run();

 		//log message on exit of user
 		logger.log(
 				ModuleID.UI,
 				LogLevel.SUCCESS,
 				"User"+ username +"successfully left the canvas"
 		);
     }
     
     public static void userJoined(String username) {
    	//Creating a JSON Object
 		JSONObject obj = new JSONObject(username);

 		//Obtaining the username string
 		String userName = obj.getString("username");

 		//new user joined alert.
 		Alert alert = new Alert(
 							AlertType.CONFIRMATION,
 							"The user" + userName +  "joined the canvas! ",
 							ButtonType.OK
 						);

 		//Displaying the alert
 		alert.show();

 		//Creating a new thread to display the alert for some seconds and dismiss
 		new Thread(new Runnable() {
 			@Override
 			public void run() {
 				try {
 					Thread.sleep(1000);  // seconds * 100
 				} catch (InterruptedException e) {
 					if (alert.isShowing()) {
 						 alert.close();
 					}
 					e.printStackTrace();

 					//log message
 					logger.log(
 							ModuleID.UI,
 							LogLevel.ERROR,
 							e.toString()
 						);
 				} finally {
 					if (alert.isShowing()) {
 					alert.close();
 					}
 				}
 			}
 		}).run();

 		//log message on entry of user
 		logger.log(
 				ModuleID.UI,
 				LogLevel.SUCCESS,
 				"User"+ username +"successfully entered the canvas"
 			);
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
	public void initialize( URL url,ResourceBundle resourceBundle) {

		// Get the logger
		ILogger logger = LoggerFactory.getLoggerInstance();

		logger.log(
			ModuleID.UI,
			LogLevel.INFO,
			"Initializing Canvas Controller"
		);
		
		updateList.addListener(new ListChangeListener<model>() {			
			public void onChanged(Change<? extends model> change) {
				while(change.next()) {
					if(change.wasReplaced() || change.wasAdded() || change.wasUpdated()) {
						for(model newModel: change.getAddedSubList()) {
							System.out.println(newModel);
							if(newModel.getIdentifier() == UpdateMode.CONTENT_JOINED) {
								logger.log(
						 				ModuleID.UI,
						 				LogLevel.INFO,
						 				"user enter from listener"
						 			);
								userJoined(newModel.getMessage());
							}
							else if(newModel.getIdentifier() == UpdateMode.CONTENT_LEAVE) {
								System.out.println(newModel);								
								logger.log(
						 				ModuleID.UI,
						 				LogLevel.INFO,
						 				"user leave from listener"
						 			);
								userExit(newModel.getMessage());
							}
							else if(newModel.getIdentifier() == UpdateMode.CONTENT_MESSAGE) {
								System.out.println(newModel);								
								logger.log(
						 				ModuleID.UI,
						 				LogLevel.INFO,
						 				"chat message from listener"
						 			);
								newChatMessage(newModel.getMessage());
							}
							else if(newModel.getIdentifier() == UpdateMode.PROCESSING_CHANGES) {
								System.out.println(newModel);								
								logger.log(
						 				ModuleID.UI,
						 				LogLevel.INFO,
						 				"selectPixels from listener"
						 			);
								updateChanges(newModel.getPixelArray());
							}
							else if(newModel.getIdentifier() == UpdateMode.PROCESSING_SELECTED) {
								System.out.println(newModel);								
						 		logger.log(
						 				ModuleID.UI,
						 				LogLevel.INFO,
						 				"updatePixels from listener"
						 			);
								updateSelectedPixels(newModel.getPixelArray());
							}
						}
					}
				}
			}		
	});


		// Add drop down angles to the rotate Button ComboBox
		rotateButton
			.getItems()
			.addAll("90", "180", "270");

		//Grapics context object for updating pixels
		gcForUpdate = canvasF.getGraphicsContext2D();
		// The following code initializes the dropdown of brushSize.
		brushSize.setItems(list);
	}
}
