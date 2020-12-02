package UI;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import processing.*;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;

/**
 * @author: Sajith Kumar Erasani
 * This class implements all the methods in IChanges interface
 */


public class PixelListener implements IChanges {
	/**
	 * This method will update the canvas with given pixels
	 * @param pixels ArrayList of pixels
	 */
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		CanvasController controller = new CanvasController();
		controller.updateChanges(pixels);
	}

	/**
	 * This method will select an object by changing the color of given pixels
	 * @param pixels ArrayList of pixels
	 */
	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		CanvasController controller = new CanvasController();
		controller.updateSelectedPixels(pixels);
	}
}
