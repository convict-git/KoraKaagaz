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
 * Test for colorChange API in IOperation interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class ColorChangeTest  extends TestCase {
	
	/**
	 * Create two objects, objectA and objectB on Board using drawCurve API 
	 * and position of objectA is passed for selecting objectA, as first an 
	 * object is to be selected for delete. Then colorChange API of processing 
	 * module is called. Expected output is a ArrayList<Position> same as 
	 * objectA and new color.
	 * 
	 * @return true if the colorChange operation works successfully.
	 */
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the colorChnage API in IOperation interface.");
		setCategory("Processing");
		setPriority(1);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ColorChangeTest: Create input for test."
		);
		
		int b;
		Pixel pixel;
		Position pos;
		
		/* stores the position to be passed to API for select */
		ArrayList<Position> selectObjectPosition = new ArrayList<Position>();
		
		/* stores the ArrayList<Pixel> of objectA */
		ArrayList<Pixel> objectA = new ArrayList<Pixel>();
		/* intensity for object A */
		Intensity intensityA = new Intensity(10, 12, 14);
		
		/* stores the ArrayList<Pixel> of objectB */
		ArrayList<Pixel> objectB = new ArrayList<Pixel>();
		/* intensity for object B */
		Intensity intensityB = new Intensity(10, 12, 14);
		
		/* stores expected output*/
		ArrayList<Pixel> expectedOutput = new ArrayList<Pixel>();		
		Intensity newColor = new Intensity(0, 0, 255);
		
		for (int a=2; a<7; a++)
		{
			b = 2;
			pos = new Position(a, b);
			pixel  = new Pixel(pos, intensityA);
			objectA.add(pixel);

			expectedOutput.add(new Pixel(pos, new Intensity(newColor)));

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
				"ColorChangeTest: Initialise processor for test."
		);
		
		/* 
		 * ClientObjectHandler is the handler to be used while 
		 * subscribing network by processor 
		 */
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
				
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
			/* call processing select to select object at passed input array */
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
			operation.colorChange(newColor);
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
					"ColorChangeTest: Successful!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("ColorChange failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"ColorChangeTest: FAILED."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}

