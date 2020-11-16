package processing.testsimulator;

import processing.utility.*;
import java.util.*;

import processing.ProcessingFactory;
import processing.Processor;

public class ClientUI {

	private static Processor processor = null;
	
	private static ArrayList<Pixel> output = null;
	
	public static Processor getProcessor() {
		if (processor == null)
			processor = ProcessingFactory.getProcessor();
		return processor;
	}	
}
