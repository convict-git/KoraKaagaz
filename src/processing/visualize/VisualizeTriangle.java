package processing.visualize;

/**
 * Visualize Triangle Drawing Algorithm Results
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

import java.util.ArrayList;

import processing.shape.TriangleDrawer;
import processing.shape.ShapeHelper;
import processing.utility.*;

public class VisualizeTriangle {
	public static void main(String[] args) {
		ArrayList<Pixel> pixels = null;
		Dimension dimension = new Dimension(1000, 1000);
		
		pixels = TriangleDrawer.drawTriangle(
			new Position(100, 300),
			new Position(500, 100),
			new Position(500, 700),
			new Intensity(0, 0, 0)
		);
		
		pixels = ShapeHelper.postDrawProcessing(
			pixels,
			new BrushRadius(1),
			dimension
		);
		
		Visualize.visualize(pixels, dimension);
	}
}
