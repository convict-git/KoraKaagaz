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
	
	/**
	 * processor stores the object of the Processor class where all the functions
	 * defined on all the interfaces for the UI is defined so we need to pass
	 * this object to the UI. Same object should be transferred to the UI whenever they
	 * ask for this object so this is made static. 
	 */
	public static Processor processor = new Processor();
	
	/**
	 * Constructor of this class is made private so that UI won't be able to create 
	 * the object of this class, if they do so they will be able to get new Processor
	 * object everytime.
	 */
	private ProcessingFactory() {};
	
	/**
	 * getProcessor function will be called by the UI module to get the object of the processor
	 * class to access the api's provided by the processing module to the UI.
	 * 
	 * @return Processor class object
	 */
	public static Processor getProcessor() {
		return processor;
	}
	
}
