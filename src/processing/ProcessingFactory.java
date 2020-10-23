package processing;

/**
* UI module will first create the object of this factory class and then will call the function getProcessor()
* which will return them an object of type Processor.
*
* @author Himanshu Jain
*/

public class ProcessingFactory {
	
	public static Processor processor = new Processor();
	
	private ProcessingFactory() {};
	
	public static Processor getProcessor() {
		return processor;
	}
	
}
