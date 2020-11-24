package processing;

import java.util.ArrayList;

import infrastructure.validation.logger.*;
import processing.boardobject.*;
import processing.utility.*;
/**
 * Reset Class clears the screen
 * enables undo and redo
 * but not select delete and rotate
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 */
public class Reset {
	
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	/*
	 *  The function makes a white filled rectangle object
	 * covers the whole screen with undo enabled
	 * 
	 * @param Reset flag so reset object is not selected
	 * deleted rotated color changed
	 */
	public static BoardObject screenReset(UserId newUserId,boolean reset) {
		
		/* Get BoardDimensions from UI */
		Dimension dimension = ClientBoardState.boardDimension;
		
		int rows = dimension.numRows;
		
		int cols = dimension.numCols;
		
		Intensity whiteIntensity = new Intensity(255,255,255);
		
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		/* Traverse through screen and assign position
		 * and white intensity to each pixel
		 */
		for(int i = 0; i < rows; i++) {
			
			for(int j = 0; j < cols; j++) {
				
				// Each position is chosen for a pixel
				Position whitePosition = new Position(i,j);
				
				// White intensity and position is assigned to the pixel
				Pixel whitePixel = new Pixel(whitePosition, whiteIntensity);
				
				// Add pixel to the ArrayList
				pixels.add(whitePixel);
				
			}
		}
		
		// Get current timestamp for ObjectId
		Timestamp newTimestamp = Timestamp.getCurrentTime();
		
		// Assign new ObjId to reset BoardObject
		ObjectId newObjId = new ObjectId(newUserId, newTimestamp);
		
		// An empty array is passed in previous pixels
		ArrayList<Pixel> prevPixels = new ArrayList<Pixel>();
		
		// Assign CREATE operation to the BoardOperation
		IBoardObjectOperation newBoardOp = new CreateOperation();
		
		/*
		 * Create white filled rectangle as an BoardObject
		 * using drawCurve method it is pushed to stack
		 * and added to client maps 
		 */
		
		BoardObject resetObject = CurveBuilder.drawCurve(
				pixels,
				newBoardOp,
				newObjId,
				newTimestamp,
				newUserId,
				prevPixels,
				reset
		);
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] " +
				"Screen Reset done Successfully"
				);
		
		return resetObject;
		
	}
}
