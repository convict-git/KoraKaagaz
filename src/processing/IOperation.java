package processing;

import java.util.*;

/**
*
* @author Himanshu Jain
*/

public interface IOperation {
    // returns the pixel position for selected object if  an object exist at selected position  
    ArrayList<Position> select (ArrayList <Position> positions);
    
    // deletes the selected object
    void delete ();
    
    // changes color of selected objected to specified intensity 
    void colorChange (Intensity intensity);
    
    // rotates selected object by specified angle in counter clockwise direction
    void rotate (double angleCCW);
    
    // removes all the object present on the board
    void reset ();
}
