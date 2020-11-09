package processing.test;

import processing.ProcessingFactory;
import processing.Processor;

public class ClientUI {

	private static Processor processor = null;
	
	private static String testIdentifier = null;
	
	public static Processor getProcessor() {
		if (processor == null)
			processor = ProcessingFactory.getProcessor();
		return processor;
	}
	
	public static void setTestIdentifier(String identifier) {
		testIdentifier = identifier;
	}
	
	public static String getTestIdentifier() {
		return testIdentifier;
	}
	
}
