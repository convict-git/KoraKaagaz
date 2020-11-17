package processing.tests;

import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import processing.*;
import processing.shape.*;
import processing.testsimulator.*;
import processing.utility.*;

public class DrawRectangleTest extends TestCase {

	@Override
	public boolean run() {
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in EraseTest interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position topLeft = new Position(1,2);
		Position bottomRight = new Position(4,6);
		Intensity intensity = new Intensity(1,2,3);
		Pixel topLeftPixel = new Pixel(topLeft, intensity);
		Pixel bottomRightPixel = new Pixel(bottomRight, intensity);
		
		ArrayList<Pixel> arrayPixels = RectangleDrawer.drawRectangle(topLeft,
				bottomRight,
				intensity);
		
		// Perform post processing on the pixels
		arrayPixels = ShapeHelper.postDrawProcessing(
				arrayPixels,
				ClientBoardState.brushSize,
				ClientBoardState.boardDimension
		);
		
		TestUtil.initialiseProcessorForTest();
		
		IDrawShapes processor = ProcessingFactory.getProcessor();
		
		IUser user = ProcessingFactory.getProcessor();
		
		user.subscribeForChanges("ProcessorTest", new ChangesHandler());
		
		ChangesHandler.receivedOutput = null;
		
		try {
			processor.drawRectangle(topLeftPixel, bottomRightPixel);	
		} catch (Exception error) {
			this.setError(error.toString());
			ChangesHandler.receivedOutput = null;
			System.out.println(error);
			return false;
		}
		
		while (ChangesHandler.receivedOutput == null) {
			try{
				Thread.currentThread().sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		
		if (ChangesHandler.receivedOutput.containsAll(arrayPixels)) {
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
