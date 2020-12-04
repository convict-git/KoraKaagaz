package networking.testSimulator;

import networking.tests.InternetTest;
import networking.tests.LanTest;


/**
 * This class is utility class for testing the test cases written
 * @author sravan
 *
 */
public class TestSimulator {
	public static void main(String args[]) {
		
		//Run the test for land
		/*LanTest test1 = new LanTest();
		if(test1.run()) {
			System.out.println("Lan Passed");
		}
		else {
			System.out.println("Lan Failed");
		}*/
		//Run the test for internet
		InternetTest test2 = new InternetTest();
		if(test2.run()) {
			System.out.println("Internet Passed");
		}
		else {
			System.out.println("Internet Failed");
		}
	}
}
