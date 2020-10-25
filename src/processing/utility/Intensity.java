package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to RGB values
public class Intensity {
	public int r, g, b; // r: red, g: green, b: blue
	
	// Constructor for Intensity Class
	public Intensity(int red, int green, int blue) {
		r = red;
		g = green;
		b = blue;
	}
	
	// Copy constructor for Intensity Class
	public Intensity(Intensity intensity) {
		r = intensity.r;
		g = intensity.g;
		b = intensity.b;
	}
	
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
