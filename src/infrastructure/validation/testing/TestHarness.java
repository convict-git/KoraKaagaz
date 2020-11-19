/*
 * @author        Abhishek Saran, Manas Sanjay
 * @module        Infrastructure module 
 * @team          Test Harness
 * @description   Test Harness main class
 * @summary       To provide functionality to tester to run test case sets in various ways- 
                  Methods such as runAll(), runByName(), runByCategroy() and run by  Priority() are provided. 
 */
package infrastructure.validation.testing;
import java.io.File;
import java.util.*;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;

public class TestHarness{
  
  private String savePath;
  
  public void setSavePath(String path){
    this.savePath=path;
  }
  
  public String getSavePath(){
    return this.savePath;
  }
  
  /**
  * static helper method to return all the test cases of project
  * @return    ArrayList of File type containing all the test cases with valid naming
  */
  private static ArrayList<File> getAllTests(){
    File[] modules = new File("../../../").listFiles(File::isDirectory);
    ArrayList<File> allTests = new ArrayList<File>();
    for(File module : modules){
      if(module.isDirectory()){
        String strModule = module.getName();
        System.out.println(strModule);
        try {
          File f = new File("src/"+strModule+"/tests/");
          File[] tests  = f.listFiles(File::isFile);
          for (File test : tests){
            if(test.getName().endsWith("Test.java")){
              allTests.add(test); 
            }
          } 
        }
        catch (Exception e){
          System.out.println("Exception in module: "+strModule+" >> "+e);
        }
      }
    } 
    return allTests;
  }


 /**
  * Void method to run test case of a given category
  * @param Category    The name of the category of tests to be run
  */
  public void runByCategory(String Category){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    String path = "../../../" + Category + "/tests";
    try{
      File directoryPath = new File(path);
      //get all the test cases in tests directory of respective category 
      String tests[] = directoryPath.list();
      totalNumberOfTests = tests.length;
      
      //create object of each test case class to run and get the result of each test
      for(int i =0; i<tests.length; i++){
        String[] arrOfStr = tests[i].split(".", 2); 
        String testClassName = arrOfStr[0];
        Class<?> testClass = Class.forName("testClassName");
        Object test = testClass.getDeclaredConstructor().newInstance();
        if(Category.equals(test.getCategory())){
          boolean result = test.run();

          if(result==false){
            failedNumberOfTests++;
            if(failedNumberOfTests==1){
              logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
            }
          //log the result of failed test cases
          logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError());
          }
          else{
            successfulNumberOfTests++;
          }
        
        }
      }
    }
    catch (Exception e){
      System.out.println("Exception in module: "+Category+" >> "+e);
    }
	  
    //result logging
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
  }


 /**
  * Void method to run test case of a given priority
  * @param Category    The name of the category of tests to be run
  */
  public void runBypriority(int priority){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    //get list of all the tests using helper static method getAllTests()
    ArrayList<File> allTests = getAllTests();
    totalNumberOfTests = allTests.size();
    
    for (File testFile : allTests){
      String testName = testFile.getName();
      String[] arrOfStr = testName.split(".", 2); 
      String testClassName = arrOfStr[0];
      Class<?> testClass = Class.forName("testClassName");
      Object test = testClass.getDeclaredConstructor().newInstance();
      if(priority==(test.getPriority())){
         boolean result = test.run(); 
         
         if(result==false){
          failedNumberOfTests++;
          if(failedNumberOfTests==1){
            logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
          }
          //log the result of failed test cases
          logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError());
        }
        else{
          successfulNumberOfTests++;
        }
        
      }  
    } 
      
    //result logging
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
  }


 /**
  * Run all test cases of all modules one by one 
  */
  public void runAll(){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    //ArrayList<File> failedTests;
    
    ILogger logger = LoggerFactory.getLoggerInstance();
     
    //call the helper method to get list of all test cases 
    ArrayList<File> allTests = getAllTests();
    totalNumberOfTests = allTests.size();
    
    for (File testFile : allTests){
      String testName = testFile.getName();
      String[] arrOfStr = testName.split(".", 2); 
      String testClassName = arrOfStr[0];
      Class<?> testClass = Class.forName("testClassName");
      Object test = testClass.getDeclaredConstructor().newInstance();
      boolean result = test.run();  
       
      if(result==false){
        failedNumberOfTests++;
        if(failedNumberOfTests==1){
          logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
        }
        //log the result of failed test cases
        logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError());
      }
      else{
       successfulNumberOfTests++;
      }
    }
    
    //result logging 
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
  } 
	 
	  
 /**
  * Boolean method to give result of the Test Case Class
  * @param testName    The absoulete path to file name of test case class including .java extension as a string
  * @return    <code>true</code> if the test case pass 
  *     and actual output is same as expected output; 
  *     <code>false</code> otherwise.
  */
  public boolean runByName(String testName){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    totalNumberOfTests = 1;
    String[] arrOfStr = testName.split(".", 2); 
    String testClassName = arrOfStr[0];
    Class<?> testClass = Class.forName("testClassName");
    Object test = testClass.getDeclaredConstructor().newInstance();
    boolean result = test.run();
    
    if(result==false){
      failedNumberOfTests++;
      if(failedNumberOfTests==1){
        logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
      }
      //log the result of failed test cases
      logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError());
    }
    else{
     successfulNumberOfTests++;
    }
    
    //result logging 
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
    
    return result;
  }

}
