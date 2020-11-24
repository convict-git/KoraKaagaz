package processing.tests;

import java.util.*;

import processing.*;
import processing.utility.*;
import processing.testsimulator.*;
import processing.testsimulator.handlers.*;
import processing.testsimulator.ui.*;
import infrastructure.validation.logger.*;
import infrastructure.validation.testing.TestCase;

/**
 * Test for drawCurve API in IDrawCurve interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class DrawCurveTest extends TestCase {
	
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test drawCurve function in IDrawCurve interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCurveTest: Create input for test."
		);
		
		ArrayList<Pixel> arrayPixels = new ArrayList<Pixel>();
		
		int b;
		Position pos;
		Pixel pixel;
		Intensity intensity = new Intensity(10, 12, 14);
		
		for (int a=40; a<60; a++)
		{
			b = a + 10;
			pos = new Position(a, b);
			pixel  = new Pixel(pos, intensity);
			arrayPixels.add(pixel);
		}

		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCurveTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ServerObjectHandler());
		
		/* get an instance of IDrawErase interface */
		IDrawErase processor = ProcessingFactory.getProcessor();
		
		/* Get an instance of IUser interface */
		IUser user = ProcessingFactory.getProcessor();
		
		/* Subscribe for receiving changes from processor */
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			/* pass the input array for drawing curve to processor module */
			processor.drawCurve(arrayPixels);	
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"DrawCurveTest: Waiting for UI to receive output."
		);
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try {
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
					"DrawCurveTest: Successfull!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Draw Curve failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"DrawCurveTest: FAILED!."
					);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
