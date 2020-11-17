package processing.tests;

import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import processing.ClientBoardState;
import processing.IDrawShapes;
import processing.IUser;
import processing.ProcessingFactory;
import processing.shape.LineDrawer;
import processing.shape.ShapeHelper;
import processing.shape.TriangleDrawer;
import processing.testsimulator.ChangesHandler;
import processing.testsimulator.TestUtil;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;

public class DrawTriangleTest extends TestCase {

	@Override
	public boolean run() {
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in EraseTest interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position posA = new Position(1,2);
		Position posB = new Position(4,6);
		Position posC = new Position(9,8);
		Intensity intensity = new Intensity(1,2,3);
		Pixel vertA = new Pixel(posA, intensity);
		Pixel vertB = new Pixel(posB, intensity);
		Pixel vertC = new Pixel(posC, intensity);
		
		ArrayList<Pixel> arrayPixels = TriangleDrawer.drawTriangle(vertA.position,
				vertB.position, vertC.position, vertA.intensity);
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
			processor.drawTriangle(vertA, vertB, vertC);	
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
		for (int i = 0; i < arrayPixels.size(); i++)
		{
			Pixel p = arrayPixels.get(i);
			System.out.println(p.position.r + " " + p.position.c);
		}
		System.out.println("enter");
		for (int i = 0; i < ChangesHandler.receivedOutput.size(); i++)
		{
			Pixel p = ChangesHandler.receivedOutput.get(i);
			System.out.println(p.position.r + " " + p.position.c);
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
