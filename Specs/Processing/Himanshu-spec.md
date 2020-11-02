# Design Spec
> By Himanshu Jain - 111701013

## Objective:
* To implement server side processing which includes - 
    * Create a new process for a new board server.
    * Pass the board state of the board which a new user requested.
    * Perform processing related to white board objects received from other clients.
    * Broadcasting the message received from other clients.
* To write main file for processing on client side which includes - 
    * Receive changes from UI and redirect it to the class handling the given operation.
    * Send the changes to the server, after processing it.
    * Receive changes from the server.
    * Pass the changes to the UI for display.

## Server Side Processing
There will be two kinds of server, one is the **Main server** which will handle request of new clients and other will be a **Board Server** which will be a separate server for all the boards running currently.

### New User joins
* When a new user joins the application, there are two cases possible when the client communicates with server-
#### Case 1. To connect to an older board
* After joining the session, user will be asked to enter a boardId or to start a new board, here, if he wants to connect to an older board he will enter the boardId of the older board.
* Main server will maintain two maps
    * One will be from boardId -> persistence file path 
    * Second map will store all the boards currently running and map with their corresponding port number where it's board server is active.
* Main server will receive the request of connecting to an older board. Now two possibilities are there
    * If the board requested is already running, then it will use the second map to find the port number on which this board's server is running and return this port number to the client.
    * If the board requested is not currently running, then the main server will find the path to it's persistence file using the first map, then it will ask the networking module to provide a free port number on server's machine to run the new board server. Then the main server will run a new process on the given port number with data we got from it's persistence file and return the port number to the new user.
* When the client processing module will get the port number, it will ask for the persistent data from the board server by sending getData request to the given port number on server's machine.
* When the board server receive request from the user to send the data, it will send the board state to the user.

#### Case 2. To set up a new board
* Main server will receive a new board request and ask the networking module to give a free port number.
* Main server will then start a new Board Server on the given port number with data on the board as empty (as it's a new board, so no persistent was there).
* Main server will then send the port number back to the client requested a new board.

### After User is connected to the Board Server
* Once the user received the port number and the board state (in case of connecting to older board), then further changes on the board will be communicated to it's respective board server on the given port number.
* Board Server will maintain a list of all the clients connected to it, along with their ip addresses.
* Board Server will also maintain all the BoardObjects made by any client, so that when any client joins it can pass the board state to it.
* Board Server will listen for any changes from all it's clients, and upon receiving a BoardObject from any of the client, it will do the following
    * It will pass that BoardObject to all the other clients.
    * It will also do all the processing related to that object which any other client would have done upon receiving that BoardObject.

## Client Side Processing

### New User joins
* UI will pass whether the new user wants to start a new board or an older board.
* We will pass the above request to the main server and it will pass us the port number where the board server for the requested board is running on server's machine.
* Then if the board requested is an older board, then the client will ask for the board state from the board server otherwise it will initialse all its data structures to be initially empty.

### After User is connected to the Board Server
* When user will perform any operation on the board, UI will pass the changes.
* Then after doing all the processing, this object will be send to the corresponding Board Sever.
* Another client connected to this Board Server will then receive this object and again do all the processing related to it.
* Then finally this other client will pass the changes to the UI.
* For every changes send by UI or from any other client, a new thread will be spawned and then all the processing related to that object will be performed.

![](https://i.imgur.com/JrVCKZM.png)


## Communication Design between Server and Client
* Client will send requests to the Main Server, so Main Server will subscribe for notifications to the networking module with different identifiers (RequestForNewBoard, RequestForExistingBoard, RequestForPortNumber).
* Main Server will have different handler class for all these identifiers, and will provide object of these handler classes while subscribing to the networking module for notification.
* Client will subscribe for notifications to the networking module with different identifiers (ProcessingObject, ProcessingBoardState, PortRequest, BoardIdRequest).
* Client will have different handler class for all these identifiers and will will provide object of these handler classes while subscribing to the networking module for notification.

![](https://i.imgur.com/OEvE46U.png)


## Interfaces
### Sever Side Interfaces
#### Main Server
```java
/* This interface will be used by the client side processing to make
new board requests */
public interface IRequests {
    /*
    When client request for a new board, client processing module will
    call this function.
    input - NULL
    output - Port Number on which the requested Board Server will be 
    running on Server's machine
    */
    Port requestForNewBoard();
    
    /*
    When client request for an existing board, client porcessing module will
    call this function.
    input - boardId
    output - Port Number on which the requested Board Server will be
    running on Server's machine
    */
    Port requestForExistingBoard(String boardId);
    
    /*
    getPortNumber will return the Port number where the given board is running
    input - boardId
    output - If board is running then it's Board Server's port number
             else it will throw BoardNotFound exception
    */
    Port getPortNumber(String boardId);
}
```

#### Board Server
```java
/* This interface will be used by the client after connected to the board
for making further communication with the server */
public interface IServerCommunication {
    /*
    getBoardState function will be called by the client processing module
    once they received the port number is they requested for an existing
    board.
    input - NULL
    output - BoardState object, which contains all the previous board data
    */
    public BoardState getBoardState();
    
    /*
    When the client need to pass any changes, it will call this function and
    pass the BoardObject (change) as the parameter.
    input - BoardObject object
    ouput - NULL
    */
    public void sendObject(BoardObject obj);
    
    /*
    When client wants to know their boardId, they will call this function.
    input - NULL
    output - BoardId
    */
    public BoardId getBoardId();
    
    /*
    When client leave the board, client processing module will inform the
    board server using this function.
    input - NULL
    output - NULL
    */
    public void stopConnection();
}
```

### Client Side
```java
/* While subscribing for receiving objects from the server, we will give
handler as the object of this class */
public class ObjectHandler implements INotificationHandler {
    //will handle when an object will be received from other clients
    public void onMessageReceived(String message);
}
```

```java
/* While subscribing for receiving the BoardState of the existing board,
we will give handler as the object of this class */
public class BoardStateHandler implements INotificationHandler {
    //will handle when the whole BoardState will be received from other clients
    public void onMessageReceived(String message);
}
```

```java
/* When subscribing for receiving port number of the Board Server we will 
give handler as the object of this class */
public class PortHandler implements INotificationHandler {
    //will handle when server will send the requested service
    public void onMessgaReceived(String message);
}
```

```java
/* When subscribing for receiving port number of the Board Server we will
give handler as the object of this class */
public class BoardIdHandler implements INotificationHandler {
    //will handle when server will send the boardId
    public void onMessageReceived(String message);
}
```


## Some Other Classes
```java
//BoardState class contains all the data on the board, it will be there for
//each client and also for the Board Server
public class BoardState implements Serializable {
    //will store the boardId of the board
    public BoardId boardId;
    
    //will store the select object on the board, if any object is selected 
    //otherwise NULL
    public ObjectId selectedObject;
    
    //map from userid to username
    public Map <UserId, UserName> idToName;
    
    // map from pixel to priority queue (max-heap) of (timestamp, objectId)
    // based on timestamp (max timestamp has more priority)
    public Map <Position, PriorityQueue <Pair <ObjectId, timestamp> >
    (0, new ObjectComparator()) > pixelToPQ;

    // map from objectId to Object
    public Map <ObjectId, BoardObject> objIdToBoardObject;
    
    //undo and redo stacks which will store BoardObjects made by the 
    //particular user
    public List<BoardObject> undoStack;
    public List<BoardObject> redoStack;
    
    /* Some functions will also be there to access or modify the private
    data members of this class */
}
```

```java
/* This class provides some utility functions while processing the board
objects */
public class Utilities {
    /*
    This function will return the current timestamp
    input - NULL
    output - Current time
    */
    public static Date getTimestamp();
    
    /*
    This function will give the boardId on which they are working
    input - NULL
    output - board ID
    */
    public static BoardId getBoardId();
    
    /*
    This function will give the client's IP address
    input - NULL
    output - IP Address
    */
    public static IpAddress getIpAddress();
    
    /*
    This function will give the Board Server's IP Address
    input - NULL
    output - Server's IP Address
    */
    public static String getServerIpAdress();
}
```

## Design Analysis
* For creating a new board sessing instead of spawning a new thread for it, we are creating a separate process which will run the Board Server. This way we can start as many board sessions as we want and all the board sessions will run parallely using the multithreading defined by the operating system, which will be more efficient.
* For every BoardObject received by the client whether from UI or from other clients we are spawning a new thread for it's processing which will make the processing faster and efficient.
* There will be a two level hierarchy design pattern for implementing interfaces to be used by the UI module. This way UI module need not be aware of all the classes of the processing module except for one "Processor" class.