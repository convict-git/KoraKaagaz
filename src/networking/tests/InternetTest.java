package networking.tests;

import infrastructure.validation.testing.TestCase;
import networking.testSimulator.InternetTestHelper;


/**
 * This Test Case is for testing the Internet communication 
 * while running this test case the server in the Internet should be running
 * @author sravan
 *
 */
public class InternetTest extends TestCase {

	@Override
	public boolean run() {
		
		InternetTestHelper internetTest = new InternetTestHelper();
		
		setDescription("Testing the internet Communication");
		setCategory("NETWORKING");
		setPriority(2);
		
		//Run the tests for different message length and different number of messages
		boolean testOne= internetTest.run(1000, 1000);
		if(testOne ==false) {
			this.setError("Error while sending 1000 messages 0f length 1000");
			return false;
		}
		
		//This is the case where fragmentation will happen 
		boolean testTwo = internetTest.run(3, 100000);
		if(testTwo ==false) {
			this.setError("Error while sending 10 messages 0f length 100000 , could be the problem with fragmentation");
			return false;
		}
		return true;
	}

}
