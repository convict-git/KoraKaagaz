package processing.tests;

import processing.test.*;
import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.*;

import processing.test.*;
import processing.test.*;

public class DrawCurveTest extends TestCase {
	
	public boolean run() {
		
		setDescription("Test the drawCurve interface.");
		setCategory("Processing");
		setPriority(2);
		
		ClientUI.setTestIdentifier("DrawCurveTest");
		TestUtil.initialiseProcessorForTest();
		
		Processor processor = ProcessingFactory.getProcessor();
		
		processor.subscribeForChanges("TestProcessor", new ChangesHandler());
		
		Position pos = new Position(1,2);
		Intensity intensity = new Intensity(1,2,3);
		Pixel pixel  = new Pixel(pos, intensity);
		ArrayList<Pixel> arrayPixel = new ArrayList<Pixel>();
		arrayPixel.add(pixel);
		
		try {
			processor.drawCurve(arrayPixel);	
		} catch (Exception error) {
			this.setError(error.toString());
			return false;
		}
		
		
		
		return true;
	}
}
