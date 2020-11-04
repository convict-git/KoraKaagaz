package processing;

/**
 * This Factory class provides an object of the Processor class, where all the functions for
 * Interfaces are implemented.
 * It is following singleton pattern so that only one object of the class will be created.
 *
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class ProcessingFactory {
	
	public static Processor processor = new Processor();
	
	private ProcessingFactory() {};
	
	public static Processor getProcessor() {
		return processor;
	}
	
}
