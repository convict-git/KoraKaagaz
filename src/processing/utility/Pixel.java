package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to position
// and intensity at that position
public class Pixel {
	public Position position;
	public Intensity intensity;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pixel) {
			Pixel pixel = (Pixel)obj;
			return
				position.equals(pixel.position)
				&&
				intensity.equals(pixel.intensity);
		}
		else
			return false;
	}
}
