package processing.tests;

import java.util.ArrayList;
import infrastructure.validation.testing.TestCase;
import processing.*;
import processing.testsimulator.*;
import processing.utility.*;

public class DrawCurveTest extends TestCase {
	
	public boolean run() {
		
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in IDrawCurve interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position pos = new Position(1,2);
		Intensity intensity = new Intensity(1,2,3);
		Pixel pixel  = new Pixel(pos, intensity);
		ArrayList<Pixel> arrayPixel = new ArrayList<Pixel>();
		arrayPixel.add(pixel);
		
		TestUtil.initialiseProcessorForTest();
		
		IDrawErase processor = ProcessingFactory.getProcessor();
		
		IUser user = ProcessingFactory.getProcessor();
		
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		try {
			processor.drawCurve(arrayPixel);	
		} catch (Exception error) {
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			System.out.println(error);
			return false;
		}
		
		while (ChangesHandler.receivedOutput == null) {
			// wait till output received
		}
		
		if (ChangesHandler.receivedOutput.equals(arrayPixel)) {
			ChangesHandler.receivedOutput = null;
			return true;
		} else {
			setError("Draw Curve Output failed. Output is different from the input.");
			ChangesHandler.receivedOutput = null;
			System.out.println("here");
			return false;
		}
	}
}
