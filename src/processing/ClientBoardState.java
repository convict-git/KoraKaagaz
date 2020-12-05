package processing;

import java.util.*;
import processing.server.main.*;
import processing.threading.HandleSend;
import networking.ICommunicator;
import processing.handlers.*;
import processing.boardobject.*;
import processing.utility.*;
import infrastructure.validation.logger.*;

/**
* This class contains all the info of the current board.
*
* @author Himanshu Jain
* @reviewer Ahmed Zaheer Dadarkar
*/

public class ClientBoardState {
	
	/**
	 * maps is an object of BoardState which stores both the maps which
	 * we need for persistence on the server.
	 */
	public static BoardState maps;
	
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
	public static ArrayList <BoardObject> undoStack;
	public static ArrayList <BoardObject> redoStack;
	
	/**
	 * Will remain empty on the client side, as there is no need to know 
	 * all the users to the client.
	 * Server will use this users List to maintain the list of all the users 
	 * connected to this Board.
	 */
	public static Map <UserId, IpAddress> users;
	
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
	public static final BrushRadius brushSize = new BrushRadius(1);
	
	/**
	 * This is the communicator that we will get from the networking module.
	 * It is saved here so that we don't need to call the networking module function
	 * again and again.
	 */
	public static ICommunicator communicator;
	
	/**
	 * boardDimension will store the dimensions of the canvas board defined
	 * by the UI. This will be used while drawing shapes.
	 */
	public static final Dimension boardDimension = new Dimension(720,1100);
	
	/**
	 * serverPort will store the port number of the Main Server which is kept
	 * fixed as 8467 by default.
	 */
	public static Port serverPort = new Port(8467);
	
	// logger of the infrastructure module for printing logs
	public static ILogger logger = LoggerFactory.getLoggerInstance();
	
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
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for receiving Board objects from the server"
		);
		
		communicator.subscribeForNotifications(
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
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for receiving Board State after start of board server"
		);
		
		communicator.subscribeForNotifications(
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
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for receving Port number of the Board Server from the main server"
		);
		
		communicator.subscribeForNotifications(
				"ProcessingServerPort", 
				new PortHandler()
		);
		
		/**
		 * Subscribing for receiving Board ID from the Main Server passing identifier as
		 * "ProcessingBoardId" and handler as the object of BoardIdHandler class.
		 */
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Subscribing for receiving BoardID from the server"
		);
		
		communicator.subscribeForNotifications(
				"ProcessingBoardId", 
				new BoardIdHandler()
		);
		
		/**
		 * If the board ID is null then the user is requesting for a new board, otherwise
		 * it request for an existing board with the given board ID.
		 * Call the respective function defined in the interface IRequests in
		 * processing.server.main package which is implemented in the class Requests.
		 */
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Making Board Request to the server"
		);
		
		IRequests request = new Requests();
		if(boardId == null) {
			request.requestForNewBoard(userIP, userPort);
		} else {
			request.requestForExistingBoard(boardId, userIP, userPort);
		}
		
		/**
		 * We need to wait until we receive BoardID from the server.
		 * This start function will be called by the giveUserDetails function
		 * in IUser and it will be run in a thread in the processor class, so 
		 * this while loop will not halt the process.
		 */
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"[#" + Thread.currentThread().getId() + "] "
				+ "Waiting to receive boardID from the server"
		);
		
		// wait till we receive boardID from the server
		while(boardId == null) {
			
			try {
				Thread.sleep(50);
			} catch(Exception e) {
			
				logger.log(
						ModuleID.PROCESSING, 
						LogLevel.ERROR, 
						"[#" + Thread.currentThread().getId() + "] "
						+ "Error while waiting for boardID from the server"
				);
				
			}
			
		}
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
		return selectedObject;
	}
	
	/**
	 * wrapper send function of the networking module send so as to spawn a thread
	 * and then call the send function of networking module inside that thread.
	 * 
	 * @param address address of the receiver
	 * @param message message to be sent
	 * @param identifier identifier of the message
	 */
	public static void send(String address, String message, String identifier) {
		
		// create a runnable object of the class HandleSend
		HandleSend runnable = new HandleSend(address,message,identifier);
		
		// spawn a new thread with runnable as the object
		Thread handleSend = new Thread(runnable);
		
		// start the process in the thread
		handleSend.start();
		
	}
	
	
	/**
	 * When in start client will receive the board State from the server as the persistence,
	 * then we need to pass the changes of the earlier objects to the UI. 
	 * 
	 * So after receiving the board State, it's handler will call this function.
	 */
	public static void sendBoardState() {
		
		CommunicateChange.identifierToHandler
			.get(CommunicateChange.identifierUI)
			.getChanges(maps.getPixelsAtTop(maps.getPositions()));
		
	}
	
}
