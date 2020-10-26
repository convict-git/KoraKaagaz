package processing.utility;

/**
 * Class Representing an Angle
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Angle {
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
}
