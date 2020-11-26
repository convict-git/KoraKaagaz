package processing.boardobject;

import java.io.Serializable;

import processing.utility.Angle;

/**
 * The Rotate Operation
 * 
 * It is parameterized by the Angle of rotation
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class RotateOperation implements IBoardObjectOperation, Serializable {
	/** UID of this serializable class */
	private static final long serialVersionUID = 1083016147969650796L;

	/** The Angle Parameter */
	private Angle angleCCW;
	
	/** Constructor for the Rotate Operation */
	public RotateOperation(Angle angleCCW) {
		this.angleCCW = angleCCW;
	}
	
	/** Returns the type of operation */
	public BoardObjectOperationType getOperationType () {
		return BoardObjectOperationType.ROTATE;
	}
	
	/** Gets the Angle Parameter */
	public Angle getAngle() {
		return angleCCW;
	}
}
