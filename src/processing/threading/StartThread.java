package processing.threading;

import processing.ClientBoardState;

/**
 * This will spawn a thread to wait for the Board ID from
 * the server.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */
public class StartThread implements Runnable{
	
	@Override
	public void run() {
		while(ClientBoardState.boardId != null) {
			
			// wait for the Board ID from the server
			
		}
	}

}
