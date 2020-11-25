package processing.boardobject;

import java.io.Serializable;
import processing.utility.Intensity;

/**
 * The Color Change Operation
 * 
 * It is parameterized by the Intensity (Color)
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class ColorChangeOperation implements IBoardObjectOperation, Serializable  {
	/** UID of this serializable class */
	private static final long serialVersionUID = -1972682873989750630L;

	/** The Intensity (Color) Parameter  */
	private Intensity intensity; // 
	
	/** Constructor for the Color Change Operation */
	public ColorChangeOperation(Intensity intensity) {
		this.intensity = intensity;
	}
	
	/** Returns the type of operation */
	public BoardObjectOperationType getOperationType () {
		return BoardObjectOperationType.COLOR_CHANGE;
	}
	
	/** Gets the Intensity (Color) Parameter */
	public Intensity getIntensity() {
		return intensity;
	}
}
