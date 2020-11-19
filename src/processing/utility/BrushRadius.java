package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Brush Radius
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar, Himanshu Jain
 */

public class BrushRadius implements Serializable   {

	/** Serial UID */
	private static final long serialVersionUID = 3798847967650288408L;
	
	/** The Brush Radius */
	public double brushRadius; 
	
	/**
 	 * BrushRadius Constructor
 	 * 
 	 * @param brushRadius Brush Radius as an double value
 	 */
 	public BrushRadius(double brushRadius) {
 		this.brushRadius = brushRadius;
 	}

 	/** Copy Constructor */
 	public BrushRadius(BrushRadius brushRadObj) {
 		brushRadius = brushRadObj.brushRadius;
 	}
	
	/* The equals method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BrushRadius)
			return brushRadius == ((BrushRadius)obj).brushRadius;
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return (int)brushRadius;
	}
}
