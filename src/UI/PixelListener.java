package UI;

import java.util.ArrayList;
import processing.*;
import processing.utility.Pixel;


/**
 * @author: Sajith Kumar Erasani
 * This class implements all the methods in IChanges interface
 */


public class PixelListener implements IChanges {
	/**
	 * This method will call updateChanges method to update the canvas with given pixels
	 * @param pixels ArrayList of pixels
	 */
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		if (pixels != null) {
			CanvasController controller = new CanvasController();
			controller.updateChanges(pixels);
		}
	}


	/**
	 * This method will call updateSelectedPixels method to select an object by changing the color of given pixels
	 * @param pixels ArrayList of pixels
	 */
	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		if (pixels != null) {
			CanvasController controller = new CanvasController();
			controller.updateSelectedPixels(pixels);
		}
	}
}
