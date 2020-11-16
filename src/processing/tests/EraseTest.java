package processing.tests;

import java.util.*;
import infrastructure.validation.testing.TestCase;
import processing.*;
import processing.testsimulator.*;
import processing.utility.*;

public class EraseTest extends TestCase {

	@Override
	public boolean run() {
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in EraseTest interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position pos = new Position(1,2);
		Intensity intensity = new Intensity(255, 255, 255);
		Pixel pixel = new Pixel(pos, intensity);
		ArrayList<Pixel> arrayPixels = new ArrayList<Pixel>();
		arrayPixels.add(pixel);
		ArrayList<Position> arrayPositions = new ArrayList<Position>();
		arrayPositions.add(pos);
		
		TestUtil.initialiseProcessorForTest();
		
		IDrawErase processor = ProcessingFactory.getProcessor();
		
		IUser user = ProcessingFactory.getProcessor();
		
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		ChangesHandler.receivedOutput = null;
		try {
			processor.erase(arrayPositions);	
		} catch (Exception error) {
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			System.out.println(error);
			return false;
		}
		int count = 1;
		System.out.println("reached1");
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.currentThread().sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		System.out.println("reached");

		if (ChangesHandler.receivedOutput.equals(arrayPixels)) {
			ChangesHandler.receivedOutput = null;
			System.out.println("reached");
			return true;
		} else {
			setError("Erase Output failed. Output is different from the input.");
			ChangesHandler.receivedOutput = null;
			return false;
		}
	}
	
}
