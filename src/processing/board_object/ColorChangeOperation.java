package processing.board_object;

import processing.utility.Intensity;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// The Color Change Operation
// It is parameterized by the Intensity (Color)
public class ColorChangeOperation implements BoardObjectOperation {
	private Intensity intensity; // The Color Parameter
	
	// The Constructor for Color Change Operation
	public ColorChangeOperation(Intensity intensity) {
		this.intensity = intensity;
	}
	
	// Return the type of operation
	public BoardObjectOperationType getOperationType () {
		return BoardObjectOperationType.COLOR_CHANGE;
	}
	
	// Get the Intensity (Color) Parameter
	public Intensity getIntensity() {
		return intensity;
	}
}
