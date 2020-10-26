package processing;

import processing.utility.*;

/**
 * This interface provides function for drawing shapes on the board.
 * These function will be used by the UI module.
 *
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IDrawShapes {
	/**
	 * drawCircle will be called when a circle is drawn on the board, it will process
	 * the  circle drawn.
	 * 
	 * @param center center position with intensity for circle
	 * @param radius radius of circle
	 */
	void drawCircle(Pixel center, float radius);
	
	/**
	 * drawSquare will be called when a square is drawn on the board, it will process
	 * the square drawn.
	 * 
	 * @param start start position for square with RGB values in whiteBoard
	 * @param length length of the square
	 */
	void drawSquare(Pixel start, float length);
	/**
	 * drawRectangle will be called when a Rectangle is drawn on the board, it will process
	 * the rectangle drawn.
	 * 
	 * @param start top left position with RGB values for rectangle in whiteBoard
	 * @param end bottom right position with RGB values for rectangle in whiteBoard
	 */
	void drawRectangle(Pixel start, Pixel end);
	
	/**
	 * drawLine will be called when a line is drawn on the board, it will process
	 * the line drawn.
	 * 
	 * @param start start position with RGB values
	 * @param end end position with RGB values
	 */
	void drawLine(Pixel start, Pixel end);        
}