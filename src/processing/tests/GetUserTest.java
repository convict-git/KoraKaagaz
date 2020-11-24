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
 * Test for getUser API in IUser interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class GetUserTest extends TestCase {
	
	/**
	 * Create an objectA and call getUser API 
	 * 
	 * Expected output is ArrayList<Pixel> of objectA pixels.
	 * 
	 * @return true if the getUser operation works successfully.
	 */
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the getUser API in IUser interface.");
		setCategory("Processing");
		setPriority(1);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"GetUserTest: Create input for test."
		);

		int b;
		Pixel pixelA;
		Position posA;
		
		/* stores the position to be passed to API for getUser */
		ArrayList<Position> inputPositions = new ArrayList<Position>();
		
		/* stores the output from getUser user name */
		String userName;
		
		/* stores the ArrayList of objectA */
		ArrayList<Pixel> objectA = new ArrayList<Pixel>();
		/* intensity for object A */
		Intensity intensityA = new Intensity(10, 12, 14);
		
		for (int a=2; a<7; a++)
		{
			b = 2;
			posA = new Position(a, b);
			pixelA  = new Pixel(posA, intensityA);
			objectA.add(pixelA);
		}
		
		inputPositions.add(new Position(2,2));
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"GetUserTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast",
				new ClientObjectHandler()
		);
		
		/* get an instance of IDrawErase interface */
		IDrawErase draw = ProcessingFactory.getProcessor();
		
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
		
		/* Call undo API of processing module */
		try {
			userName = user.getUser(inputPositions);
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		}
		
		/* check whether the output received is same as expected output */
		if (userName.equals("Tester")) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"GetUserTest: Successful!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("GetUser failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"GetUserTest: FAILED."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}