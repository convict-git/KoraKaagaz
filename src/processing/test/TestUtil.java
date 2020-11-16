package processing.test;

import infrastructure.validation.logger.*;
import processing.ClientBoardState;
import processing.utility.*;

public class TestUtil {
	
	public static void initialiseProcessorForTest() {
		
		ILogger logger = LoggerFactory.getLoggerInstance();
		
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
		ClientBoardState.userId = new UserId(ClientBoardState.userIP, ClientBoardState.username);
		ClientBoardState.communicator = CommunicatorFactory.getCommunicator();
		ClientBoardState.communicator.start();
//		ClientBoardState.start();
    	ClientBoardState.communicator.subscribeForNotifications("ObjectBroadcast", new ServerObjectHandler());
    	logger.log(
    			ModuleID.PROCESSING, 
    			LogLevel.SUCCESS, 
    			"Started communicator and the ClientBoardState"
    	);
    	
	}
}
