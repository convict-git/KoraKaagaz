package processing.utility;

import java.io.Serializable;

/**
 * Class Representing an Angle
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Angle implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = 1831016067017799061L;
	
	/** Angle as a double value */
	public double angle;
	
	/**
	 * Angle Constructor
	 * 
	 * @param angle Angle as a double value
	 */
	public Angle(double angle) {
		this.angle = angle;
	}
	
	/** Copy Constructor */
	public Angle(Angle angleObject) {
		angle = angleObject.angle;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object otherAngle) {
		if(otherAngle instanceof Angle)
			return angle == ((Angle)otherAngle).angle;
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return (int)angle;
	}
}
