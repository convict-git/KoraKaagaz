package processing.shape;

import java.util.ArrayList;

import processing.ClientBoardState;
import processing.CurveBuilder;
import processing.boardobject.BoardObject;
import processing.boardobject.CreateOperation;
import processing.boardobject.IBoardObjectOperation;
import processing.utility.*;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;

/**
 * Static methods for constructing board objects from shapes and
 * performing the necessary updates to the global board state of
 * the current board
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class BoardObjectBuilder {
	/**
	 * Constructs a Circle based on the center, radius and intensity
	 * of each pixel, performs clean up and then makes necessary
	 * changes to the board state
	 * 
	 * @param center Center of the circle to be constructed
	 * @param radius Radius of the circle to be constructed
	 * @param intensity Intensity of each pixel of the circle
	 * @return the circle as a Board Object
	 */
	public static BoardObject drawCircle (
	    Position center,
	    Radius radius,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Circle"
		);
		
		// Get arraylist of pixels of the circle
		ArrayList<Pixel> circlePixels = 
			CircleDrawer.drawCircle(center, radius, intensity);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Circle Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		circlePixels = ShapeHelper.postDrawProcessing(
			circlePixels,
			ClientBoardState.brushSize,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Circle Post Processing Completed"
		);
		
		// Draw the circle's pixels
		return drawPixels(circlePixels);
	}
	
	/**
	 * Constructs a Filled Circle based on the center,
	 * radius and intensity of each pixel, performs clean up
	 * and then makes necessary changes to the board state
	 * 
	 * @param center Center of the filled circle to be constructed
	 * @param radius Radius of the filled circle to be constructed
	 * @param intensity Intensity of each pixel of the filled circle
	 * @return the filled circle as a Board Object
	 */
	public static BoardObject drawCircleFill (
	    Position center,
	    Radius radius,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Filled Circle"
		);
		
		// Get arraylist of pixels of the filled circle
		ArrayList<Pixel> circleFillPixels = 
			CircleDrawer.drawCircleFill(center, radius, intensity);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Filled Circle Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		circleFillPixels = ShapeHelper.postFillProcessing(
			circleFillPixels,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Filled Circle Post Processing Completed"
		);
		
		// Draw the filled circle's pixels
		return drawPixels(circleFillPixels);
	}
	
	/**
	 * Constructs an Axis Aligned Rectangle based on the top left
	 * and bottom right coordinates
	 * 
	 * @param topLeft Coordinate of the top left pixel of the rectangle
	 * @param bottomRight Coordinate of the bottom right pixel of the rectangle
	 * @param intensity Intensity of each pixel of the rectangle
	 * @return the rectangle as a Board Object
	 */
	public static BoardObject drawRectangle (
	    Position topLeft,
	    Position bottomRight,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
	
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Rectangle"
		);
		
		// Get arraylist of pixels of the rectangle
		ArrayList<Pixel> rectPixels = RectangleDrawer.drawRectangle(
			topLeft, 
			bottomRight, 
			intensity
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Rectangle Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		rectPixels = ShapeHelper.postDrawProcessing(
			rectPixels,
			ClientBoardState.brushSize,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Rectangle Post Processing Completed"
		);
		
		// Draw the rectangle's pixels
		return drawPixels(rectPixels);
	}
	
	/**
	 * Constructs an Axis Aligned Filled Rectangle based on the top left
	 * and bottom right coordinates
	 * 
	 * @param topLeft Coordinate of the top left pixel of the filled rectangle
	 * @param bottomRight Coordinate of the bottom right pixel of the filled rectangle
	 * @param intensity Intensity of each pixel of the filled rectangle
	 * @return the filled rectangle as a Board Object
	 */
	public static BoardObject drawRectangleFill (
	    Position topLeft,
	    Position bottomRight,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
	
		logger.log(
			ModuleID.PROCESSING,
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Filled Rectangle"
		);
		
		// Get arraylist of pixels of the filled rectangle
		ArrayList<Pixel> rectFillPixels = RectangleDrawer.drawRectangleFill(
			topLeft, 
			bottomRight, 
			intensity
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Filled Rectangle Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		rectFillPixels = ShapeHelper.postFillProcessing(
			rectFillPixels,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Rectangle Post Processing Completed"
		);
		
		// Draw the filled rectangle's pixels
		return drawPixels(rectFillPixels);
	}
	
	/**
	 * Constructs a Triangle based on the three vertices provided
	 * 
	 * @param vertA First vertex of the triangle
	 * @param vertB Second vertex of the triangle
	 * @param vertC Third vertex of the triangle
	 * @param intensity Intensity of each pixel of the triangle
	 * @return the triangle as a Board Object 
	 */
	public static BoardObject drawTriangle (
	    Position vertA,
	    Position vertB,
	    Position vertC,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Triangle"
		);
		
		// Get arraylist of pixels of the triangle
		ArrayList<Pixel> trianglePixels = TriangleDrawer.drawTriangle(
			vertA, 
			vertB,
			vertC,
			intensity
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Triangle Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		trianglePixels = ShapeHelper.postDrawProcessing(
			trianglePixels,
			ClientBoardState.brushSize,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Triangle Post Processing Completed"
		);
		
		// Draw the triangle's pixels
		return drawPixels(trianglePixels);
	}
	
	/**
	 * Constructs a Line Segment based on the two endpoints of the
	 * line segment which are provided as input
	 * 
	 * @param pointA First end point of the line segment
	 * @param pointB Second end point of the line segment
	 * @param intensity Intensity of each pixel of the segment
	 * @return the line segment as a Board Object 
	 */
	public static BoardObject drawSegment (
	    Position pointA,
	    Position pointB,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Drawing The Line"
		);
		
		// Get arraylist of pixels of the triangle
		ArrayList<Pixel> segmentPixels = LineDrawer.drawSegment(
			pointA, 
			pointB,
			intensity
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Line Pixels Constructed"
		);
		
		// Perform post processing on the pixels
		segmentPixels = ShapeHelper.postDrawProcessing(
			segmentPixels,
			ClientBoardState.brushSize,
			ClientBoardState.boardDimension
		);
		
		logger.log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Line Post Processing Completed"
		);
		
		// Draw the line segment's pixels
		return drawPixels(segmentPixels);
	}
	
	/**
	 * Draws the provided pixels and constructs a board object
	 * using them 
	 * 
	 * @param pixels ArrayList of pixels
	 * @return Board Object constructed from the provided arraylist of pixels
	 */
	private static BoardObject drawPixels(ArrayList<Pixel> pixels) {
		// Get current time
		Timestamp currTime = Timestamp.getCurrentTime();
		
		// Construct a new Object ID
		ObjectId objectId = new ObjectId(
			ClientBoardState.userId,
			currTime
		);
	
		// Draw the filled circle and return the Board Object
		return CurveBuilder.drawCurve(
			pixels,
			(IBoardObjectOperation) new CreateOperation(),
			objectId,
			currTime,
			ClientBoardState.userId,
			null,
			false
		);
	}
}
