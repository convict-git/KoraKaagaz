package processing.board_object;

import processing.utility.Angle;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// The Rotate Operation
// It is parameterized by the Angle
public class RotateOperation implements BoardObjectOperation {
	private Angle angleCCW; // The Angle Parameter
	
	// The Constructor for Rotate Operation
	public RotateOperation(Angle angleCCW) {
		this.angleCCW = angleCCW;
	}
	
	// Return the type of operation
	public BoardObjectOperationType getOperationType () {
		return BoardObjectOperationType.ROTATE;
	}
	
	// Get the Angle Parameter
	public Angle getAngle() {
		return angleCCW;
	}
}
