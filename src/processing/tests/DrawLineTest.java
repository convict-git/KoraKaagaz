package processing.tests;

import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import processing.ClientBoardState;
import processing.IDrawShapes;
import processing.IUser;
import processing.ProcessingFactory;
import processing.shape.LineDrawer;
import processing.shape.ShapeHelper;
import processing.testsimulator.ChangesHandler;
import processing.testsimulator.TestUtil;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;

public class DrawLineTest extends TestCase {

	@Override
	public boolean run() {
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in EraseTest interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position startPos = new Position(1,2);
		Position endPos = new Position(4,6);
		Intensity intensity = new Intensity(1,2,3);
		Pixel start = new Pixel(startPos, intensity);
		Pixel end = new Pixel(endPos, intensity);
		
		ArrayList<Pixel> arrayPixels = LineDrawer.drawSegment(start.position,
				end.position, start.intensity);
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
			processor.drawLine(start, end);	
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
