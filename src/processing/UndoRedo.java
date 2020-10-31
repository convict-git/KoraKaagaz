package processing;

import processing.*;
import processing.utility.*;
import processing.boardobject.*;
import java.util.*;

public class UndoRedo {
	
	private static int STACK_CAPACITY = 20;
    
	// map for access in the list
    // private static Map <ObjectId, ListIterator> idToIterator;
   
	private static void addIntoStack(LinkedList <BoardObject> stack, BoardObject obj) {
		
		/** If stack size becomes full, delete the bottom (first) object */
		if (stack.size() >= STACK_CAPACITY)
			stack.removeFirst();
		
		/** Push the object to the top (last) */
		stack.addLast(obj);
	}
	
	
   /*
    * Performs the undo operation.
    * Looks at top object of the undoStack, performs the 
    * object's inverse operation, pops from the undoStack 
    * and pushes into the redoStack.
    */
    public static void undo() {
    	
    	Integer stackSize =  ClientBoardState.undoStack.size();
    	
    	/** No undo operation possible */
    	if (stackSize <= 0)
    		return;
    	
    	BoardObject obj =  ClientBoardState.undoStack.get(stackSize - 1);
    	
    	/** Gets the operation performed on that object */
    	IBoardObjectOperation operation = obj.getOperation();
    	BoardObject newObj;
    	
    	switch (operation) {
    		case CREATE : break;
    		case DELETE : IBoardObjectOperation newOp = 
    							new ColorChangeOperation(obj.getOperation().getIntensity());
    					  newObj = BoardOCurveBuilder.drawCurve( 
    												obj.getPixels(), 
    												newOp, 
    												obj.getObjectId, 
    												obj.getTimestamp(),
    												obj.getUserId, 
    												obj.getPrevIntensity(), 
    												false 
    												);
    					  break;
    					  
    		case ROTATE : Angle angleCCW = obj.getOPeration.RotateOperation.getAngle();
    					  Angle newAngle = Angle(-angleCCW.angle);
    					  newObj = rotationUtil(obj, obj.getUserId(), newAngle)
    					  break;
    					   
    		case COLOR_CHANGE : ArrayList <Pixel> prevIntensity = obj.getPrevIntensity();
    							Intensity intensity = prevIntensity.get(0).intensity;
    							UserId id = obj.getUserId();
    							newObj = colorChangeUtil(obj, id, intensity);
    							break;
    		default : break; /** Invalid operation*/
    	}
    	
    	
    }
   
   /*
    * Performs the redo operation.
    * Looks at top object of the redoStack, performs back the 
    * object's operation, pops from the redoStack and pushes 
    * into the undoStack.
    */
   public static void redo() {
	   ;
   }
   
   /*
    * Any operation performed on object by other classes
      needs to be pushed into the undoStack
    */
   public static void pushIntoStack(BoardObject object) {
	   ClientBoardState.undoStack.add(object);
	   /** No iterator map for now*/
   }
   
   /*
    * Deletes an object by the ObjectId from the undoStack
    * Needed for the transfer of the ownership of an object in case
      the object was deleted by other user 
    */
   //public static void deleteFromStack(ObjectId objectId);
}
