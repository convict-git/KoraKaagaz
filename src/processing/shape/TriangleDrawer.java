package processing.shape;

import java.util.ArrayList;
import processing.utility.*;

/**
 * Methods for drawing Triangles
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class TriangleDrawer {
	/**
	 * Draws a triangle given the three vertices of the triangles
	 * 
	 * @param vertA First vertex of the triangle
	 * @param vertB Second vertex of the triangle
	 * @param vertC Third vertex of the triangle
	 * @param intensity Intensity of each point on the triangle
	 * @return arraylist of pixel values of the border points of the
	 * triangle
	 */
	public static ArrayList<Pixel> drawTriangle (
		Position vertA,
        Position vertB,
        Position vertC,
        Intensity intensity
	) {
		// Initialize pixel values
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Draw line between A and B
		pixels.addAll(LineDrawer.drawSegment(vertA, vertB, intensity));
		
		// Draw line between B and C
		pixels.addAll(LineDrawer.drawSegment(vertB, vertC, intensity));
		
		// Draw line between C and A
		pixels.addAll(LineDrawer.drawSegment(vertC, vertA, intensity));
		
		// Return the constructed pixels
		return pixels;
	}
}
