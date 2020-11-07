package processing.visualize;

/**
 * Visualize Rectangle Filling Algorithm Result
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

import java.util.ArrayList;

import processing.shape.RectangleDrawer;
import processing.shape.ShapeHelper;
import processing.utility.*;

public class VisualizeRectangleFill {
	public static void main(String[] args) {
		ArrayList<Pixel> pixels = null;
		Dimension dimension = new Dimension(1000, 1000);
		
		pixels = RectangleDrawer.drawRectangleFill(
			new Position(100, 100),
			new Position(700, 700),
			new Intensity(0, 0, 0)
		);
		
		pixels = ShapeHelper.postFillProcessing(
			pixels,
			dimension
		);
		
		Visualize.visualize(pixels, dimension);
	}
}
