package processing.utility;

/**
 * Class Representing a Position on the Board
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Position {
	/**
	 * r Row number
	 * c Column number
	 */
	public int r, c;
	
	/**
	 * Position Constructor
	 * 
	 * @param row Row number
	 * @param column Column number
	 */
	public Position(int row, int column) {
		r = row;
		c = column;
	}
	
	/** Copy Constructor */
	public Position(Position posObject) {
		r = posObject.r;
		c = posObject.c;
	}
	
	/** Equals Method */
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
