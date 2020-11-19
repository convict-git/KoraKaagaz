package processing.tests;

import java.util.*;

import infrastructure.validation.logger.*;
import infrastructure.validation.testing.*;
import processing.*;
import processing.shape.*;
import processing.testsimulator.*;
import processing.testsimulator.ui.*;
import processing.utility.*;

/**
 * Test for drawRectangle API in IDrawShapes interface. 
 *   
 * @author Sakshi Rathore
 */
public class DrawRectangleTest extends TestCase {

	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the drawRectangle function in IDrawShapes interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) */
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "DrawRectangleTest: Create input for test.");
		
		Position topLeft = new Position(1,2);
		Position bottomRight = new Position(4,6);
		Intensity intensity = new Intensity(1,2,3);
		Pixel topLeftPixel = new Pixel(topLeft, intensity);
		Pixel bottomRightPixel = new Pixel(bottomRight, intensity);
		
		ArrayList<Pixel> arrayPixels = new ArrayList<Pixel>();
		
		try {
			/* arrayPixels contains all pixels for rect given top left and bottom right */
			arrayPixels = RectangleDrawer.drawRectangle(topLeft,
					bottomRight,
					intensity);
			
			/* Perform post processing on the pixels */
			arrayPixels = ShapeHelper.postDrawProcessing(
					arrayPixels,
					ClientBoardState.brushSize,
					ClientBoardState.boardDimension
			);
		} catch (Exception error) {
			setError(error.toString());
			logger.log(ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"DrawRectangleTest: Failed to create input arrayList for given positions");
			return false;
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"DrawRectangleTest: Initialise processor for test.");
		
		TestUtil.initialiseProcessorForTest();
		
		/* get an instance of IDrawShapes interface */
		IDrawShapes processor = ProcessingFactory.getProcessor();
		
		/* Get an instance of IUser interface */
		IUser user = ProcessingFactory.getProcessor();
		
		/* Subscribe for receiving changes from processor */
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			/* 
			 * pass top left and bottom right pixel for 
			 * drawing rectangle to processor module 
			 */
			processor.drawRectangle(topLeftPixel, bottomRightPixel);	
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		logger.log(ModuleID.PROCESSING, 
				LogLevel.INFO, 
				"DrawRectangleTest: Waiting for UI to receive output.");
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.currentThread().sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		Set<Pixel> inputSet = new HashSet<Pixel>();
		inputSet.addAll(arrayPixels);
		Set<Pixel> outputSet = new HashSet<Pixel>();
		outputSet.addAll(ChangesHandler.receivedOutput);
		
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(ModuleID.PROCESSING, LogLevel.SUCCESS, "DrawRectangleTest: Successfull!.");
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Draw Curve Output failed. Output is different from the input.");
			logger.log(ModuleID.PROCESSING, LogLevel.WARNING, "DrawRectangleTest: FAILED!.");
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
