package networking.testSimulator;

import infrastructure.validation.testing.TestCase;
import networking.tests.InternetTest;
import networking.tests.LanTest;


/**
 * This class is utility class for testing the test cases written
 * @author sravan
 *
 */
public class TestSimulator {
	public static void main(String args[]) {
		
		TestCase test = null;
		String communication  = null;
		if(args[0]== "INTERNET") {
			test = new InternetTest();
			communication = "INTERNET";
		}
		else if(args.length==0 || args[0]=="LAN") {
			test = new LanTest();
			communication = "LAN";
		}
		
		if(test.run()) {
			System.out.println("TestCase passed for "+communication);
		}
		else {
			System.out.println("TestCase Failed for" + communication);
		}
	}
}
