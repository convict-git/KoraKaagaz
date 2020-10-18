package processing;

import java.util.*;
import processing.utility.*;

/**
*
* @author Himanshu Jain
*/

public interface IUser {
    // this function is used to get the user details during initial setup from user through UI 
    // if the board Id is null then we get a new board id from server and pass to UI
    // parameter: userName - user name
    //            ipAddress - ipaddress for the server with the port
    //            boardId - white board ID to which user wants to connect 
    String getUserDetails(String userName, String ipAddress, String boardId);
    
    // return the username at the selected position
    String getUser(ArrayList<Position> positions);
    
    // stop baord session for that user
    void stopBoardSession();
    
    // UI will subscribe for any changes 
    void subscribeForChanges(String identifier, IChanges handler);
}
