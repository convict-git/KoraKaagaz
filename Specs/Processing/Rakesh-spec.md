# Design Spec
> By Rakesh Kumar (111701024)
# Objectives
* Build a class `UndoRedo` that implements the methods needed to perform the Undo-Redo operations.
    * The class will provide the methods: `undo` and `redo` which will perform the undo and redo operations respectively.
    * It will provide `pushIntoStack` method for other classes that will allow them to push an object into the stack.
    * It will also provide `deleteFromStack` method which allows an object to be deleted from the stack using `ObjectId`.
* Build a class `UserUtil` for providing these utility methods:
    * The method `initiateUserConnection` gets username, server's ip-address and whiteboard id through UI from the user.
    * The method `getUser` returns `UserId` of the user who have drawn object on the given set of positions. 
    * `stopBoardSession()` method ends the session of the user.
* Build a class `CommunicateChange` for communicating changes in pixels to UI. It provides these two methods:
    * The method `subscribeForChanges` allows the UI to subscribe for any changes in the pixels.
    * `provideChanges`  method will notify the UI about any changes in pixels that will have effect on the screen.


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


## Implementation of Stacks (Analysis)
1. Due to space constraint, the stacks capacity are fixed. Their sizes can't be more than `STACK_CAPACITY`. This imply that a user perform atmost `STACK_CAPACITY` undo or redo operations. Once the stack reaches it full capacity, the bottommost object must have to removed from the stack to make room for a new object to be pushed. So, we need to effectively delete the bottommost stack object also.
2. If a  user's object gets deleted by another user, then the object must be transferred to the user who performed the delete operation and should also be deleted from current user stack. So, the stack should also support effecient object search by `ObjectId` and deletion.

The following data structures can be used to support the above operations:
* Stack data structure: It provides only push and pop methods. To remove the bottommost object, one needs to pop all the objects and push back all the objects (except the bottommost) into the stack. So, it will cost `O(STACK_CAPACITY)` for each subsequent push operation. Since the stack data structure doesn't support search and delete by a key so, the cost to delete an object by `ObjectId` will be `O(stackSize)`.
* Dequeue data structure: In this data structure, we can insert or delete at both ends. So, it can delete the bottommost object in `O(1)` but for deletion by `ObjectId`, it will also take `O(stackSize)`.
* List data structure and Map: We can store objects into the list and maintain a map from `ObjectId` to `ListIterator`. One push operation would require a insert operation into top of undo List along with storing the `ObjectId-ListIterator` pair into the map. So, the bottommost object deletion would require deletion from the List as well as from the map. Similarly, for the deletion by the `ObjectId` we first find corresponding iterator and then delete from both the map as well as the List. The time complexity in each case will be `O(log(stackSize))`. Since, it has better overall time complexity so it is used for the implementation.
## Interface and Class    
```java=
    public interface IUndoRedo {
        // redo the work done by a particular user
        void redo();
        // undo the work done by a particular user
        void undo();
    }
    
    public class UndoRedo {

         private static int STACK_CAPACITY = 20;
         // map for access in the list
         private static Map <ObjectId, ListIterator> idToIterator;
        
        /*
         * Performs the undo operation.
         * Looks at top object of the undoStack, performs the 
         * object's inverse operation, pops from the undoStack 
         * and pushes into the redoStack.
         */
        public static void undo();
        
        /*
         * Performs the redo operation.
         * Looks at top object of the redoStack, performs back the 
         * object's operation, pops from the redoStack and pushes 
         * into the undoStack.
         */
        public static void redo();
        
        /*
         * Any operation performed on object by other classes
           needs to be pushed into the undoStack
         */
        public static void pushIntoStack(BoardObject object);
        
        /*
         * Deletes an object by the ObjectId from the undoStack
         * Needed for the transfer of the ownership of an object in case
           the object was deleted by other user 
         */
        public static void deleteFromStack(ObjectId objectId);
    }

```
# User Utility Class
The class provides the following useful methods:
* The method `initiateUserConnection` gets username, server’s ip-address and whiteboard ID through UI from the user. These information will be used for making board request to the server.
* `getUser` method takes a list of positions and returns the `UserId` of the user who have drawn object on these positions. The method considers only the topmost objects on these positions. Since, there could be multiple users on these positions it returns the `UserId` of the user having maximum occurences in these positions.
* `stopBoardSession` method is called to end the board session. It then informs the server about ending the connection. 
## Performance Analysis
For `getUser` method, for every given positions the method will find the `UserId` of the top objects. Then it will return back the `UserId` having maximum occurences. For finding the `UserId`, it will perform two map lookups. First look would be from pixel to object id map and second would the object id to object map. This will take `O(log(No. of Total Objects))` for each given position. For finding the maximum occuring `UserId`, data structure like priority queue can be used. So, the overall time complexity will be `O(Plog(N)` where `N = Total number of Objects drawn` and `P = Number of given positions`.
## Class
```java=
    public class UserUtil {
        /* this function is used to get the user details during initial
           setup from user through UI if the board Id is null then we get
           a new board id from server and pass to UI
         */
        /*Arguments:  userName - user name
                      ipAddress - ipaddress for the server with the port
                      boardId - white board ID to which user wants to connect 
        */
        public static String initiateUserConnection(String userName, String ipAddress, String boardId);
        // return the user id at the selected position
        public static UserId getUser(ArrayList<Position> positions);
        // stop board session for that user
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
        private Map <String, IChanges> handlerMap;
        
        /*
            Computes the set of pixels which will have effect
            on the screen and notify the UI using the getChanges
            method of IChanges.
         */
        public static void provideChanges(String identifier, ArrayList<Pixel> oldPixels, 
        ArrayList<Pixel> newPixels);
        // UI will subscribe for any changes 
        public static void subscribeForChanges(String identifier, IChanges handler);
    }

    // implemented by the UI team
    public interface IChanges {
        void getChanges(ArrayList<Pixel> pixels);
    }
```
# Summary and Conclusion
For the undo-redo operation, List and map have replaced the actual stack to support more operations effeciently. Similarly, in other places (like finding the `UserId` at postions) some data structure is used for performance improvement. During the coding phase, if a better data structure is found then it may replace those internal data structure. Since , here data structure is used only for implementation so changing those internal data structures won't have any effect on the design.
