package processing;

/**
* UI module will first create the object of this factory class and then will call the function getProcessor()
* which will return them an object of type Processor.
*
* @author Himanshu Jain
*/

public class ProcessingFactory {
	
	public Processor getProcessor() {
		Processor processor = new Processor();
		
		return processor;
	}
	
}
