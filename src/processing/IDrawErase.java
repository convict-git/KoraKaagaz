package processing;

import java.util.*;
import processing.utility.*;

/**
*
* @author Himanshu Jain
*/

public interface IDrawErase {
	
	//will draw the curve on given pixels
	void drawCurve (ArrayList <Pixel> pixels);
	
	//will work as an eraser on the Board
	void erase (ArrayList <Position> position);
}
