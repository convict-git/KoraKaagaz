package processing;

import java.util.*;
import processing.utility.*;

/**
 * This interface provides basic draw erase function on the board.
 * This will be used by UI
 *
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IDrawErase {
	
	/**
	 * drawCurve function will be called by UI to draw a random curve on the board.
	 * 
	 * @param pixels List of all the pixels of type Pixel, where curve is there on the board.
	 */
	void drawCurve (ArrayList <Pixel> pixels);
	
	/**
	 * erase function will be called by UI whenever erase is there on the screen using
	 * an eraser.
	 * 
	 * @param position List of all the position of type Position, where eraser is moved on the board.
	 */
	void erase (ArrayList <Position> position);
}
