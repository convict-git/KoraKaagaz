package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the radius
public class Radius {
	public double radius;
	
	public boolean equals(Object obj) {
		if(obj instanceof Radius)
			return radius == ((Radius)obj).radius;
		else
			return false;
	}
}
