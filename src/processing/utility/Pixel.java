package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Pixel
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */


public class Pixel implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = -3052611990076537074L;

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
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return 31 * position.hashCode() + intensity.hashCode();
	}
}
