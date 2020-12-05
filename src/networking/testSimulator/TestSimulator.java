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
	
	/** This progtam expects arguments , if no arguments provided , communication will be set to LAN */
	public static void main(String args[]) {
		
		TestCase test = null;
		String communication  = null;
		
		if(args.length==0 || args[0].equals("LAN")) {
			test = new LanTest();
			communication = "LAN";
		}
	
		else if(args[0].equals("INTERNET")) {
			test = new InternetTest();
			communication = "INTERNET";
		}
		else{
			System.out.println("Invalid Arguments");
			return;
		}
		
		if(test.run()) {
			System.out.println("TestCase passed for "+communication);
		}
		else {
			System.out.println("TestCase Failed for" + communication);
		}
	}
}
