package processing;

import java.util.ArrayList;
import processing.utility.*;
import processing.server.board.*;
import processing.boardobject.*;
import networking.utility.*;
import networking.*;
import infrastructure.validation.logger.*;

/**
 * Implements some of the User APIs
 * 
 * @author Rakesh Kumar
 * @reviewer Devansh Singh Rathore
 */

public class UserUtil {
	
    // gets the logger instance
    private static ILogger logger = LoggerFactory.getLoggerInstance();
	
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
                                              ) {
    		// stores the information
        ClientBoardState.username  = new Username(username);
        ClientBoardState.serverIp  = new IpAddress(ipAddress);

        if (boardId == null) {
            ClientBoardState.boardId = null;
        }
        else {
            ClientBoardState.boardId = new BoardId(boardId);
        }
		
        // gets the client information (ip address + free port)
        ClientInfo clientInfo = CommunicatorFactory.getClientInfo();
        logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS,
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Got the client information"
                   );
    	
        // stores the user ip and port number
        ClientBoardState.userIP  = new IpAddress(clientInfo.getIp());
        Port userPort  = new Port(clientInfo.getPort());
        ClientBoardState.userPort = userPort;

        // Initialize the data members of the ClientBoardState
        ClientBoardState.maps = new BoardState();
        ClientBoardState.undoStack = new ArrayList <BoardObject>();
        ClientBoardState.redoStack = new ArrayList <BoardObject>();
        ClientBoardState.userId = new UserId(
                                             ClientBoardState.userIP,
                                             ClientBoardState.username
                                             );
    	
        // gets a communicator and starts it
        ClientBoardState.communicator = CommunicatorFactory.getCommunicator(userPort.port);
        ClientBoardState.communicator.start();
        // starts the client side processing
        ClientBoardState.start();
    	
        logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS, 
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Started communicator and the ClientBoardState"
                   );
        return;
    }
    
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
    
    public static String getUser(ArrayList <Position> positions) {
    	
    		// null object
    		if (positions == null) {
            logger.log(
                       ModuleID.PROCESSING, 
                       LogLevel.WARNING, 
                       "[#" + Thread.currentThread().getId() + "] "
                       + "Got a null ArrayList in getUser"
                       );
            return null;
    		}
    	
    		// gets the id of the object occurring maximum times at these positions
    		ObjectId objectId = ClientBoardState
            .maps
            .getMostProbableObjectId(positions);
    	
    		String maxOccUser = null;
    	
    		// if a object found
    		if (objectId != null) {
            // gets the username for the above object
            maxOccUser = ClientBoardState
    						.maps
    						.getUserNameOfObject(objectId);
    		}
    	
    		logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS, 
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Found the user at the selected positions"
                   );
    		return maxOccUser;
  	}
    
    /**
     * Stops the board session for the user
     */
    public static void stopBoardSession() {
    	
        IServerCommunication serverComm = new ServerCommunication();
    		// Informs the server about closing the session of the user
    		serverComm.stopConnection();
    	
    		// stops the communicator (all its threads)
    		ClientBoardState.communicator.stop();
    		logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS, 
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Server is informed and Communicator is stopped"
                   );
    		return;
    }
        
}
