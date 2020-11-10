package processing.utility;

/**
 * Class Representing Pixel Intensity
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Intensity {
	/**
	 * r Red intensity
	 * g Green intensity
	 * b Blue blue intensity
	 */
	public int r, g, b;
	
	/**
	 * Intensity Constructor
	 * 
	 * @param red Red intensity
	 * @param green Green intensity
	 * @param blue Blue intensity
	 */
	public Intensity(int red, int green, int blue) {
		r = red;
		g = green;
		b = blue;
	}
	
	/** Copy Constructor */
	public Intensity(Intensity intensity) {
		r = intensity.r;
		g = intensity.g;
		b = intensity.b;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Intensity) {
			Intensity intensity = (Intensity)obj;
			return
				r == intensity.r
				&&
				g == intensity.g
				&&
				b == intensity.b;
		}
		else
			return false;
	}
}
