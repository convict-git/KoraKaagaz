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
 * Test for rotate API in IOperation interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class RotateTest extends TestCase {
	
	/**
	 * Create two objects, objectA and objectB on Board using drawCurve API 
	 * and position of objectA is passed for selecting objectA, as first the 
	 * object is to be selected for rotate. Then rotate API of processing 
	 * module is called. 
	 * 
	 * @return true if the rotate operation works successfully.
	 */
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the rotate API in IOperation interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"RotateTest: Create input for test."
		);
		
		int b;
		Pixel pixel;
		Position pos;
		
		/* stores the position to be passed to API for select */
		ArrayList<Position> selectObjectPosition = new ArrayList<Position>();
		
		/* stores the ArrayList of objectA */
		ArrayList<Pixel> objectA = new ArrayList<Pixel>();
		/* intensity for object A */
		Intensity intensityA = new Intensity(10, 12, 14);
		
		/* stores the ArrayList of objectB on Board */
		ArrayList<Pixel> objectB = new ArrayList<Pixel>();
		/* intensity for object B */
		Intensity intensityB = new Intensity(10, 12, 15);
		
		/* expected output*/
		ArrayList<Pixel> expectedOutput = new ArrayList<Pixel>();		
		Angle rotateBy = new Angle(90);
		
		/* rotate by 90 */
		expectedOutput.add(new Pixel(new Position(4,4), intensityA));
		expectedOutput.add(new Pixel(new Position(4,3), intensityB));
		expectedOutput.add(new Pixel(new Position(4,2), intensityA));
		expectedOutput.add(new Pixel(new Position(4,1), intensityA));
		expectedOutput.add(new Pixel(new Position(4,0), intensityA));
		
		for (int a=2; a<7; a++)
		{
			b = 2;
			pos = new Position(a, b);
			pixel  = new Pixel(pos, intensityA);
			objectA.add(pixel);

			pos = new Position(a, b+1);
			pixel = new Pixel(pos, intensityB);
			objectB.add(pixel);
		}
		
		/* add positions of objectA to select */
		selectObjectPosition.add(new Position(2,2));
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"RotateTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast",
				new ClientObjectHandler()
		);
		
		/* get an instance of IDrawErase interface */
		IDrawErase draw = ProcessingFactory.getProcessor();
		
		/* get an instance of IOperation interface */
		IOperation operation = ProcessingFactory.getProcessor();
		
		/* Get an instance of IUser interface */
		IUser user = ProcessingFactory.getProcessor();
		
		/* Subscribe for receiving changes from processor */
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			/* pass the input array for drawing objectA to processor module */
			draw.drawCurve(objectA);
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
				
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		ChangesHandler.receivedOutput = null;

		try {
			/* pass the input array for drawing ObjectB to processor module */
			draw.drawCurve(objectB);

		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
				
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		ChangesHandler.receivedOutput = null;

		try {
			/* call processing select */
			operation.select(selectObjectPosition);
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		ChangesHandler.receivedOutput = null;
		
		/* Call colorChange API of processing module */
		try {
			operation.rotate(rotateBy);
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		}
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		Set<Pixel> inputSet = new HashSet<Pixel>();
		inputSet.addAll(expectedOutput);
		Set<Pixel> outputSet = new HashSet<Pixel>();
		outputSet.addAll(ChangesHandler.receivedOutput);
		
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"RotateTest: Successful!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Rotate failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR,
					"RotateTest: FAILED."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
