package processing;

import processing.*;

public class UndoRedo {
	
	private static int STACK_CAPACITY = 20;
    // map for access in the list
    //private static Map <ObjectId, ListIterator> idToIterator;
   
   /*
    * Performs the undo operation.
    * Looks at top object of the undoStack, performs the 
    * object's inverse operation, pops from the undoStack 
    * and pushes into the redoStack.
    */
    //public static void undo();
   
   /*
    * Performs the redo operation.
    * Looks at top object of the redoStack, performs back the 
    * object's operation, pops from the redoStack and pushes 
    * into the undoStack.
    */
   //public static void redo();
   
   /*
    * Any operation performed on object by other classes
      needs to be pushed into the undoStack
    */
   public static void pushIntoStack(BoardObject object) {
	   ClientBoardState.undoStack.add(object);
	   // iterator map should also be updated
   }
   
   /*
    * Deletes an object by the ObjectId from the undoStack
    * Needed for the transfer of the ownership of an object in case
      the object was deleted by other user 
    */
   //public static void deleteFromStack(ObjectId objectId);
}
