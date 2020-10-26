/**
 * @author - Abhishek Saran(111701001)
 * @module - Infrastucture module 
 * @team - Test Harness
 * @description - ITest interface with abstract class TestCase 
 * @summary - Modules have to extend the abstract class to implement the run method for each test case  
 */


interface ITest{

	//Methods
	void setDescription(String des);
	String getDescription();
	void setError(String err);
	String getError();
	void setCategory();
	String getCategory(String c);
	void setPriority(int p);
	String getPriority();

	boolean run();

}

public abstract class TestCase implements ITest {

	//data members
	//category of a test case as its module name 
	private String category;
	
	//error occured in the test case 
  private String error;
  
  //a small description about what this test case is about
  private String description;
  
  //the priority level of the test case - 0 being lowest and 2 being highest
  private int priority;
  
  /* abstract method to be implemented for each TestCase
   * the run evaluates the test case against the expected output and actual output. 
   * Set the errors before returning the result of test case as pass or fail for which return value type being true and false respectively.
   */
  public abstract boolean run();
  
  //getters and setters for @param=description
  public void setDescription (String description_) {
  	this.description=description_;
	}

	public String getDescription() {
		return this.description;
	}
	
	//getters and setters for @param=error
	public void setError(String error_) {
  	this.error=error_;
	}

	public String getError() {
		return this.error;
	}
	
	//getters and setters for @param=category
	 public void setCategory(String category_) {
  	this.category=category_;
	}

	public String getCategory() {
		return this.category;
	}
	
	//getters and setters for @param=priority
	public void setPriority(int priority_) {
 		this.priority=priority_;
	}

	public String getPriority() {
		return this.priority;
	}
	

}


