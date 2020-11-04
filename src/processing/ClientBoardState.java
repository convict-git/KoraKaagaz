package processing;

import java.util.*;
import processing.threading.*;
import networking.ICommunicator;
import processing.handlers.*;
import processing.boardobject.*;
import processing.utility.*;

/**
* This class contains all the info of the current board.
*
* @author Himanshu Jain
*/

public class ClientBoardState {
	
	/**
	 * maps is an object of BoardState which stores both the maps which
	 * we need for persistence on the server.
	 */
	public static BoardState maps = new BoardState();
	
	/**
	 * portNumber stores the port number of the Board Server to make 
	 * communications from the server
	 */
	public static Port portNumber;
	
	/** boardId will store the Board ID of the board to which
	 * the client is connected to.
	 */
	public static BoardId boardId;
	
	/**
	 * undo and redo stacks. We need to remove objects from between on the stacks
	 * so instead of stack we used ArrayList for efficient computation.
	 */
	public static ArrayList <BoardObject> undoStack = new ArrayList <BoardObject>();
	public static ArrayList <BoardObject> redoStack = new ArrayList <BoardObject>();
	
	/**
	 * Will remain empty on the client side, as there is no need to know 
	 * all the users to the client.
	 * Server will use this users List to maintain the list of all the users 
	 * connected to this Board.
	 */
	public static ArrayList <String> users = new ArrayList <String>();
	
	/**
	 * selectedObject will store the object that is currently selected by this client.
	 * It is of type PriorityQueueObject which contains object ID and timestamp.
	 * We need both so as to get the top most object from the priority queue.
	 */
	private static PriorityQueueObject selectedObject;
	
	/**
	 * These are some of the user details like username, User ID
	 * User IP Address and the port of this user which is given
	 * by the networking module.
	 */
	public static Username username;
	public static UserId userId;
	public static IpAddress userIP;
	public static Port userPort;
	
	/**
	 * This stores the IP Address of the Main Server, it will be given by the user manually.
	 * It will remain constant.
	 */
	public static IpAddress serverIp;
	
	/**
	 * brushSize will store the radius of the brush which will be used while drawing
	 * different shapes, it is given by the UI for default.
	 */
	public static BrushRadius brushSize;
	
	/**
	 * This is the communicator that we will get from the networking module.
	 * It is saved here so that we don't need to call the networking module function
	 * again and again.
	 */
	public static ICommunicator communicator;
	
	/**
	 * This will be called at the start of the program to setup the connection 
	 * from the networking. To subscribe for notifications for different events that 
	 * can happen, it also spawn a thread to wait till we get the board ID in case 
	 * a new board is requested as we need to return the boardID to the UI in the start 
	 * of the process.
	 */
	public static void start() {
		
		/**
		 * Subscribing for receiving objects made by the other clients
		 * from the server, passing identifier as "ProcessingObject" and
		 * handler is object of ObjectHandler class.
		 * This same identifier will be used by the Board Server to send the
		 * board object which it receives from any client to broadcast it.
		 */
		ClientBoardState.communicator.subscribeForNotifications(
				"ProcessingObject", 
				new ObjectHandler()
		);
		
		/**
		 * Subscribing for receiving BoardState from the BoardServer if the board
		 * requested is an existing board, passing identifier as "ProcessingBoardState"
		 * and handler as the object of BoardStateHandler class.
		 * This same identifier will be used by the Board Server to send the Board State
		 * as soon as the Board Server starts.
		 */
		ClientBoardState.communicator.subscribeForNotifications(
				"ProcessingBoardState", 
				new BoardStateHandler()
		);

		/**
		 * Subscribing for receiving Port number of the Board Server for further
		 * communications, passing identifier as "ProcessingServerPort" and handler
		 * as the object of PortHandler class.
		 * The main server will send the port using the same identifier as soon as it
		 * starts the Board Server.
		 */
		ClientBoardState.communicator.subscribeForNotifications(
				"ProcessingServerPort", 
				new PortHandler()
		);
		
		/**
		 * Subscribing for receiving Board ID from the Main Server passing identifier as
		 * "ProcessingBoardId" and handler as the object of BoardIdHandler class.
		 */
		ClientBoardState.communicator.subscribeForNotifications(
				"ProcessingBoardId", 
				new BoardIdHandler()
		);
		
		/**
		 * We need to return the Board ID back to the UI when the process starts. As
		 * this function is called in the start of the process it will wait till we receive
		 * the BoardID from the Main Server so for it we need to spawn a new thread to wait 
		 * for the BoardID from the server.
		 */
		Thread waitForBoardID = new Thread(new StartThread());
		waitForBoardID.start();
	}
	
	/**
	 * As selectedObject is private we need to give setter function.
	 * 
	 * @param selectedObject object to be set as the selected object
	 */
	public static synchronized void setSelectedObject(PriorityQueueObject selectedObject) {
		ClientBoardState.selectedObject = selectedObject;
	}
	
	/**
	 * As selectedOBject is private member we need to give getter function
	 * 
	 * @return the selectedObject
	 */
	public static synchronized PriorityQueueObject getSelectedObject() {
		return ClientBoardState.selectedObject;
	}
	
	/**
	 * boardDimension will store the dimensions of the canvas board defined
	 * by the UI. This will be used while drawing shapes.
	 */
	public static Dimension boardDimension;
	
	/**
	 * serverPort will store the port number of the Main Server which is kept
	 * fixed as 8467 by default.
	 */
	public static Port serverPort = new Port(8467);
	
}
