package processing.tests;

import processing.ProcessingFactory;
import processing.Processor;

/**
*
* @author Sakshi Rathore
*/

public class ProcessorFactoryObject {

	private static Processor processor;
	/**
	 * 
	 * @return Processor creates and returns object of singleton class Processor
	 */
	public static Processor getProcessor() {
		if (processor == null)
			processor = ProcessingFactory.getProcessor();
		return processor;
	}
}
