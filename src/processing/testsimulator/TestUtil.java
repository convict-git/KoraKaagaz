package processing.testsimulator;

import java.util.ArrayList;

import infrastructure.validation.logger.*;
import networking.INotificationHandler;
import processing.*;
import processing.boardobject.BoardObject;
import processing.server.main.*;
import processing.server.main.ServerState;
import processing.testsimulator.network.*;
import processing.utility.*;

/**
 * Utility functions for processing test.
 * 
 * @author Sakshi Rathore
 *
 */

public class TestUtil {
	
	public static void initialiseProcessorForTest(INotificationHandler handler) {
		/* Get logger instance */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Initialise the variables in ClientBoardState */
		String username = "Tester";
		String ipAddress = "192.168.1.2";
		String boardId = "1";
		String userIP = "192.168.1.1";
		Port userPort  = new Port(8080);
		
		ClientBoardState.username  = new Username(username);
		ClientBoardState.serverIp  = new IpAddress(ipAddress);
		ClientBoardState.boardId   = new BoardId(boardId);
		ClientBoardState.userIP  = new IpAddress(userIP);
		ClientBoardState.userPort = new Port(userPort);
		ClientBoardState.userId = new UserId(
				ClientBoardState.userIP,
				ClientBoardState.username
				);
		
		ClientBoardState.communicator = CommunicatorFactory.getCommunicator();
		ClientBoardState.communicator.start();
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast", 
				handler
				);
		
		ClientBoardState.maps = new BoardState();
		ClientBoardState.redoStack = new ArrayList<BoardObject>();
		ClientBoardState.undoStack = new ArrayList<BoardObject>();
		
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Test: Started communicator and intialised ClientBoardState"
		);
	}
	
	public static void initialiseMainServerForTest() {
		
		/* set network communicator of test in server */
		ServerState.communicator = CommunicatorFactory.getCommunicator();
		
		ServerState.communicator.start();
		
		ServerState.communicator.subscribeForNotifications(
				"NewBoard",
				new NewBoardRequestHandler()
		);
		
		ServerState.communicator.subscribeForNotifications(
				"ExistingBoard", 
				new BoardRequestHandler()
		);
		
		ServerState.communicator.subscribeForNotifications(
				"RemoveBoard", 
				new RemoveBoardHandler()
		);
	}
}
