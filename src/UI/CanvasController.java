/***
 * @Author	 : Jaya Madhav, Sajith Kumar,Shiva Dhanush,Anish Jain
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
import infrastructure.validation.logger.*;
import infrastructure.content.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import processing.IUser;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CanvasController implements Initializable {
	/***
	 * Declare and use all the javafx components and events here.
	 * sendButton is the id of 'SEND' button in Chatbox
	 * sendMessage is the id of text entered in the text area of Chatbox
	 * chatDisplayBox is the id of the component which displays the Chatbox
	 ***/
	static ILogger logger = LoggerFactory.getLoggerInstance();
	/**
	 * ComboBox below has a list of values for dropdown in brushSize.
	 */
	@FXML
	public ComboBox<Integer> brushSize;
	ObservableList<Integer> list = FXCollections.observableArrayList(2,4,6,8);
	@FXML
	private Button eraser;
	@FXML
	private Button brush;
	@FXML
	private Button cursor;
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
	private VBox chatDisplayBox;
	@FXML
	private ScrollPane chatScroll;
	@FXML
	private Button line,rect,square,triangle,circle,oval;
	@FXML
	public static Canvas canvasF;
	@FXML
	private Canvas canvasB;
	@FXML
	private ColorPicker colorpicker;
	static GraphicsContext gc;
	double x1,y1,x2,y2,x3,y3;
	Color color;
	int size;

	
	/***
	 * Function called when eraser is clicked.
	 ***/
	@FXML
	public void eraserClicked(ActionEvent e ) {
	    	synchronized(this) {
			Shapes.defaultSelected();
	    		Brush.defaultSelected();
	    		Brush.erasorSelected =true;
	    	}
	}
	
	/***
	 * Function called when cursor is clicked.
	 ***/
	@FXML
	public void cursorClicked(ActionEvent e ) {
		Shapes.defaultSelected();
	}
	
	/***
	 * Function called when undo button is clicked.
	 ***/
	@FXML
	public void undoClicked(ActionEvent e ) {
		
	}
	
	/***
	 * Function called when redo button is clicked.
	 ***/
	@FXML
	public void redoClicked(ActionEvent e ) {
		
	}
	
	/***
	 * Function called when reset button is clicked.
	 ***/
	@FXML
   	public void brushClicked(ActionEvent e ) {
	    	synchronized(this) {
	    		Shapes.defaultSelected();
	    		Brush.defaultSelected();
	    		Brush.brushSelected =true;
	    	}
   	}
    

   	public void resetClicked(ActionEvent e ) {
   		
   	}
	
	/***
	 * Function called when brush is clicked.
	 ***/
	@FXML
	public void brushClicked(ActionEvent e ) {
		Shapes.defaultSelected();
	}
	
	/***
	 * Function called when brush size is changed.
	 ***/

	@FXML
   	public void brushSizeChanged(ActionEvent e ) {
	    	synchronized(this) {
	    		size = (int)brushSize.getValue();
	    		Brush.sizeSelected =true;
	    	}
   	}

	/***
	 * Function called when leave session button is clicked.
	 ***/
	@FXML
   	public void leaveSession(ActionEvent e ) {
    		synchronized(this) {
		    	/** This function notifies processing and content module that the user is exiting and then closes the canvas */
		    	infrastructure.content.IContentCommunicator communicator = ContentFactory.getContentCommunicator();
			communicator.notifyUserExit();
		    	logger.log(ModuleID.UI, LogLevel.SUCCESS, "Notified content module about exiting of user");
		     	
			/**Notifying to Stop board session */
			Processor processor = ProcessingFactory.getProcessor() ;
			IUser user = processor;  
			user.stopBoardSession();
			logger.log(ModuleID.UI, LogLevel.SUCCESS, "Notified Processing module to stop board session.");
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();  
	    	}
   	}

	/***
	 * Function to get the send button of the chatbox 
	 ***/
	public Button getSendButton() {
		synchronized(this) {
			return this.sendButton;
		}
	}
	
	/***
	 * Function to get the text area field of the chatbox
	 ***/
	public TextArea getsendMessage() {
		synchronized(this) {
			return this.sendMessage;
		}
	}
	
	/***
	 * Function to get the chat display box of the chatbox 
	 ***/
	public VBox getchatDisplayBox() {
		synchronized(this) {
			return this.chatDisplayBox;
		}
	}
	
	/***
	 * Function to get the scroll pane of the chatbox 
	 ***/
	public ScrollPane getchatScroll() {
		synchronized(this) {
			return this.chatScroll;
		}
	}
	
	/***
	 * Javafx event handling the changes after clicking the 'SEND' button in the ChatBox.
	 ***/
	@FXML
	public void sendButtonClicked(ActionEvent e ) {
		String message = sendMessage.getText();
		Chatbox.buttonClick(e,message,chatDisplayBox,chatScroll);
		sendMessage.setText(null);
	}

	/***
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
	
	/***
	 * This method will be called when circle is selected
	 * @param event
	 */
	@FXML
	void circleSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
	    		Shapes.defaultSelected(); 
	    		Shapes.circleselected = true;
		}
	}
	
	/***
	 * This method will be called when line is selected
	 * @param event
	 */
	@FXML
	void lineSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
	    		Shapes.defaultSelected(); 
	    		Shapes.lineselected = true;
		}
	}
	
	/***
	 * This method will be called when rectangle is selected
	 * @param event
	 */
	@FXML
	void rectSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
	    		Shapes.defaultSelected(); 
	    		Shapes.rectselected = true;
		}
	}
	
	/***
	 * This method will be called when square is selected
	 * @param event
	 */
	@FXML
	void squareSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
	    		Shapes.defaultSelected(); 
	    		Shapes.squareselected = true;
		}
    	}

	/***
	 * This method will be called when triangle is selected
	 * @param event
	 */
	@FXML
	void triangleSelected(ActionEvent event) {
		synchronized(this) {
			Brush.defaultSelected();
	    		Shapes.defaultSelected(); 
	    		Shapes.triangleselected = true;
		}
	}

	
	/***
	 * This method is called when the mouse is pressed on canvas.
	 * It records the position on canvas when the mouse is pressed
	 ***/
	@FXML
	void mousePressed(MouseEvent ev) {
		synchronized(this) {
			setStartPoint(ev.getX(), ev.getY());
		}
	}
	
	/***
	 * This method is called when the mouse is released on canvas.
	 * This method draws the shapes on front canvas when mouse is released after selecting a shape
	 ***/
	@FXML
	void mouseReleased(MouseEvent ev) {
		synchronized(this) {
		    	gc = canvasF.getGraphicsContext2D();
		    	setEndPoint(ev.getX(), ev.getY());
		    	color = colorpicker.getValue();
			if(Shapes.rectselected) {
				Shapes.drawPerfectRect(color,gc,x1, y1, x2, y2);
			}
			if(Shapes.circleselected) {
				Shapes.drawPerfectCircle(color,gc,x1, y1, x2, y2);
			}
			if(Shapes.lineselected) {
				Shapes.drawPerfectLine(color,gc,x1, y1, x2, y2);
			}
			if(Shapes.triangleselected) {
				Shapes.drawPerfectTriangle(color,gc,x1, y1, x2, y2);
			}
			if(Shapes.squareselected) {
				Shapes.drawPerfectSquare(color,gc,x1, y1, x2, y2);
			}
			if(Brush.brushSelected) {
				Shapes.defaultSelected(); 
				Brush.drawBrush(color,gcF,x1, y1, x2, y2);
			}
			if(Brush.erasorSelected) {
				Shapes.defaultSelected(); 
				Brush.drawEraser(color,gcF,x1, y1, x2, y2);
			}

		}
	}
	
	/***
	 * This method is called when the mouse is being dragged on canvas.
	 * This method creates the scaling effect on rear canvas for shapes when mouse is dragged
	 ***/
	@FXML
	void mouseDragged(MouseEvent ev) {
		synchronized(this) {
		    	gc = canvasB.getGraphicsContext2D();
		    	double x3=ev.getX();
		    	double y3=ev.getY();
		    	color = colorpicker.getValue();
			if(Shapes.rectselected) {
				Shapes.drawPerfectRectEffect(canvasB,color,gc,x1, y1, x3, y3);
			}
			if(Shapes.circleselected) {
				Shapes.drawPerfectCircleEffect(canvasB,color,gc,x1, y1, x3, y3);
			}
			if(Shapes.lineselected) {
				Shapes.drawPerfectLineEffect(canvasB,color,gc,x1, y1, x3, y3);
			}
			if(Shapes.triangleselected) {
				Shapes.drawPerfectTriangleEffect(canvasB,color,gc,x1, y1, x3, y3);
			}
			if(Shapes.squareselected) {
				Shapes.drawPerfectSquareEffect(canvasB,color,gc,x1, y1, x3, y3);
			}
			if(Brush.brushSelected) {
				ILogger logger = LoggerFactory.getLoggerInstance();
					
				if(Brush.sizeSelected==true) {
					logger.log(ModuleID.UI,LogLevel.SUCCESS,"Brush is selected");
				}
				else {
					logger.log(ModuleID.UI,LogLevel.INFO,"Select size of the brush");
				}
					
				Shapes.defaultSelected(); 
				Brush.drawBrushEffect(canvasB,color, gcF, x1, y1, x3, y3,size);
			}
			if(Brush.erasorSelected) {
				ILogger logger = LoggerFactory.getLoggerInstance();
					
				if(Brush.sizeSelected==true) {
					logger.log(ModuleID.UI,LogLevel.SUCCESS,"Eraser is selected");
				}
				else {
					logger.log(ModuleID.UI,LogLevel.INFO,"Select size of the eraser");
				}
					
				Shapes.defaultSelected(); 
				Brush.drawEraserEffect(canvasB,color, gcF, x1, y1, x3, y3,size);
			}
		}
	}
	
	/***
	 * updateChanges method updates the canvas with given pixels
	 * @param pixels
	 ***/
	public void updateChanges(ArrayList<Pixel> pixels) {
		synchronized(this) {
			gc = canvasF.getGraphicsContext2D();
			for (Pixel pix:pixels) {
	    		Intensity i= pix.intensity;
	    		Position p = pix.position;
	            	gc.setStroke(Color.rgb(i.r, i.g, i.b));
	            	gc.strokeRect(p.r,p.c,2,2);
			}
			logger.log(ModuleID.UI, LogLevel.SUCCESS, "Canvas Updated Successfuly");
		}

		
	}
	
	/***
	 * The following code initializes the dropdown of brushSize.
	 ***/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		brushSize.setItems(list);		
	}
}

