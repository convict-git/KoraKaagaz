package processing;

import java.util.*;

/**
*
* @author Himanshu Jain
*/

public interface IDrawErase {
	
	//will draw the curve on given pixels
	void DrawCurve (ArrayList <Pixel> pixels);
	
	//will work as an eraser on the Board
	void Erase (ArrayList <Position> position);
}
