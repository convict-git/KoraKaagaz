/**
 * @author        Abhishek Saran
 * @module        Infrastructure module 
 * @team          Test Harness
 * @description   ITest interface consisting of methods needed to implement and run unit test cases 
 * @summary       Setters and Getters methods will be implemented in the abstract class TestCase
 */	

  package infrastructure.validation.testing;

  public interface ITest{

  //Methods	
  String getDescription();
  String getError();
  String getCategory();
  int getPriority();
  void setDescription(String description);
  void setError(String error);
  void setCategory(String category);
  void setPriority(int priority);

  /**
   * abstract method to be implemented for each TestCase
   * @return  <code>true</code> if the test case pass 
   *     and actual output is same as expected output; 
   *     <code>false</code> otherwise.
   */
 boolean run();

}



