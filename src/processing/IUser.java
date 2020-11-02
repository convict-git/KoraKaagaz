package processing;

import java.util.*;
import processing.utility.*;

/**
 * IUser will provide functions for getting user details and also it
 * provides subscribeForChanges function.
 *
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IUser {
	/**
	 * This will be used by the UI module in the start to give the user details to the
	 * processing module.
	 * 
	 * @param userName Username of the user
	 * @param ipAddress IP Address of the server
	 * @param boardId Board ID of the requested board, if any
	 * @return It will return the Board ID to the UI module
	 */
	String giveUserDetails(String userName, String ipAddress, String boardId);
	
	/**
	 * getUser will return the User who has drawn the particular object
	 * 
	 * @param positions List of position where a part of object is there, whose
	 * user they need to identify
	 * @return Username of the user
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
