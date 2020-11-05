package processing.tests;

import java.util.ArrayList;

import infrastructure.validation.testing.TestCase;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.*;

/**
 *
 * @author Sakshi Rathore
 */

public class CurveBuilderTest extends TestCase{
	
	@Override
	public boolean run() {
		
		this.setDescription("Test the drawCurve interface.");
		this.setCategory("Processing");
		this.setPriority(2);
		
		Processor processor = ProcessingFactory.getProcessor();
		
		Position pos = new Position(1,2);
		Intensity intensity = new Intensity(1,2,3);
		Pixel pixel  = new Pixel(pos, intensity);
		ArrayList<Pixel> arrayPixel = new ArrayList<Pixel>();
		arrayPixel.add(pixel);
		
		try {
			processor.drawCurve(arrayPixel);	
		} catch (Exception error) {
			this.setError(error.toString());
		}
		
		
		return true;
	}
}
