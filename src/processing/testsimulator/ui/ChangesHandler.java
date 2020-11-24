package processing.testsimulator.ui;

import java.util.ArrayList;

import infrastructure.validation.logger.*;
import processing.*;
import processing.utility.*;

/**
 * Implement handlers used by UI. Changes received from the 
 * processing module is stored in a variable that can accessed
 * while testing to validate the result.
 * 
 * @author Sakshi Rathore
 *
 */

public class ChangesHandler implements IChanges{

	/* Get logger instance */
	ILogger logger = LoggerFactory.getLoggerInstance();
	
	/* Stores the received output by processor module */
	public static ArrayList<Pixel> receivedOutput = null; 
	
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		
		logger.log(
				ModuleID.PROCESSING, 
				LogLevel.SUCCESS, 
				"Test: UI Successfully received the changes passed by processor."
		);
		/* set the receivedOutput variable */ 
		receivedOutput = pixels;
		return;
	}

	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		
		logger.log(ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"Test: UI received the selectedPixels passed by processor.");
		/* set the receivedOutput variable */ 
		receivedOutput = pixels;
		return;
	}

}
