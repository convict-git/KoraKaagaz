package processing;

import java.util.*;
import processing.utility.*;
import processing.BoardState;
import processing.board_object.*;
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
				
		BoardObject curveObj = new BoardObject(pixels, newobjectId, newtimestamp, newuserId, reset);
		
		//There is no previous intensity
		//for a new object so it is null
		ArrayList<Pixel> prevpixel = new ArrayList<Pixel> ();
		prevpixel = prevPixelIntensity;
		
		//Insert BoardObject in the Map
		ClientBoardState.maps.insertObjectIntoMaps(curveObj);
		
		//Push BoardObject in the stack
		stackUtil(curveObj);
		
		return curveObj;
	}
	
	private static void stackUtil(BoardObject newObj) {
		
		UndoRedo.pushIntoStack(newObj);
	
	}
	
}