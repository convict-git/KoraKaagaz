package processing;
import java.security.Timestamp;
import java.util.*;
import processing.utility.*;
import processing.BoardState;

/**
*
* @author Satchit Desai
* 
* @reviewer Himanshu Jain
*/
/* CurveBuilder Class to 
 * convert curves in BoardObject
 */


public class CurveBuilder {
	public static BoardObject drawCurve(
			ArrayList<Pixel> pixels,
			BoardObjectOperation newboardOp,
			ObjectId newobjectId,
			Timestamp newtimestamp,
			UserId newuserId,
			ArrayList<Pixel> prevPixelIntensity,
			Boolean reset
			) {
				
		BoardObject curveObj = new BoardObject();
		
		curveObj.pixels = pixels;
		
		//Update object of the new object
		curveObj.objectid = new ObjectId();
		
		curveObj.objectid = newobjectId;
		
		//Update timestamp of the new object
		curveObj.timestamp = new Timestamp();
		
		curveObj.timestamp = newtimestamp;
		
		//Update userid of the new object
		curveObj.userid = new UserId();
		
		curveObj.userid = newuserId;
		
		//There is no previous intensity
		//for a new object so it is null
		curveObj.prevpixel = new prevPixelIntensities();
		
		curveObj.prevpixel = prevPixelIntensity;
		
		//Set reset flag as false
		curveObj.resetFlag = new isReset();
		
		curveObj.resetFlag= reset;
		
		//Insert BoardObject in the Map
		insertObjectIntoMap(curveObj);
		
		//Push BoardObject in the stack
		stackUtil(curveObj);
		
		return curveObj;
	}
	
	private static void stackUtil(BoardObject newObj) {
		
		UndoRedo.pushIntoStack(newObj);
	
	}
	
}