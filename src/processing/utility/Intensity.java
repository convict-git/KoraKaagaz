package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to RGB values
public class Intensity {
	public int r, g, b; // r: red, g: green, b: blue
	
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
