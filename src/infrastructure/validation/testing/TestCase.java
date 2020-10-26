/**
 * @author - Abhishek Saran(111701001)
 * @module - Infrastucture module 
 * @team - Test Harness
 * @description - Abstract class which implements ITest Interface
 * @summary - Modules have to extend the abstract class to implement the run method for each test case  
 */

package infrastructure.validation.testing;

public abstract class TestCase implements ITest {

	//data members
	/*
	 * @category - of a test case as its module name 
	 * @error - occured in the test case 
	 * @description - a small description about what this test case is about
	 * @priority - the priority level of the test case - 0 being lowest and 2 being highest
	 */
	private String category;
	private String error;
	private String description;
	private int priority;

	//methods
	public abstract boolean run();

	public void setDescription (String description_) {
	this.description=description_;
	}

	public String getDescription() {
	return this.description;
	}

	public void setError(String error_) {
	this.error=error_;
	}

	public String getError() {
	return this.error;
	}

	public void setCategory(String category_) {
	this.category=category_;
	}

	public String getCategory() {
	return this.category;
	}

	public void setPriority(int priority_) {
	this.priority=priority_;
	}

	public String getPriority() {
	return this.priority;
	}

}

