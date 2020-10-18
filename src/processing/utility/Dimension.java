package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Dimension
public class Dimension {
	public int numRows, numCols;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Dimension) {
			Dimension dim = (Dimension) obj;
			return 
				numRows == dim.numRows
				&&
				numCols == dim.numCols;
		}
		else
			return false;
	}
}
