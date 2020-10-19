package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Angle
public class Angle {
	public double angle;
	
	@Override
	public boolean equals(Object otherAngle) {
		if(otherAngle instanceof Angle)
			return angle == ((Angle)otherAngle).angle;
		else
			return false;
	}
}
