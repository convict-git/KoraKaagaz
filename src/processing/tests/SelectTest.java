package processing.tests;

import java.util.*;

import processing.*;
import processing.utility.*;
import processing.testsimulator.*;
import processing.testsimulator.ui.*;
import infrastructure.validation.logger.*;
import infrastructure.validation.testing.TestCase;

/**
 * Test for select API in IOperation interface. 
 * 
 * @author Sakshi Rathore
 *
 */

public class SelectTest extends TestCase {
	
	/**
	 * Create two objects, objectA and objectB on Board using drawCurve API 
	 * and position of objectA is passed for selecting objectA. Excepted 
	 * output is ArrayList<Pixel> corresponding to objectA
	 * 
	 */
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the drawCurve API in IOperation interface.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		/* Create input (ArrayList<Pixel>) and expected output */
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "SelectTest: Create input for test.");
		
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
		
		/* add positions of objectA to select */
		selectObjectPosition.add(new Position(40, 50));
		
		/* Initialize the variables in Processor Module */
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "SelectTest: Initialise processor for test.");
		TestUtil.initialiseProcessorForTest();
		
		/* get an instance of IDrawErase interface */
		IDrawErase draw = ProcessingFactory.getProcessor();
		
		/* get an instance of IOperation interface */
		IOperation select = ProcessingFactory.getProcessor();
		
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
		
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "SelectTest: Waiting for UI to receive output.");
		
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
		
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "SelectTest: Waiting for UI to receive output.");
		
		/* wait till UI receives the output */
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		ChangesHandler.receivedOutput = null;

		ArrayList<Pixel> selectedObject = null;
		try {
			/* call processing select API to select object at passed input array */
			selectedObject = select.select(selectObjectPosition);
			
		} catch (Exception error) {
			/* return and set error in case of unsuccessful processing */
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			return false;
		
		}
		
		Set<Pixel> inputSet = new HashSet<Pixel>();
		inputSet.addAll(objectA);
		Set<Pixel> outputSet = new HashSet<Pixel>();
		outputSet.addAll(selectedObject);
		
		for (int i = 0; i < objectA.size(); i++)
		{
			Pixel p = objectA.get(i);
			System.out.println(p.position.r + " " + p.position.c);
		}
		System.out.println("enter");
		for (int i = 0; i < selectedObject.size(); i++)
		{
			Pixel p = selectedObject.get(i);
			System.out.println(p.position.r + " " + p.position.c);
		}
		/* check whether the output received is same as expected output */
		if (inputSet.equals(outputSet)) {
			logger.log(ModuleID.PROCESSING, LogLevel.INFO, "SelectTest: Successful!.");
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Select Output failed. Output is different from the input.");
			logger.log(ModuleID.PROCESSING, LogLevel.WARNING, "SelectTest: FAILED.");
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
}
