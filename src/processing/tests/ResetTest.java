package processing.tests;

import java.util.*;

import processing.*;
import processing.utility.*;
import processing.testsimulator.*;
import processing.testsimulator.handlers.ClientObjectHandler;
import processing.testsimulator.ui.*;
import infrastructure.validation.logger.*;
import infrastructure.validation.testing.TestCase;

/**
 * Test for reset API in IOperation interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class ResetTest extends TestCase {
	
	/**
	 * Create expected output for reset operation i.e.
	 * a white object of dimension same as Board
	 * 
	 * @return ArrayList<Pixel> reset object
	 */
	ArrayList<Pixel> createOutputForReset() {
		
		/* Get BoardDimensions from UI */
		Dimension dimension = ClientBoardState.boardDimension;
		
		int rows = dimension.numRows;
		
		int cols = dimension.numCols;
		
		Intensity whiteIntensity = new Intensity(255,255,255);
		
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		Position whitePosition;
		Pixel whitePixel;
		/* 
		 * Traverse through screen and assign position
		 * and white intensity to each pixel
		 */
		for(int i = 0; i < rows; i++) {
			
			for(int j = 0; j < cols; j++) {
				
				// Each position is chosen for a pixel
				whitePosition = new Position(i,j);
				
				// White intensity and position is assigned to the pixel
				whitePixel = new Pixel(whitePosition, whiteIntensity);
				
				// Add pixel to the ArrayList
				pixels.add(whitePixel);
				
			}
		}
		return pixels;
	}
	
	/**
	 * Creates two board object objectA, objectB and then call reset API of
	 * processor. Expected output is a white object of same dimension as Board.
	 * 
	 * @return true if reset works successfully
	 */
	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the reset API in IOperation interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ResetTest: Create input for test."
		);
		
		int b;
		Pixel pixel;
		Position pos;
		
		/* stores the ArrayList of objectA */
		ArrayList<Pixel> objectA = new ArrayList<Pixel>();
		/* intensity for object A */
		Intensity intensityA = new Intensity(10, 12, 14);
		
		/* stores the ArrayList of objectB on Board */
		ArrayList<Pixel> objectB = new ArrayList<Pixel>();
		/* intensity for object B */
		Intensity intensityB = new Intensity(10, 12, 14);
		
		for (int a=40; a<60; a++)
		{
			b = a + 10;
			pos = new Position(a, b);
			pixel  = new Pixel(pos, intensityA);
			objectA.add(pixel);
			
			pos = new Position(b,a);
			pixel = new Pixel(pos, intensityB);
			objectB.add(pixel);
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ResetTest: Initialise processor for test."
		);
		
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		
		/* get an instance of IDrawErase interface */
		IDrawErase draw = ProcessingFactory.getProcessor();
		
		/* get an instance of IOperation interface */
		IOperation reset = ProcessingFactory.getProcessor();
		
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
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ResetTest: Waiting for UI to receive output."
		);
		
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
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ResetTest: Waiting for UI to receive output."
		);
		
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
			/* call reset API of processing */
			reset.reset();

		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"ResetTest: Waiting for UI to receive output."
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
		ArrayList<Pixel> resetObject = createOutputForReset();
		inputSet.addAll(resetObject);
		Set<Pixel> outputSet = new HashSet<Pixel>();
		outputSet.addAll(ChangesHandler.receivedOutput);
		
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"ResetTest: Successful!."
			);
			
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Reset failed. Result does not match expected output.");
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"ResetTest: FAILED."
			);
			
			ChangesHandler.receivedOutput = null;
			return false;
		}	
	}
}