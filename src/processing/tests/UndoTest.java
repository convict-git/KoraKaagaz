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
 * Test for undo API in IUndoRedo interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class UndoTest extends TestCase {
	
	/**
	 * Create two objects, objectA and objectB on Board using drawCurve API 
	 * and undo API of processing module is called twice. The objects are
	 * line intersecting at 90 degree.
	 * 
	 * Expected output first time is objectB will intensity(255,255,255) at 
	 * positions other than intersecting point and next undo result should be 
	 * objectA positions with intensity(255,255,255)
	 * 
	 * @return true if the undo operation works successfully.
	 */
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the undo API in IUndoRedo interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"UndoTest: Create input for test."
		);
		
		int b;
		Pixel pixelA, pixelB;
		Position pos;
		
		/* stores the ArrayList of objectA */
		ArrayList<Pixel> objectA = new ArrayList<Pixel>();
		/* intensity for object A */
		Intensity intensityA = new Intensity(10, 12, 14);
		
		/* stores the ArrayList of objectB on Board */
		ArrayList<Pixel> objectB = new ArrayList<Pixel>();
		/* intensity for object B */
		Intensity intensityB = new Intensity(10, 12, 15);
		
		/* expected output */
		ArrayList<Pixel> expectedOutputA = new ArrayList<Pixel>();		
		ArrayList<Pixel> expectedOutputB = new ArrayList<Pixel>();		
		Intensity white = new Intensity(255, 255, 255);
		
		int j =2;
		for (int a=2; a<7; a++)
		{
			b = 2;
			pos = new Position(a, b);
			pixelA  = new Pixel(pos, intensityA);
			objectA.add(pixelA);
			expectedOutputA.add(new Pixel(pos, white));
			
			pos = new Position(a+j, b+j);
			pixelB = new Pixel(pos, intensityB);
			objectB.add(pixelB);
			
			if (pixelA.position.equals(pixelB.position)) {
				expectedOutputB.add(new Pixel(pos, intensityA));
			} else { 
				expectedOutputB.add(new Pixel(pos, white));
			}
			j = j - 1;
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"UndoTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		
		ClientBoardState.communicator.subscribeForNotifications(
				"ObjectBroadcast",
				new ClientObjectHandler()
		);
		
		/* get an instance of IDrawErase interface */
		IDrawErase draw = ProcessingFactory.getProcessor();
		
		/* get an instance of IOperation interface */
		IUndoRedo undo = ProcessingFactory.getProcessor();
		
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
		
		/* Call undo API of processing module */
		try {
			undo.undo();
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
		inputSet.addAll(expectedOutputB);
		Set<Pixel> outputSet = new HashSet<Pixel>();
		outputSet.addAll(ChangesHandler.receivedOutput);
		
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.INFO, 
					"UndoTest: First undo successful!."
			);
			
			ChangesHandler.receivedOutput = null;
		} else {
			setError("Select Output failed. Output is different from the input.");
			logger.log(ModuleID.PROCESSING, LogLevel.WARNING, "UndoTest: FAILED.");
			ChangesHandler.receivedOutput = null;
			return false;
		}
		
		/* Call undo API of processing module */
		try {
			undo.undo();
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
		
		inputSet = new HashSet<Pixel>();
		inputSet.addAll(expectedOutputA);
		outputSet = new HashSet<Pixel>();
		outputSet.addAll(ChangesHandler.receivedOutput);
		
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"UndoTest: Successful!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Undo failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.WARNING,
					"UndoTest: FAILED."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}