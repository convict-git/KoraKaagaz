package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Radius value
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Radius implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = 1523394784076853538L;
	
	/** The radius as a double value */
	public double radius;
	
	/**
	 * Radius Constructor
	 * 
	 * @param radius Radius as a double value
	 */
	public Radius(double radius) {
		this.radius = radius;
	}

	/** Copy Constructor */
	public Radius(Radius radiusObject) {
		radius = radiusObject.radius;
	}
	
	/** Equals Method */
	public boolean equals(Object obj) {
		if(obj instanceof Radius)
			return radius == ((Radius)obj).radius;
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return (int)radius;
	}
}
