package networking.testSimulator;

import networking.tests.InternetTest;
import networking.tests.LanTest;

public class TestSimulator {
	public static void main(String args[]) {
		LanTest test1 = new LanTest();
		if(test1.run()) {
			System.out.println("Lan Passed");
		}
		else {
			System.out.println("Lan Failed");
		}
		
		/*InternetTest test2 = new InternetTest();
		if(test2.run()) {
			System.out.println("Internet Passed");
		}
		else {
			System.out.println("Internet Failed");
		}*/
	}
}
