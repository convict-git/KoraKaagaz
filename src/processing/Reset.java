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
		
		Intensity whiteIntensity = new Intensity(255,255,255);
		
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		ArrayList<Position> positions = ClientBoardState.maps.getPositions();
		
		/* Traverse through screen and assign position
		 * and white intensity to each pixel
		 */
		for(Position pos: positions) {
			pixels.add(new Pixel(pos, whiteIntensity));
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
		
		CommunicateChange.provideChanges(null, pixels);
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] " +
				"Screen Reset done Successfully"
				);
		
		return resetObject;
		
	}
}
