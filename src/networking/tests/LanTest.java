package networking.tests;

import infrastructure.validation.testing.TestCase;
import networking.testSimulator.LanTestHelper;
/**
 * This is used for testing the LanCommunication
 * @author sravan
 *
 */
public class LanTest extends TestCase {

	@Override
	public boolean run() {
		
		
		LanTestHelper lanTest = new LanTestHelper();
		setDescription("Testing the Lan Communication");
		setCategory("NETWORKING");
		setPriority(1);
		//Run the tests for different message length and different number of messages
		
		boolean testOne= lanTest.run(1000, 1000);
		if(testOne ==false) {
			this.setError("Error while sending 1000 messages 0f length 1000");
			return false;
		}
		//This is the case where fragmentation will happen 
		boolean testTwo = lanTest.run(10, 100000);
		if(testTwo ==false) {
			this.setError("Error while sending 10 messages 0f length 100000 , could be the problem with fragmentation");
			return false;
		}
		return true;
	}

}
