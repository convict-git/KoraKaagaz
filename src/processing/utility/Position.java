package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to row and column
// of a position
public class Position {
	public int r, c; // r: row, c: column
	
	// Constructor for Position class
	public Position(int row, int column) {
		r = row;
		c = column;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position pos = (Position) obj;
			return
				r == pos.r
				&&
				c == pos.c;
		}
		else
			return false;
	}
}
