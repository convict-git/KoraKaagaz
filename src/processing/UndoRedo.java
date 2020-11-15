package processing;


import processing.utility.*;
import processing.boardobject.*;
import java.util.*;
import infrastructure.validation.logger.*;

/**
 * UndoRedo class implements undo-redo operations
 * 
 * @author Rakesh Kumar
 * @reviewer Devansh Singh Rathore
 */

public class UndoRedo {
	
	// gets the logger instance
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	/** 
	 *  maximum stack size 
	 *  restricts the maximum undo-redo operations
	 */
	private static int STACK_CAPACITY = 20;
	private enum Operation {
		UNDO, REDO;
	}
    
	/**
	 * sets the stack capacity
	 * 
	 * @param newCapacity new stack capacity
	 */
	public static void setStackCapacity(int newCapacity) {
		STACK_CAPACITY = newCapacity;
	}
	
	/**
	 * gets the stack capacity
	 * 
	 * @return stack capacity
	 */
	public static int getStackCapacity() {
		return STACK_CAPACITY;
	}
	
	/**
	 *  Will be visited later
	 *  map for access in the list
   	 *  private static Map <ObjectId, ListIterator> idToIterator;
     	 */
	
	/**
	 * This helper method will push the object into the given stack
	 * 
	 * @param stack could be undo or redo stack
	 * @param obj object to be pushed
	 */
	private static void addIntoStack(ArrayList <BoardObject> stack, BoardObject obj) {
		
		// If stack size becomes full, delete the bottom (first) object
		if (stack.size() >= STACK_CAPACITY) {
			stack.remove(0);
		}
		
		// Push the object to the top (last)
		stack.add(obj);
	}
	
	/**
	 * This helper method creates the object
	 * It is called during undo-redo operations
	 * 
	 * @param obj
	 * @return newly created object
	 */
	private static BoardObject createOperation(BoardObject obj) {
		// BoardObject CREATE operation
		IBoardObjectOperation newOp = new CreateOperation();
		// gets the newly created object
		BoardObject newObj = new BoardObject(
				obj.getPixels(),
				obj.getObjectId(),
				obj.getTimestamp(),
				obj.getUserId(),
				obj.isResetObject()
		);
				
		//sets the CREATE operation in the created object
		newObj.setOperation(newOp);
		
		//Insert the object into the Map
		ClientBoardState.maps.insertObjectIntoMaps(newObj);

		logger.log(
			   	ModuleID.PROCESSING, 
			   	LogLevel.SUCCESS, 
			   	"Undo / Redo : object " + obj.getObjectId() + " created"
		);

		return newObj;
	}
	
	/**
	 * This helper method calls colorChangeUtil method to change the color of the object
	 * It is called during undo-redo operations
	 * 
	 * @param obj object whose color has to be changed
	 * @param intensity new intensity or color of the object
	 * @return newly created color changed object
	 */
	private static BoardObject colorChangeOperation(BoardObject obj, Intensity intensity) {
		UserId id = obj.getUserId();
		// gets the newly created color changed object
		BoardObject newObj = ParameterizedOperationsUtil.colorChangeUtil(obj, id, intensity);
		logger.log(
			   	ModuleID.PROCESSING, 
			   	LogLevel.SUCCESS, 
			   	"Undo / Redo : object " + obj.getObjectId() + " color changed"
		);
		return newObj;
	}
	
	/**
	 * This helper method calls rotationUtil method to rotate the object
	 * It is called during undo-redo operations
	 * 
	 * @param obj object that has to be rotated
	 * @param angle angle in CCW the object has to be rotated
	 * @return the newly created rotated object
	 */
	private static BoardObject rotateOperation(BoardObject obj, Angle angle) {
		UserId id = obj.getUserId();
		// gets the newly rotated object
		BoardObject newObj = ParameterizedOperationsUtil.rotationUtil(obj, id, angle);
		logger.log(
			   	ModuleID.PROCESSING, 
			   	LogLevel.SUCCESS, 
			   	"Undo / Redo : object " + obj.getObjectId() + " rotated"
		);
		return newObj;
	}
	
	/**
	 * This method performs undo or redo operation
	 * To perform any of the two operation proper arguments need to be passed
	 * 
	 * @param curStack could be undo or redo stack depending upon operation
	 * @param otherStack other stack than curStack
	 * @param operation undo or redo operation
	 */
	private static void undoRedoUtil(
			ArrayList <BoardObject> curStack,
			ArrayList <BoardObject> otherStack,
			Operation operation
	) {
		
		// No undo or redo operation possible
    	if (curStack.size() <= 0)
    		return;
    	
    	// Gets the top object from the stack
    	BoardObject topObj =  curStack.get(curStack.size() - 1);
    	
    	// Gets the operation performed on that object
    	BoardObjectOperationType operationType = topObj.getOperation().getOperationType();
    	BoardObject newObj = null;
    	
    	switch (operationType) {
		case CREATE :	
				if (operation == Operation.REDO) {
					newObj = createOperation(topObj);
				}
				else {
					// performs simple delete operation
					ClientBoardState.maps.removeObjectFromMaps(topObj.getObjectId());
					logger.log(
							ModuleID.PROCESSING, 
							LogLevel.SUCCESS, 
							"Undo : object " + topObj.getObjectId() + " deleted"
					);
				}
				break;
			
		case DELETE :	
				if (operation == Operation.UNDO) {
					newObj = createOperation(topObj);
				}
				else {
					// performs simple delete operation
					ClientBoardState.maps.removeObjectFromMaps(topObj.getObjectId());
					logger.log(
							ModuleID.PROCESSING, 
							LogLevel.SUCCESS, 
							"Redo : object " + topObj.getObjectId() + " deleted"
					);
				}
				break;
    					  
		case ROTATE :	
				Angle angleCCW = ((RotateOperation) topObj.getOperation()).getAngle();
				Angle newAngle = null;
				/** 
				 *  For undo, the object should be rotated -angle CCW
				 *  For redo, the object should be rotated angle CCW
				 */
				if (operation == Operation.UNDO) {
					newAngle = new Angle(-angleCCW.angle);
				}
				else {
					newAngle = new Angle(angleCCW.angle);
				}
				newObj = rotateOperation(topObj, newAngle);
				break;
    					   
		case COLOR_CHANGE :	
    				ArrayList <Pixel> newPixels;
				/** 
				 *  For undo, the object's should be changed to previous color
				 *  For redo, the object's should be changed to current color
				 */
				if (operation == Operation.UNDO) {
					newPixels = topObj.getPrevIntensity();
				}
				else {
					newPixels = topObj.getPixels();
				}
				Intensity newIntensity = newPixels.get(0).intensity;
				newObj = colorChangeOperation(topObj, newIntensity);
				break;
    							
		default : 	// Invalid operation 
    				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"Undefined Operation type : " + operationType
    				); 
    				return;
    	}
    	
    	// Transfers the object from one stack to other
    	addIntoStack(otherStack, topObj);
    	curStack.remove(curStack.size() - 1);
    	/** 
    	 *  Send the modified pixels to the UI 
    	 *  null value occurs when delete operation is performed 
    	 */
    	if (newObj != null) {
    		CommunicateChange.provideChanges(newObj.getPrevIntensity(), newObj.getPixels());
    	}
    	else {
    		CommunicateChange.provideChanges(topObj.getPixels(), null);
    	}
    	logger.log(
			ModuleID.PROCESSING, 
			LogLevel.SUCCESS, 
			"Undo / Redo successfully performed"
    	); 
    	
	}
	
	/**
	 * This method deletes the object from the stack using object id
	 * 
	 * @param stack stack from which the object has to be deleted
	 * @param objectId object id of the object to be deleted
	 */
	private static void deleteFromStack(ArrayList <BoardObject> stack, ObjectId objectId) {
		   
		   // Deletes from the stack
		   ListIterator <BoardObject> iterStack = stack.listIterator();
		   while (iterStack.hasNext()) {
			   BoardObject obj = iterStack.next();
			   if (obj.getObjectId().equals(objectId)) {
				   iterStack.remove();
			   }
		   }
		   return;
	}
	
   /**
    * Performs the undo operation.
    * Looks at top object of the undoStack, performs the 
    * object's inverse operation, pops from the undoStack 
    * and pushes into the redoStack.
    */
    public static void undo() {
    	ArrayList <BoardObject> undoStack = ClientBoardState.undoStack;
    	ArrayList <BoardObject> redoStack = ClientBoardState.redoStack;
    	undoRedoUtil(undoStack, redoStack, Operation.UNDO);
    }
   
   /**
    * Performs the redo operation.
    * Looks at top object of the redoStack, performs back the 
    * object's operation, pops from the redoStack and pushes 
    * into the undoStack.
    */
   public static void redo() {
	   ArrayList <BoardObject> undoStack = ClientBoardState.undoStack;
	   ArrayList <BoardObject> redoStack = ClientBoardState.redoStack;
	   undoRedoUtil(redoStack, undoStack, Operation.REDO);
   }
   
   /**
    * Any operation performed on object by other classes 
    * needs to be pushed into the undoStack
    * 
    * @param object object to be pushed
    */
   public static void pushIntoStack(BoardObject object) {
	   
	   //If the object is created by other user then do not push it into the stack
	   if (object.getUserId().equals(ClientBoardState.userId) == false) {
		   return;
	   }
	   
	   // pushes into undo stack
	   addIntoStack(ClientBoardState.undoStack, object);
	   
	   logger.log(
			ModuleID.PROCESSING, 
			LogLevel.SUCCESS, 
			"Object pushed into the Undo stack"
		);
	   
	   /** 
	    * No iterator map for now
	    * Will be added later 
	    */
   }
   
   /**
    * Deletes an object by the ObjectId from the undo and redo
    * Needed for the transfer of the ownership of an object in case
    * the object was deleted by other user 
    * 
    * @param objectId id of the object to be deleted
    */
   public static void deleteFromStack(ObjectId objectId) {
	   
	   // Deletes from undo stack 
	   deleteFromStack(ClientBoardState.undoStack, objectId);
	   // Deletes from redo stack
	   deleteFromStack(ClientBoardState.redoStack, objectId);
	   
	   logger.log(
			ModuleID.PROCESSING, 
			LogLevel.SUCCESS, 
			"Object deleted from the stacks"
		);
   }
}