package processing;

import java.util.*;
import processing.utility.*;
import processing.BoardState;
import processing.boardobject.*;
/**
 * CurveBuilder Class to 
 * convert curves in BoardObject
 * @author Satchit Desai
 * 
 * @reviewer Himanshu Jain
 */
public class CurveBuilder {
	public static BoardObject drawCurve(
			ArrayList<Pixel> pixels,
			IBoardObjectOperation newboardOp,
			ObjectId newobjectId,
			Timestamp newtimestamp,
			UserId newuserId,
			ArrayList<Pixel> prevPixelIntensity,
			Boolean reset
			) {
				
		BoardObject curveObj = new BoardObject(
				pixels,
				newobjectId,
				newtimestamp,
				newuserId,
				reset
				);
				
		//set operation of the created object
		curveObj.setOperation(newboardOp);
		
		//Insert BoardObject in the Map
		ClientBoardState.maps.insertObjectIntoMaps(curveObj);
		
		//Push BoardObject in undo stack
		pushIntoStack(curveObj);
		
		return curveObj;
	}

	public static BoardObject eraseCurve(
			ArrayList<Position> position,
			IBoardObjectOperation newboardOp,
			ObjectId newobjectId,
			Timestamp newtimestamp,
			UserId newuserId,
			Boolean reset
			) {
		int pixelSize = position.size();
		
		ArrayList<Pixel> pixel = new ArrayList<Pixel>();
		
		//Give each channel white intensity
		int r = 255;
		int g = 255;
		int b = 255;
		
		for(int i = 0; i < pixelSize; i++)
		{
			//White intensity object
			Intensity whiteIntensity = new Intensity(r,g,b);
			
			Position whitePosition = new Position(position.get(i));
			
			// Make a pixel for each position
			Pixel whitePixel = new Pixel (whitePosition, whiteIntensity);
			
			// Pixels of Eraser object 
			pixel.add(whitePixel);
		}
		
		BoardObject eraseObj = new BoardObject(
				pixel,
				newobjectId,
				newtimestamp,
				newuserId,
				reset
				);
		
		//set operation of the created object
		eraseObj.setOperation(newboardOp);
		
		//Insert BoardObject in the map
		ClientBoardState.maps.insertObjectIntoMaps(eraseObj);
		
		//Push BoardObject in undo stack
		pushIntoStack(eraseObj);
		
		return eraseObj;
	}
	
	private static void pushIntoStack(BoardObject newObj) {
		UndoRedo.pushIntoStack(newObj);
	}	
}