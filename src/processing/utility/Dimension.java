package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Dimension
public class Dimension {
	// Number of rows and columns
	public int numRows, numCols;
	
	// Constructor for Dimension Class
	public Dimension(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
	}
	
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
