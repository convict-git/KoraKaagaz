package processing.tests;

import java.util.*;

import infrastructure.validation.logger.*;
import infrastructure.validation.testing.*;
import processing.*;
import processing.shape.*;
import processing.testsimulator.*;
import processing.testsimulator.handlers.ServerObjectHandler;
import processing.testsimulator.ui.*;
import processing.utility.*;

/**
 * Test for drawCircle API in IDrawShapes interface. 
 *   
 * @author Sakshi Rathore
 */

public class DrawCircleTest extends TestCase {

	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test drawCircle function in IDrawShapes interface.");
		setCategory("Processing");
		setPriority(0);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCircleTest: Create input for test."
		);
		
		Position pos = new Position(1,1);
		Intensity intensity = new Intensity(1,2,3);
		Pixel pixel = new Pixel(pos, intensity);
		Radius radius = new Radius(10.0f);
		float r = 10.0f;
		
		ArrayList<Pixel> arrayPixels = new ArrayList<Pixel>();
		
		try {
			/* arrayPixels contains all pixels for given circle */
			arrayPixels = CircleDrawer.drawCircle(
					pos,
					radius,
					intensity
			);
			
			/* Perform post processing on the pixels */ 
			arrayPixels = ShapeHelper.postDrawProcessing(
					arrayPixels,
					ClientBoardState.brushSize,
					ClientBoardState.boardDimension
			);	
			
		} catch (Exception error) {
			setError(error.toString());
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"DrawCircleTest: Failed to create input arrayList."
			);
			
			return false;
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCircleTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ServerObjectHandler());
		
		/* get an instance of IDrawShapes interface */
		IDrawShapes processor = ProcessingFactory.getProcessor();
		
		/* Get an instance of IUser interface */
		IUser user = ProcessingFactory.getProcessor();
		
		/* Subscribe for receiving changes from processor */
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			/* pass the input array for drawing circle to processor module */
			processor.drawCircle(pixel, r);	
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCircleTest: Waiting for UI to receive output."
		);
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
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
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"DrawCircleTest: Successfull!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("DrawCircle failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"DrawCircleTest: FAILED!."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
