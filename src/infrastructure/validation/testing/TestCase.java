/**
 * @author        Abhishek Saran
 * @module        Infrastructure module 
 * @team          Test Harness
 * @description   Abstract class which implements ITest Interface
 * @summary       Modules have to extend the abstract class to implement the run method for each test case  
 */

package infrastructure.validation.testing;

public abstract class TestCase implements ITest{

  //data members
  /**
   * @param category    The category of a test case is its module name 
   * @param error       The error occured in the test case 
   * @param description A small description about what this test case is about
   * @param priority    The priority level of the test case - 0 being lowest and 2 being highest
   */
  private String category;
  private String error;
  private String description;
  private int priority;

  //methods
  /**
  * abstract method to be implemented for each TestCase
  * @return    <code>true</code> if the test case pass 
  *     and actual output is same as expected output; 
  *     <code>false</code> otherwise.
  */
  public abstract boolean run();

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return this.error;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return this.category;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }

}

