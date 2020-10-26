package processing.utility;

/**
 * Class Representing a Pixel
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */


public class Pixel {
	/** Position of this pixel on the Board */
	public Position position;

	/** Intensity of this pixel */
	public Intensity intensity;
	
	/**
	 * Pixel Constructor
	 * 
	 * @param position Position of pixel
	 * @param intensity intensity of this pixel
	 */
	public Pixel(Position position, Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}
	
	/** Copy Constructor */
	public Pixel(Pixel pixelObject) {
		position = new Position(pixelObject.position);
		intensity = new Intensity(pixelObject.intensity);
	}
	
	/** Equals Method */
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
