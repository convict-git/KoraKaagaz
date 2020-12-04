package processing.visualize;

/**
 * Visualize Triangle Drawing Algorithm Results
 * 
 * @author Rakesh Kumar
 * @reviewer Ahmed Zaheer Dadarkar
 */

import java.util.ArrayList;

import processing.shape.TriangleDrawer;
import processing.shape.ShapeHelper;
import processing.utility.*;

public class VisualizeTriangleFill {
	public static void main(String[] args) {
		ArrayList<Pixel> pixels = null;
		Dimension dimension = new Dimension(1000, 1000);
		
		pixels = TriangleDrawer.drawTriangleFill(
			new Position(100, 300),
			new Position(500, 100),
			new Position(500, 700),
			new Intensity(0, 0, 0)
		);
		
		pixels = ShapeHelper.postFillProcessing(
			pixels,
			dimension
		);
		
		Visualize.visualize(pixels, dimension);
	}
}
