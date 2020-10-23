package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the radius
public class Radius {
	public double radius;
	
	// Constructor for Radius Class
	public Radius(double radius) {
		this.radius = radius;
	}
	
	// Copy Constructor for Radius Class
	public Radius(Radius radiusObject) {
		radius = radiusObject.radius;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Radius)
			return radius == ((Radius)obj).radius;
		else
			return false;
	}
}
