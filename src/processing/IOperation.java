package processing;

import java.util.*;
import processing.utility.*;

/**
 * IOperation interface provides the functions for processing various operations on
 * any object on the board.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IOperation {
	/**
	 * select function will be called when the user will select an object on the board
	 * 
	 * @param positions list of all the Position when user clicked for selection
	 * @return List of all the position of the object selected
	 */
	ArrayList<Position> select (ArrayList <Position> positions);
	
	/**
	 * delete will delete the selected object
	 */
	void delete ();
	
	/**
	 * color change will change the color of the selected object
	 *
	 * @param intensity new color to be given to the selected object
	 */
	void colorChange (Intensity intensity);
    
	/**
	 * rotate will rotate the selected object with a given angle
	 * @param angleCCW angle through which selected object to be rotated
	 */
	void rotate (Angle angleCCW);
	
	/** reset will clear all the objects on the screen */
	void reset ();
}
