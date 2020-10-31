package processing;

import processing.*;
import processing.utility.*;
import processing.boardobject.*;
import java.util.*;

public class UndoRedo {
	
	private static int STACK_CAPACITY = 20;
	
	private enum Operation {
		UNDO, REDO;
	}
    
	/**
	 *  map for access in the list
   	 *  private static Map <ObjectId, ListIterator> idToIterator;
     */
	private static void addIntoStack(LinkedList <BoardObject> stack, BoardObject obj) {
		
		/** If stack size becomes full, delete the bottom (first) object */
		if (stack.size() >= STACK_CAPACITY)
			stack.removeFirst();
		
		/** Push the object to the top (last) */
		stack.addLast(obj);
	}
	
	private static BoardObject createOperation(BoardObject obj) {
		/** BoardObject CREATE operation*/
		IBoardObjectOperation newOp = new CreateOperation();
		BoardObject newObj = CurveBuilder.drawCurve( 
									obj.getPixels(), 
									newOp, 
									obj.getObjectId(), 
									obj.getTimestamp(),
									obj.getUserId(), 
									obj.getPrevIntensity(), 
									false 
									);
		return newObj;
	}
	
	private static BoardObject colorChangeOperation(BoardObject obj, Intensity intensity) {
		/** BoardObject COLOR_CHANGE operation*/
		UserId id = obj.getUserId();
		BoardObject newObj = ParameterizedOperationsUtil.colorChangeUtil(obj, id, intensity);
		return newObj;
	}
	
	private static BoardObject rotateOperation(BoardObject obj, Angle angle) {
		/** BoardObject ROTATE operation*/
		UserId id = obj.getUserId();
		BoardObject newObj = ParameterizedOperationsUtil.rotationUtil(obj, id, angle);
		return newObj;
	}
	
	private static void undoRedoUtil(
			LinkedList <BoardObject> curStack,
			LinkedList <BoardObject> otherStack,
			Operation operation
			) {
		
		/** No undo or redo operation possible */
    	if (curStack.size() <= 0)
    		return;
    	
    	BoardObject topObj =  curStack.getLast();
    	
    	/** Gets the operation performed on that object */
    	BoardObjectOperationType operationType = obj.getOperation().getOperationType();
    	BoardObject newObj;
    	
    	switch (operationType) {
    		case CREATE : if (operation == Operation.REDO)
    						newObj = createOperation(topObj);
    						break;
    		case DELETE : if (operation == Operation.UNDO)
    						newObj = createOperation(topObj);
    						break;
    					  
    		case ROTATE : Angle angleCCW = ((RotateOperation) topObj.getOperation()).getAngle();
    					  Angle newAngle;
    					  if (operation == Operation.UNDO)
    						  newAngle = new Angle(-angleCCW.angle);
    					  else
    						  newAngle = new Angle(angleCCW.angle);
    					  newObj = rotateOperation(topObj, newAngle);
    					  break;
    					   
    		case COLOR_CHANGE : ArrayList <Pixel> newPixels;
    							if (operation == Operation.UNDO)
    								newPixels = topObj.getPrevIntensity();
    							else
    								newPixels = topObj.getPixels();
    							Intensity newIntensity = newPixels.get(0).intensity;
    							newObj = colorChangeOperation(topObj, newIntensity);
    							break;
    							
    		default : break; /** Invalid operation*/
    	}
    	
    	addIntoStack(otherStack, topObj);
    	curStack.removeLast();
    	CommunicateChange.provideChanges(newObj.getPrevIntensity(), newObj.getPixels());
	}
	
	private static void deleteFromStack(LinkedList <BoardObject> stack, ObjectId objectId) {
		   
		   /** Deletes from the stack */
		   ListIterator <BoardObject> iterStack = stack.listIterator();
		   while (iterStack.hasNext()) {
			   BoardObject obj = iterStack.next();
			   if (obj.getObjectId().equals(objectId))
				   iterStack.remove();
		   }
		   return;
	}
	
   /*
    * Performs the undo operation.
    * Looks at top object of the undoStack, performs the 
    * object's inverse operation, pops from the undoStack 
    * and pushes into the redoStack.
    */
    public static void undo() {
    	 LinkedList <BoardObject> undoStack = ClientBoardState.undoStack;
    	 LinkedList <BoardObject> redoStack = ClientBoardState.redoStack;
    	 undoRedoUtil(undoStack, redoStack, Operation.UNDO);
    }
   
   /*
    * Performs the redo operation.
    * Looks at top object of the redoStack, performs back the 
    * object's operation, pops from the redoStack and pushes 
    * into the undoStack.
    */
   public static void redo() {
	   LinkedList <BoardObject> undoStack = ClientBoardState.undoStack;
  	 	LinkedList <BoardObject> redoStack = ClientBoardState.redoStack;
  	 	undoRedoUtil(undoStack, redoStack, Operation.REDO);
   }
   
   /*
    * Any operation performed on object by other classes
      needs to be pushed into the undoStack
    */
   public static void pushIntoStack(BoardObject object) {
	   ClientBoardState.undoStack.add(object);
	   
	   addIntoStack(ClientBoardState.undoStack, object);
	   /** No iterator map for now*/
   }
   
   /*
    * Deletes an object by the ObjectId from the undoStack
    * Needed for the transfer of the ownership of an object in case
      the object was deleted by other user 
    */
   public static void deleteFromStack(ObjectId objectId) {
	   
	   /** Deletes from undo stack */
	   deleteFromStack(ClientBoardState.undoStack, objectId);
	   /** Deletes from redo stack */
	   deleteFromStack(ClientBoardState.redoStack, objectId);
   }
}
