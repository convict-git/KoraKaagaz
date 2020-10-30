
## Processing Module Spec 
> Develop module that does processing for whiteboard application

# Table of contents
* [Team Members](#Team-Members)
* [Design](#Design)
    * [Initial Setup](#Initial-Setup-A-client-logins-and-ask-to-create-a-new-board-or-an-already-existing-board-on-server)
    * [Feature Handling](#Feature-Handling)
        * [Draw using brush](#1-Draw-using-brush--draw-shape) 
        * [Erase](#2-Erase)
        * [Select & Delete](#3-Select-and-Delete)
        * [Select & Rotate](#4-Select-and-Rotate)
        * [Color Change](#5-Color-Change)
        * [Undo Redo](#6-Undo-Redo)
        * [Interface with Network](#7-Interface-with-Network)
        * [Persistence](#8-Persistence)
        * [Server Processing](#9-Server-Processing)
* [Activity Diagrams](#Activity-Diagrams)
* [Interface](#Interface)
* [Tasks Distribution](#Tasks-Distribution)
* [Analysis](#Analysis)

### Team Members 
---
Name                            | Roll No
------------------------------- |-----------
Ahmed Zaheer Dadarkar           |  111701002
Devansh Singh Rathore           |  117010011
Himanshu Jain                   |  111701013
Rakesh Kumar                    |  111701024
Sakshi Rathore (Team Lead)      |  111701026
Satchit Desai                   |  121701007
Shruti Umat                     |  111701027

### Design  
---

* This is an object based design i.e. whatever a client draws from mouseDown to mouseUp event will be considered as one object.
* Only after a client completely draws an object i.e. do a mouseup, it will be passed to server and then be visible to other clients.
* All the clients as well as server will maintain all objects made by all the clients locally.
* At the time of connection establishment, the clock of a user is synchronised with that of the server. Timestamp is provided to each object while creating it on client side itself.

#### Initial Setup: A client logins and ask to create a new board or an already existing board on server

* Each client will maintain two stacks which will initially be empty.
    * Stack1 - for storing the objects on which this client has performed the operation (for undo operation).
    * Stack2 - for storing the objects on which this client has recently undone some operation.
* Each client will maintain a map of pixel to max-priority queue of objectId based on timestamp i.e. each node of priority queue will contain objectId and timestamp. 
* Each client and the server will maintain a map of objectId to object, this map will contain all the objects from all the clients.
* When a client ask for a new board or an already existing one from server, server sends the map from objectId to object and the map from pixel to priority queue.
* Processing module on the client side uses map of pixel to priority queue to sent the UI the vector of changed intensity and pixels for display.


### Feature Handling

#### 1. Draw using brush / draw shape

* Client#1 draws a random curve using brush or draw any shape.
* On mouseUp, UI will pass all the pixels and intensity value to us and we will create a corresponding object. This object will contain objectId, userId, timestamp, operation (to be done on this object, here it is create), pixels that belong to this object and intensity.
* Add this object to the stack going to be used for undo on client side. Also add this object to both the maps. 
* We will pass this object to the server.
    * Server will also add this object to both the maps.
    * Then send this object to all other clients.
* Processing module on other clients side will receive this object and add this object in their maps. Then process and pass the changes to UI. Hence whatever the client#1 had drawn will now be visible to all the other clients.

#### 2. Erase

* Using an eraser is equivalent to creating a new white object. The new white object will have a flag called "erase" indicating that this is an erase object.
* An erase object cannot be selected from UI (If the object at the top of a pixel position is an erase object, selection will not work from UI). 

#### 3. Select and Delete 

* UI will pass us the coordinate, where user has clicked after choosing the "select" feature. 
* At any pixel position, only the topmost i.e. the most recently draw object can be selected. On client side, we will check if there is any object at that pixel position using the map, if present then for representing the selected object we will send the object pixel to the UI. Now there are two possible cases -
* **Case 1: If the selected object was created by the same user.**
    * Delete the object from map of objectId to object on client side.
    * Push this object with operation "delete" on the undo stack.
    * Also delete it from the map of pixel to priority queue of objectId and timestamp.
    * The map of pixel to priority queue of objectId and timestamp will be used to know which object was present at a particular position, in case if the selected object was at the top of the priority queue, then the object present below it should now be visible. We will find the changes using map and pass it to UI.
    * In parallel the information of deleting this object is sent to server. 
    * Server will delete this object from its local maps and it will also notify other clients to delete this object from their local machine.
    * Processing module on the other clients side will also find the changes and pass changes to UI.
* **Case 2: If the selected object was created by some other user (client#2)**
    * client#1 deletes the object from map of objectId to object.
    * Also deletes it from the map of pixel to priority queue of objectId and timestamp.
    * As the object is now selected by client#1 the object will now belong to client#1. Hence we will change the userId of selected object from client#2 userId to client#1 userId.
    * Push this object with operation "delete" on the undo stack.
    * In parallel the information of deleting this object is send to the server. 
    * Server will delete this object from its local maps and it will also notify other clients to delete this object from their local machine.
    * Processing module on the other clients side first checks if the object belongs to that client, in case it does then remove all instances of the deleted objects from both the stack. Also delete object from both the maps and pass changes to UI.
* The above steps to handle the two possible cases will apply for select & rotate and select & colour change as well. This is because after a different user modifies (i.e. rotates, deletes, changes colour) a particular object of another user, the object belongs to the new user. Hence, undo and redo operations from previous user should not work.

#### 4. Select and Rotate 
* First, client has to select the object which it wants to rotate, UI will provide the position of mouse click and angle of rotation, and we will check if there is any object present at that position. 
* For representation of 'selection' operation we can send the object pixels to UI.
* If there is any object, we will calculate the difference in maximum and minimum coordinates along both the axes (x and y), calculate the centre of rotation and create a suitable rotation matrix. 
* We apply rotation operation on each of the pixel of object, using the rotation matrix and receive a new set of pixel positions. 
* We will perform the above delete feature for the previous object and create another object with the new set of pixel positions. This object will lie in the same layer (level) as it was before (we will give it the same objectId and timestamp as the previous object). 
* Use the above steps in "select and delete" to handle the user stacks and userId of the object. The operation stored in the user stack is "rotate".
* Also, parallely send the updated object to the central server.
* The central server than updates its maps and sends this info to all other clients in the same way as previous feature.

#### 5. Color Change
* First, client has to select the object whose color it wants to change, UI will provide the position of mouse click and new color, and we will check if there is any object at that position. 
* For representation of 'selection' operation we can send the object pixels to UI.
* We will perform the above delete feature for the previous object and create another object with the same set of positions but new intensity. This object will lie in the same layer (level) as it was before (we will give it the same objectId and timestamp as the previous object).
* Use the above steps in "select and delete" to handle the user stacks and userId of the object. The operation stored in the user stack is "colorChange".
* Also, parallely send the updated object to the central server.
* Server will update the colour of that object in the maps and pass this object to all other clients.
* All other clients will also update the color of that object, find the changed pixels and pass it to UI.

#### 6. Undo-Redo
* Each object contains the operation which was done on it (ex in case of draw operation is create)
* There can be four possible operations that can be on the board - Create an object, Delete an object, Change colour of an object, Rotate an object.
* Each client will have two stacks in their local machine. Size of stacks will be fixed. Hence Undo-redo will be available for fixed number of steps. 
* As each object contains operation performed on it, when any user clicks on undo button the top most object will be taken out from the undo stack and the operation performed on it will be reversed and transfered to the server.
    * If the operation is "create", then delete function will be called on that object and that object will be passed to the redo stack with operation as "create".
    * If the operation is "delete", then create function will called on that object and that object will be passed to the redo stack with operation as "delete".
    * If the operation is "colorChange", then colour change function will be called on that object with the previous colour as new color and current colour as the previous colour and this object will be passed to the redo stack with operation as "colorChange".
    * If the operation is "rotate", then rotate function will be called again with the angle 360-"rotatedAngle" on that object and the object will be passed in the redo stack with operation as "rotate".
* Similarly, if the redo button is clicked by any user, the top-most operation will be performed and we will pop the top object from the redo stack and will pass it to the undo stack.

#### 7. Interface with Network
* Serialisation and deserialisation 
    * Java objects can be directly serialised and deserialised.
    * To make a Java object serializable we can implement the java.io.Serializable interface. 
    * The ObjectOutputStream class contains writeObject() method for serializing an Object.
    * The ObjectInputStream class contains readObject() method for deserializing an object.
    * Netowrking interface has an identifier to indicate that the object belongs to processing module/content module. We will serialise the object and pass a json string as message to networking module. 
    
#### 8. Persistence
* Create a class for Persistence only on the server. An object of this class will contain the entire board state (two maps and all objects).
* The above object can be serialized and the obtained byte stream can be stored/retrieved from the disk.
* After retrieval, it can be deserialized to obtain the entire board state.

#### 9. Server Processing 

* There will be a main server running, which will listen for requests from new clients. 
    * If the board requested is already running then it will pass the Board State to the client,
    * Other wise request a port number from the networking module to run the Board Server on that port and then pass the port number with the initial Board State to the requested Client.
* Server also maintains information about all clients using the board. Whenever it receives an object for broadcast, it performs necessary operation for its local data and then send this object to all other clients through network.

### Activity Diagrams
---
<figure align="center">
    <img src="https://i.imgur.com/0gluMhj.png">
    <figcaption>Fig 1: Pass the changes from client to server</figcaption>
</figure>
<figure align="center">
    <img src="https://i.imgur.com/HqRD1oy.png">
    <figcaption>    Fig 2: Pass the changes from server to all clients</figcaption>
</figure>
<figure align="center">
    <img src="https://i.imgur.com/M3mMult.png">
    <figcaption>    Fig 3: Server Processing</figcaption>
</figure>
    
### Interface
---

```java
class Intensity {    
    int r, g, b;         // RGB values at a position
}

class Position {
    int x, y;            // pixel position
}

class Pixel {            // pixel position with RGB values
    Intensity intensity;    
    Position position;
}

public interface IDrawErase {
	
	/**
	 * drawCurve function will be called by UI to draw a random curve on the board.
	 * 
	 * @param pixels List of all the pixels of type Pixel, where curve is there on the board.
	 */
	void drawCurve (ArrayList <Pixel> pixels);
	
	/**
	 * erase function will be called by UI whenever erase is there on the screen using
	 * an eraser.
	 * 
	 * @param position List of all the position of type Position, where eraser is moved on the board.
	 */
	void erase (ArrayList <Position> position);
}
```

``` java
public interface IDrawShapes {
	/**
	 * drawCircle will be called when a circle is drawn on the board, it will process
	 * the  circle drawn.
	 * 
	 * @param center center position with intensity for circle
	 * @param radius radius of circle
	 */
	void drawCircle(Pixel center, float radius);
	
	/**
	 * drawSquare will be called when a square is drawn on the board, it will process
	 * the square drawn.
	 * 
	 * @param start start position for square with RGB values in whiteBoard
	 * @param length length of the square
	 */
	void drawSquare(Pixel start, float length);
	/**
	 * drawRectangle will be called when a Rectangle is drawn on the board, it will process
	 * the rectangle drawn.
	 * 
	 * @param start top left position with RGB values for rectangle in whiteBoard
	 * @param end bottom right position with RGB values for rectangle in whiteBoard
	 */
	void drawRectangle(Pixel start, Pixel end);
	
	/**
	 * drawLine will be called when a line is drawn on the board, it will process
	 * the line drawn.
	 * 
	 * @param start start position with RGB values
	 * @param end end position with RGB values
	 */
	void drawLine(Pixel start, Pixel end);        
}
```

```java
public interface IUndoRedo {
	/** redo will perform the redo operation */
	void redo();
	
	/** undo will perform the undo operation */
	void undo();
}
```

```java
public interface IOperation {
	/**
	 * select function will be called when the user will select an object on the board
	 * 
	 * @param positions list of all the Position when user clicked for selection
	 * @return List of all the position of the object selected
	 */
	ArrayList<Position> select (ArrayList <Position> positions);
	
	/**
	 * delete will delete the selected object
	 */
	void delete ();
	
	/**
	 * color change will change the color of the selected object
	 *
	 * @param intensity new color to be given to the selected object
	 */
	void colorChange (Intensity intensity);
    
	/**
	 * rotate will rotate the selected object with a given angle
	 * @param angleCCW angle through which selected object to be rotated
	 */
	void rotate (Angle angleCCW);
	
	/** reset will clear all the objects on the screen */
	void reset ();
}
```

```java
public interface IUser {
	/**
	 * This will be used by the UI module in the start to give the user details to the
	 * processing module.
	 * 
	 * @param userName Username of the user
	 * @param ipAddress IP Address of the server
	 * @param boardId Board ID of the requested board, if any
	 * @return It will return the userId to the UI module
	 */
	String giveUserDetails(String userName, String ipAddress, String boardId);
	
	/**
	 * getUser will return the User who has drawn the particular object
	 * 
	 * @param positions List of position where a part of object is there, whose
	 * user they need to identify
	 * @return UserId of the user
	 */
	String getUser(ArrayList<Position> positions);
	
	/**
	 * when user will close the application, UI module will call this function
	 */
	void stopBoardSession();
	
	/**
	 * UI module will first subscribe for changes on the board from other clients
	 * 
	 * @param identifier identifier provided by the UI module to identify the changes
	 * @param handler appropriate handler to handle the changes
	 */
	void subscribeForChanges(String identifier, IChanges handler);
}

public interface IChanges {
	
	/** 
	 * getChanges will take all the changes as the input and passed to the UI
	 * 
	 * @param pixels List of all the pixels where there is a change
	 */
	void getChanges(ArrayList<Pixel> pixels);
}
```

```java
public interface IClientIP {
	
	/**
	 * This interface will be used by the content module to get the IP addresses of
	 * all the clients connected to the particular board.
	 * getClientIP will return a Map of all the clients to IP
	 * 
	 * @return Map from all the clients to IP
	 */
	public Map<Username, IpAddress> getClientIP(); 
	
}

```
### Tasks Distribution
---
#### Ahmed Zaheer Dadarkar 
* Deserialisation and serialisation of objects to be sent and received from server.
* Handle processing related to drawing standard shapes (line segment, circle, square, rectangle).
* Persistence support on the central server. 
* Review Server Processing.

#### Devansh Singh Rathore
* Handle processing related to below operations: 
    * Change color of object 
    * Rotate object (in multiples of 90 degree)
    * Extend the rotate option for an angles (optional)
* Review processing related to Undo-Redo.

#### Himanshu Jain 
* Do server side processing which includes:
    *  Spwan a thread for a new board server and pass board objects to new user.
    *  Broadcasting the messages received from the clients
    *  Perform processing related to whiteboard objects received from other clients
* Write Main file for processing on client side, including request from UI, passing changes to UI.
* Review processing related to Draw, Erase and Reset.

#### Rakesh Kumar
* Handle all operations related to undo-redo on client side. 
* Review processing related to deserialisation & serialisation, draw standard shape, tag object.

#### Sakshi Rathore (Team lead)
* Testing
* Review 
* Interface and integration with other modules

#### Satchit Desai
* Handle processing related to below operations: 
    * Draw random curve using brush
    * Erase
* Provide API to clear the board 
* Review processing related to select & delete.

#### Shruti Umat
* Handle processing related to below operations: 
    * Select an object and returns tag for selected object
    * Delete an object
* Review processing related to color change and rotate.

### Analysis
---
* We used **Object Based design** instead of pixel based as it is far more efficient and easy to process. Instead of working on each pixels again and again we are operating on objects.
* Since each client is storing all the objects there is data redundancy but this reduces a lot of load on server and network. Processing module on each client can process the data independently and any user can perform operation on object made by some other user. 
* We are maintaining a **single central server** which will handle new clients coming and passing the objects to all the clients. It will also take care of persistance in the project. 
* We are using a **map from postion to list of objectID** on that postion, so that we can access the objects on any point on the board in constant time. But for this we are storing objectID for each position on the board, so data redundancy will be there, instead if we do a linear search on all the objects, it will not be time efficient. So here we did a trade-off between memory and time, and choose for time efficiency.

