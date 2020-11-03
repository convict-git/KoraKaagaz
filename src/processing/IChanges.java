package processing;

import java.util.*;
import processing.utility.*;

/**
 * This interface will be implemented by UI to get the changes on
 * the board.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IChanges {
	
	/** 
	 * getChanges will take all the changes as the input and passed to the UI
	 * 
	 * @param pixels List of all the pixels where there is a change
	 */
	void getChanges(ArrayList<Pixel> pixels);
	
	/**
	 * giveSelectedPixels will pass the list of pixels belonging to the
	 * selected object, if the selected object got changed in between
	 * by any other client.
	 * 
	 * @param pixels list of pixels belonging to the selected object
	 */
	void giveSelectedPixels(ArrayList<Pixel> pixels);
}
