package processing;

import java.util.*;
import processing.utility.*;

/**
* This interface will be implemented by UI to get the changes on
* the board.
* 
* @author Himanshu Jain
*/

public interface IChanges {
	
	/***
	 * getChanges will take all the changes as the input and passed to the UI
	 * 
	 * @param pixels List of all the pixels where there is a change
	 */
	void getChanges(ArrayList<Pixel> pixels);
}
