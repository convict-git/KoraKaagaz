package processing.utility;

/**
 * Class Representing the Dimension
 *
 * @author Ahmed Zaheer Dadarkar
 */

public class Dimension {
	/*
	 * numRows Number of rows
	 * numCols Number of columns
	 */
	public int numRows, numCols;
	
	/*
	 * Dimension Constructor
	 * 
	 * @param numRows Number of rows
	 * @param numCols Number of columns
	 */
	public Dimension(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
	}
	
	/*
	 * Equals Method
	 */
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
