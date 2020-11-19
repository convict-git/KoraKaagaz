# Rakesh
# Design Spec
> By Rakesh Kumar (111701024)
# Objectives
* Build a class `UndoRedo` that implements the methods needed to perform the Undo-Redo operations.
    * The class will provide the methods: `undo` and `redo` which will perform the undo and redo operations respectively.
    * It will provide `pushIntoStack` method for other classes that will allow them to push an object into the stack.
    * It will also provide `deleteFromStack` method which allows an object to be deleted from the stack using `ObjectId`.
* Build a class `UserUtil` for providing these utility methods:
    * The method `initiateUserConnection` gets username, server's ip-address and whiteboard id through UI from the user and starts the client side processing.
    * The method `getUser` returns `UserId` of the user who have drawn object on the given set of positions. 
    * `stopBoardSession()` method ends the session of the user.
* Build a class `CommunicateChange` for communicating changes in pixels to UI. It provides these two methods:
    * The method `subscribeForChanges` allows the UI to subscribe for any changes in the pixels.
    * `provideChanges`  method will notify the UI about any changes in pixels that will have effect on the screen.
* Provide `bfsCircleFill` and `drawTriangleFill` methods in shape package to draw a filled circle and a filled triangle with the given intensity. 


# Undo-Redo Operations
## Design
* Undo-Redo operations are user specific which means a user can performs these operations on the objects created by her / him.
* There are four possible operations on a board: object creation (`CREATE`), object deletion (`DELETE`), object rotation (`ROTATION`) and colour change of an object (`COLOR_CHANGE`).
* Each user will maintain two stacks: undo stack and redo stack. Each stack will store objects along with the operation on that object. 
* If the user selects the undo operation, depending upon the top object's operation on the undo stack `undo` method will perform the inverse operation (to reverse the effect) on the object, then will pop from the undo stack and will push the object into the redo stack. 
    * For `CREATE` operation, `DELETE` is the inverse operation. So, the delete method will be called on the object and the object will be passed to the redo stack with the operation `CREATE`.
    * Similarly, for `DELETE` operation, `CREATE` is the inverse operation. So, the create method will be called on the object and the object will be passed to the redo stack with `DELETE` operation.
    * For `COLOR_CHANGE` operation, colour change method will be called on that object with the previous colour as new color and current colour as the previous colour and the original object will be passed to the redo stack with `COLOR_CHANGE` operation.
    * If the operation is `ROTATE`, then rotate method will be called with an angle `360 - rotatedAngle` on that object and the object will be passed to the redo stack.
* For redo operation, the top object from the redo stack will be passed to the undo stack and operation associated with the object will be performed on the object.
* After any update on undo stack (using `pushIntoStack` method), redo stack is cleared to maintain the consistency in the states.
* After any undo or redo operation, a new board object is returned which is sent across the network and processed by the other clients to get the changes.


## Implementation of Stacks (Analysis)
1. Due to space constraint, the stacks capacity are fixed. Their sizes can't be more than `STACK_CAPACITY`. This imply that a user perform atmost `STACK_CAPACITY` undo or redo operations. Once the stack reaches it full capacity, the bottommost object must have to removed from the stack to make room for a new object to be pushed. So, we need to effectively delete the bottommost stack object also.
2. If a  user's object gets deleted by another user, then the object must be transferred to the user who performed the delete operation and should also be deleted from current user stack. So, the stack should also support effecient object search by `ObjectId` and deletion.

The following data structures can be used to support the above operations:
* Stack data structure: It provides only push and pop methods. To remove the bottommost object, one needs to pop all the objects and push back all the objects (except the bottommost) into the stack. So, it will cost `O(STACK_CAPACITY)` for each subsequent push operation. Since the stack data structure doesn't support search and delete by a key so, the cost to delete an object by `ObjectId` will be `O(stackSize)`.
* Dequeue (or List) data structure: In this data structure, we can insert or delete at both ends. So, it can delete the bottommost object in `O(1)` but for deletion by `ObjectId`, it will also take `O(stackSize)`.
* List data structure and Map: We can store objects into the list and maintain a map from `ObjectId` to `List` of its references in the list (There could be multiple objects with same `ObjectId` in stacks). One push operation would require a insert operation into top of undo List along with pushing the object's reference (top of undo List) in the `ObjectId-ListofReferences` map. So, the bottommost object deletion would require deletion from the List as well as from the map. Similarly, for the deletion by the `ObjectId` we first find the corresponding references and then delete from both the map as well as the List. The time complexity in each case will be `O(log(stackSize) * No. of references)`. 

For now, only List data structure used. In future, it can be modified to use the List and map approach for better overall time complexity.

## Interface and Class    
```java=
    public interface IUndoRedo {
        // redo the work done by a particular user
        void redo();
        // undo the work done by a particular user
        void undo();
    }
    
    public class UndoRedo {

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
         public static void setStackCapacity(int newCapacity);
         
        /**
         * gets the stack capacity
         * 
         * @return stack capacity
         */
         public static int getStackCapacity();
         
        /**
         * This method performs undo or redo operation
         * To perform any of the two operation proper arguments need to be passed
         * 
         * @param curStack could be undo or redo stack depending upon operation
         * @param otherStack other stack than curStack
         * @param operation undo or redo operation
         * @return object with operation to be performed among other clients
         */ 
         private static BoardObject undoRedoUtil(
             ArrayList <BoardObject> curStack,
             ArrayList <BoardObject> otherStack,
             Operation operation
         );
        
        /**
         * Performs the undo operation.
         * Looks at top object of the undoStack, performs the 
         * object's inverse operation, pops from the undoStack 
         * and pushes into the redoStack.
         * 
         * @return object with operation to be performed among other clients
         */ 
         public static BoardObject undo();
         
        /**
         * Performs the redo operation.
         * Looks at top object of the redoStack, performs back the 
         * object's operation, pops from the redoStack and pushes 
         * into the undoStack.
         * 
         * @return object with operation to be performed among other clients
         */ 
         public static BoardObject redo();
         
        /**
         * Any operation performed on object by other classes 
         * needs to be pushed into the undoStack
         * 
         * @param object object to be pushed
         */ 
         public static void pushIntoStack(BoardObject object);
         
        /**
         * Deletes an object by the ObjectId from the undo and redo
         * Needed for the transfer of the ownership of an object in case
         * the object was deleted by other user 
         * 
         * @param objectId id of the object to be deleted
         */ 
         public static void deleteFromStack(ObjectId objectId);
    }

```
# User Utility Class
The class provides the following useful methods:
* The method `initiateUserConnection` gets username, server’s ip-address and whiteboard ID through UI from the user. These information are used for making board request to the server. This method also starts the client side processing.
* `getUser` method takes a list of positions and returns the `UserId` of the user who have drawn object on these positions. The method considers only the topmost objects on these positions. Since, there could be multiple users on these positions it returns the `UserId` of the user having maximum occurences in these positions.
* `stopBoardSession` method is called to end the board session. It then informs the server about ending the connection. 

## Performance Analysis
For `getUser` method, for every given positions the method will find the `UserId` of the top objects. Then it will return back the `UserId` having maximum occurences. For finding the `UserId`, it will perform two map lookups. First look would be from pixel to object id map and second would the object id to object map. This will take `O(log(No. of Total Objects))` for each given position. For finding the maximum occuring `UserId`, data structure like priority queue can be used. So, the overall time complexity will be `O(P log(N)` where `N = Total number of Objects drawn` and `P = Number of given positions`.
## Class
```java=
    public class UserUtil {
        
        /**
         * UI calls this method to initiate connection 
         * with the server.
         * The information provided in the arguments
         * are used to start the connection
         * 
         * @param username Username
         * @param ipAddress Server's ip address
         * @param boardId Board ID	
         */
         
         public static void initiateUserConnection(
             String username, 
             String ipAddress, 
             String boardId
         );
         
        /**
         * Finds the username of the objects drawn at these list of positions
         * 
         * Since, there could be multiple objects at these positions drawn by different
         * users so username of the user having maximum occurrences at these positions
         * is selected
         * 
         * @param positions list of positions
         * @return username
         * @return null if there is no object present at those positions
         */ 
         
         public static String getUser(ArrayList <Position> positions);
         
        /**
         * Stops the board session for the user
         */ 
         public static void stopBoardSession();
        
    }
```
# Communicating Changes to UI
* UI will subsribe to processing module for any changes in pixels using the `subscribeForChanges` method with an identifier and an `IChanges` handler.
* `provideChanges` method will take both the old pixels (after an operation) and the new pixels of the object. Then it will find the set of pixels which will have effect on the screen and will send these pixels to `getChanges` method of the `IChanges` handler to update the screen.

## Class and Interface
```java=
    public class CommunicateChange {
    
        // identifier to IChanges handler map
        public static Map <String, IChanges> identifierToHandler;
        // UI identifier
        public static String identifierUI;
        
        /**
         * Computes the set of pixels which will have effect
         * on the screen and notify the UI using the getChanges
         * method of IChanges handler.
         * 
         * @param identifier identifier of the handler
         * @param oldPixels previous pixels of the object
         * @param newPixels new pixels of the object
         */ 
         public static void provideChanges(
             String identifier, 
             ArrayList<Pixel> oldPixels, 
             ArrayList<Pixel> newPixels
         );
         
        /**
         * A module can subscribe for any changes
         * In our design, only UI need subscription so
         * @param identifier can be removed but it is kept
         * for now. 
         * 
         * @param identifier identifier used for subscription
         * @param handler UI handler to handle changes
         */
         public static void subscribeForChanges(String identifier, IChanges handler);
    }

    // implemented by the UI team
    public interface IChanges {
        void getChanges(ArrayList<Pixel> pixels);
    }
```
# Shapes Drawer (Circle Fill and Triangle Fill)
* `bfsCircleFill` and `drawTriangleFill` methods are implemented in the class `CircleDrawer` and `TriangleDrawer` respectively. `bfsCircleFill` draws a filled circle and `drawTriangleFill` draws a filled triangle with the given intensity. Both methods use Breadth First Search algorithm to fill the shapes.

## Class and Methods
```java=
    public class CircleDrawer {
        /**
         * Draws a Filled Circle based on the center and radius provided
         * using the BFS algorithm
         *  
         * @param center Center of the filled circle
         * @param radius Radius of the filled circle
         * @param intensity Intensity of each pixel of the filled circle
         * @return the arraylist of pixels of the filled circle
         */ 

        private static ArrayList <Pixel> bfsCircleFill(
            Position center,
            Radius radius,
            Intensity intensity
        );
    }
    
    public class TriangleDrawer {
    
        /**
         * Draws a filled triangle given by three vertices
         * Uses BFS to fill the triangle
         *   
         * @param vertA First vertex of the triangle
         * @param vertB Second vertex of the triangle
         * @param vertC Third vertex of the triangle
         * @param intensity Intensity of each point on the triangle
         * @return filled triangle with the given intensity
         */ 
        public static ArrayList <Pixel> drawTriangleFill (
                Position vertA,
                Position vertB,
                Position vertC,
                Intensity intensity
        );
    }
```