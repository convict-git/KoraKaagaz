package processing.visualize;

/**
 * Visualize Rectangle Drawing Algorithm Result
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

import java.util.ArrayList;

import processing.shape.RectangleDrawer;
import processing.shape.ShapeHelper;
import processing.utility.*;

public class VisualizeRectangle {
	public static void main(String[] args) {
		ArrayList<Pixel> pixels = null;
		Dimension dimension = new Dimension(1000, 1000);
		
		pixels = RectangleDrawer.drawRectangle(
			new Position(100, 100),
			new Position(700, 700),
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
