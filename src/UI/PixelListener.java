package UI;

import java.util.ArrayList;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
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
	
	public static ILogger logger = CanvasController.logger;
	
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		if (pixels != null) {
			logger.log(
	 				ModuleID.UI,
	 				LogLevel.INFO,
	 				"getChanges called by processor"
	 			);
			model temp = new model(UpdateMode.PROCESSING_CHANGES,pixels);
			CanvasController.updateList.setAll(temp);
		}
	}


	/**
	 * This method will call updateSelectedPixels method to select an object by changing the color of given pixels
	 * @param pixels ArrayList of pixels
	 */
	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		if (pixels != null) {
			logger.log(
	 				ModuleID.UI,
	 				LogLevel.INFO,
	 				"giveSelectedPixels called by processor"
	 			);
			model temp = new model(UpdateMode.PROCESSING_SELECTED,pixels);
			CanvasController.updateList.setAll(temp);
		}
	}
}
