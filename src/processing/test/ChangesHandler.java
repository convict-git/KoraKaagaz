package processing.test;

import java.util.ArrayList;

import infrastructure.validation.logger.*;
import processing.IChanges;
import processing.utility.Pixel;

public class ChangesHandler implements IChanges{

	ILogger logger = LoggerFactory.getLoggerInstance();
	
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {
		String testIdentifier = ClientUI.getTestIdentifier();
		
		// can create enum type for test
		switch(testIdentifier) {
			case "DrawCurveTest": logger.log(ModuleID.PROCESSING, 
							    			 LogLevel.INFO, 
							    			"Check the output");
			default: logger.log(ModuleID.PROCESSING, LogLevel.INFO, "Unknown test" + testIdentifier);
		}
		return;
	}

	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {
		return;
	}

}
