/**
 * @author - Abhishek Saran(111701001)
 * @module - Infrastucture module 
 * @team - Test Harness
 * @description - ITest interface consisting of methods needed to implement and run unit test cases 
 * @summary - Setters and Getters methods will be implemented in the abstract class TestCase
 */

package infrastructure.validation.testing;

interface ITest{

	//Methods	
	String getDescription();
	String getError();
	String getCategory();
	String getPriority();
	void setDescription(String description_);
	void setError(String error_);
	void setCategory(String category_);
	void setPriority(int priority_);
	
	/* abstract method to be implemented for each TestCase
	 * the run evaluates the test case against the expected output and actual output. 
	 * Set the errors before returning the result of test case as pass or fail for which return value type being true and false respectively.
	 */
	boolean run();

}



