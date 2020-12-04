package processing;

import java.util.*;

import infrastructure.validation.logger.*;
import processing.utility.*;
import processing.boardobject.*;
/**
 * CurveBuilder Class to 
 * convert curves in BoardObject
 * @author Satchit Desai
 * 
 * @reviewer Himanshu Jain
 */
public class CurveBuilder {
	
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	/*
	 * create BoardObject from pixels
	 * add this object to client map
	 * push object to undo stack
	 */
	
	public static BoardObject drawCurve(
			ArrayList<Pixel> pixels,
			IBoardObjectOperation newboardOp,
			ObjectId newobjectId,
			Timestamp newtimestamp,
			UserId newuserId,
			ArrayList<Pixel> prevPixelIntensity,
			Boolean reset
			) {
		
		// Create new BoardObject from the parameters
		BoardObject curveObj = new BoardObject(
				pixels,
				newobjectId,
				newtimestamp,
				newuserId,
				reset
		);
				
		//set operation of the created object
		curveObj.setOperation(newboardOp);
		
		curveObj.setPrevIntensity(prevPixelIntensity);
		
		try {
			//Insert BoardObject in the map
			ClientBoardState.maps.insertObjectIntoMaps(curveObj);
			
		} catch(Exception e){
			
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
					"Error to insert object in Client maps"
			);
			
		}
		
		try {
			//Push BoardObject in undo stack
			pushToStack(curveObj);
			
		} catch(Exception e){
			
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
					"Error to insert object in Undo stack"
			);
			
		}
		
		return curveObj;
	}
	
	/*
	 * create BoardObject for the eraser tool
	 * using positions obtained from UI
	 * add this object to client map 
	 * push object to undo stack
	 */
	
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
		
		// Create new BoardObject from the parameters
		BoardObject eraseObj = new BoardObject(
				pixel,
				newobjectId,
				newtimestamp,
				newuserId,
				reset
		);
		
		//set operation of the created object
		eraseObj.setOperation(newboardOp);
		
		try {
			//Insert BoardObject in the map
			ClientBoardState.maps.insertObjectIntoMaps(eraseObj);
			
		} catch(Exception e){
			
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
					"Error to insert object in Client maps"
			);
			
		}

		try {
			//Push BoardObject in undo stack
			pushToStack(eraseObj);
			
		} catch(Exception e){
			
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
					"Error to insert object in Undo stack"
			);
			
		}
		
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"[#" + Thread.currentThread().getId() + "] " +
				"Created BoardObject and successfully processed the eraser"
		);

		
		return eraseObj;
	}

	private static void pushToStack(BoardObject newObj) {
		UndoRedo.pushIntoStack(newObj);
	}	
}