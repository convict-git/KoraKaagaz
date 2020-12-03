package networking.tests;

import infrastructure.validation.testing.TestCase;
import networking.testSimulator.LanTestHelper;

public class LanTest extends TestCase {

	@Override
	public boolean run() {
		
		
		LanTestHelper lanTest = new LanTestHelper();
		setDescription("Testing the Lan Communication");
		setCategory("NETWORKING");
		setPriority(1);
		boolean testOne  = lanTest.run(10, 10);
		if(testOne == false) {
			this.setError("Error while sending 10 messages 0f length 10");
			return false;
		}
		boolean testTwo  = lanTest.run(100, 100);
		if(testTwo ==false) {
			this.setError("Error while sending 100 messages 0f length 100");
			return false;
		}
		boolean testThree= lanTest.run(1000, 1000);
		if(testThree ==false) {
			this.setError("Error while sending 1000 messages 0f length 1000");
			return false;
		}
	
		boolean testFive = lanTest.run(10, 100000);
		if(testFive ==false) {
			this.setError("Error while sending 10 messages 0f length 100000 , could be the problem with fragmentation");
			return false;
		}
		return true;
	}

}
