package networking.tests;

import infrastructure.validation.testing.TestCase;
import networking.testSimulator.InternetTestHelper;

public class InternetTest extends TestCase {

	@Override
	public boolean run() {
		
		
		InternetTestHelper internetTest = new InternetTestHelper();
		setDescription("Testing the internet Communication");
		setCategory("NETWORKING");
		setPriority(2);
		boolean testOne  = internetTest.run(10, 10);
		if(testOne == false) {
			this.setError("Error while sending 10 messages 0f length 10");
		}
		boolean testTwo  = internetTest.run(100, 100);
		if(testTwo ==false) {
			this.setError("Error while sending 100 messages 0f length 100");
		}
		/*boolean testThree= internetTest.run(1000, 1000);
		if(testThree ==false) {
			this.setError("Error while sending 1000 messages 0f length 1000");
		}
		boolean testFour = internetTest.run(10000, 10000);
		if(testFour ==false) {
			this.setError("Error while sending 10000 messages 0f length 10000");
		}
		boolean testFive = internetTest.run(10, 100000);
		if(testFive ==false) {
			this.setError("Error while sending 10 messages 0f length 100000 , could be the problem with fragmentation");
		}*/
		return true;
	}

}
