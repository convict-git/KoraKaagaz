package processing.test;

import java.util.ArrayList;

import infrastructure.validation.logger.*;
import processing.IChanges;
import processing.utility.*;


public class ChangesHandler implements IChanges{

	ILogger logger = LoggerFactory.getLoggerInstance();
	
	public static ArrayList<Pixel> receivedOutput = null; 
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		
		receivedOutput = pixels;
		return;
	}

	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		return;
	}

}
