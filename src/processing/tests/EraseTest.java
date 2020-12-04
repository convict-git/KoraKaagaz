package processing.tests;

import java.util.*;

import infrastructure.validation.logger.*;
import infrastructure.validation.testing.TestCase;
import processing.*;
import processing.testsimulator.*;
import processing.testsimulator.handlers.ServerObjectHandler;
import processing.testsimulator.ui.ChangesHandler;
import processing.utility.*;

/**
 * Test for erase API in IDrawCurve interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class EraseTest extends TestCase {

	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the erase API  in IDrawCurve interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"EraseTest: Create input for test."
		);
		
		/* stores the pixels for erase object*/
		ArrayList<Pixel> arrayPixels = new ArrayList<Pixel>();
		
		/* stores the position for erase object*/
		ArrayList<Position> arrayPositions = new ArrayList<Position>();
		
		int b;
		Intensity intensity = new Intensity(255, 255, 255);
		for (int a=40; a<60; a++)
		{
			b = a + 10;
			Position pos = new Position(a, b);
			Pixel pixel  = new Pixel(pos, intensity);
			arrayPixels.add(pixel);
			arrayPositions.add(pos);
		}

		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"EraseTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ServerObjectHandler());
		
		/* get an instance of IDrawErase interface */
		IDrawErase processor = ProcessingFactory.getProcessor();
		
		/* Get an instance of IUser interface */
		IUser user = ProcessingFactory.getProcessor();
		
		/* Subscribe for receiving changes from processor */
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			/* pass the input array for erase */
			processor.erase(arrayPositions);	
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"EraseTest: Waiting for UI to receive output."
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
					"EraseTest: Successfull!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Erase failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"EraseTest: FAILED!."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
