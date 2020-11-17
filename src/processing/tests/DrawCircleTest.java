package processing.tests;

import java.util.*;
import infrastructure.validation.testing.TestCase;
import processing.*;
import processing.shape.CircleDrawer;
import processing.shape.ShapeHelper;
import processing.testsimulator.*;
import processing.utility.*;

public class DrawCircleTest extends TestCase {

	@Override
	public boolean run() {
		
		// use methods in TestCase to set the variables for test
		setDescription("Test the drawCurve function in EraseTest interface.");
		setCategory("Processing");
		setPriority(2);
		
		Position pos = new Position(1,2);
		Intensity intensity = new Intensity(1,2,3);
		Pixel pixel = new Pixel(pos, intensity);
		Radius radius = new Radius(2.9f);
		
		ArrayList<Pixel> arrayPixels = CircleDrawer.drawCircle(pos,
				radius,
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
			processor.drawCircle(pixel, 2.9f);	
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
