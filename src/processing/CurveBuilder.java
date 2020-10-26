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
				
		//set operation of the created object
		curveObj.setOperation(newboardOp);
		
		//Set pixels of the created object
		curveObj.setPixels(pixels);
		
		//SetUserId of the created Object
		curveObj.setUserId(newuserId);
		
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