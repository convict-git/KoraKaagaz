/**
 * @author        Abhishek Saran, Manas Sanjay
 * @module        Infrastructure module 
 * @team          Test Harness
 * @description   Test Harness main class
 * @summary       To provide functionality to tester to run test case sets in various ways, 
                  Methods such as runAll(), runByName(), runByCategory() and runByPriority() are provided. 
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
    this.savePath = path;
  }
  
  public String getSavePath(){
    return this.savePath;
  }
  
  /**
  * static helper method to return all the test cases of project
  * @return ArrayList of File type containing all the test cases with valid naming
  */
  private static ArrayList<File> getAllTests(){
    ILogger logger = LoggerFactory.getLoggerInstance();
    ArrayList<File> allTests = new ArrayList<File>();
    try{
      File[] modules = new File("../../../").listFiles(File::isDirectory); 
      for(File module : modules){
        if(module.isDirectory()){
          String strModule = module.getName();
          File f = new File("src/"+strModule+"/tests/");
          File[] tests  = f.listFiles(File::isFile);
          for (File test : tests){
            if(test.getName().endsWith("Test.java")){
              allTests.add(test); 
            }
          } 
        }     
      }
    }
    //If any module does not content test directory or test directory is empty
    catch (Exception e){
      logger.log(ModuleID.TEST, LogLevel.WARNING, "Exception >> "+e);      	
    } 
    return allTests;
  }


 /**
  * Void method to run test case of a given category
  * @param category The name of the category of the test to be run
  */
  public void runByCategory(String category){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    try{
      String path = "../../../" + category + "/tests";

      File directoryPath = new File(path);
      //get all the test cases in tests directory of respective category 
      String tests[] = directoryPath.list();
      totalNumberOfTests = tests.length;
      
      //create object of each test case class to run and get the result of each test
      for(int i=0; i<tests.length; i++){
        //Get full qualified class name from absolute path of testcase
        String absPath = tests[i].getAbsolutePath();
        String[] arrOfStr = absPath.split("src/", 2); 
        arrOfStr = arrOfStr[1].split(".java", 2);
        String relPath = arrOfStr[0];
        String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object test = testClass.getDeclaredConstructor().newInstance();

        if(category.equals(test.getCategory())){
          boolean result = test.run();

          if(result == false){
            failedNumberOfTests++;
            if(failedNumberOfTests == 1){
              logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
            }
           	//log the result of failed test cases
            logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+": "+test.getError());
          }
          else{
            successfulNumberOfTests++;
          }   
        }
      } 
		}
    catch (Exception e){
      logger.log(ModuleID.TEST, LogLevel.WARNING, "Exception in module: "+strModule+" >> "+e);      	
    }
    
    //result logging
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));  
  }


 /**
  * Void method to run test case of a given priority
  * @param priority The priority level of the test to be run
  */
  public void runByPriority(int priority){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    try{
      //get list of all the tests using helper static method getAllTests()
      ArrayList<File> allTests = getAllTests();
      totalNumberOfTests = allTests.size();

      for (File testFile : allTests){
        //Get full qualified class name from absolute path of testcase
        String absPath =  testFile.getAbsolutePath();
       	String[] arrOfStr = absPath.split("src/", 2); 
       	arrOfStr = arrOfStr[1].split(".java", 2);
       	String relPath = arrOfStr[0];
       	String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object test = testClass.getDeclaredConstructor().newInstance();
        
        if(priority == (test.getPriority())){
           boolean result = test.run(); 
           if(result == false){
            failedNumberOfTests++;
            if(failedNumberOfTests == 1){
              logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
            }
            //log the result of failed test cases
            logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+": "+test.getError());
           }
           else{
              successfulNumberOfTests++;
           }  
        }  
      }    
     }
     catch (Exception e){
    	logger.log(ModuleID.TEST, LogLevel.WARNING, "Exception >> "+e);      	
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
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    //call the helper method to get list of all test cases 
    ArrayList<File> allTests = getAllTests();
    
    try{
      totalNumberOfTests = allTests.size(); 
      for (File testFile : allTests){
        //Get full qualified class name from absolute path of testcase
        String absPath =  testFile.getAbsolutePath();
       	String[] arrOfStr = absPath.split("src/", 2); 
       	arrOfStr = arrOfStr[1].split(".java", 2);
       	String relPath = arrOfStr[0];
       	String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object test = testClass.getDeclaredConstructor().newInstance();
        boolean result = test.run();  
         
        if(result == false){
          failedNumberOfTests++;
          if(failedNumberOfTests == 1){
            logger.log(ModuleID.TEST, LogLevel.INFO, "Failed Tests...");
          }
          //log the result of failed test cases
          logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+": "+test.getError());
        }
        else{
          successfulNumberOfTests++;
        }
      }
		}
    catch (Exception e){
      logger.log(ModuleID.TEST, LogLevel.WARNING, "Exception >> "+e);      	
    }
    
    //result logging 
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
  } 
	 
	  
 /**
  * Boolean method to give result of the Test Case Class
  * @param testNamePath The absolute path to file name of test case class including .java extension as a string
  */
  public void runByName(String testNamePath){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    try{
      totalNumberOfTests = 1;

      //Get full qualified class name from absolute path of testcase
      String[] arrOfStr = testNamePath.split("src/", 2); 
      arrOfStr = arrOfStr[1].split(".java", 2);
      String relPath = arrOfStr[0];
      String fullQualifiedClassName = relPath.replace("/",".");

      Class<?> testClass = Class.forName(fullQualifiedClassName);
      Object test = testClass.getDeclaredConstructor().newInstance();
      boolean result = test.run();

      if(result == false){
        failedNumberOfTests++;
        if(failedNumberOfTests == 1){
          logger.log(ModuleID.TEST, LogLevel.INFO, "\nFailed Test...");
        }
        //log the result of failed test cases
        logger.log(ModuleID.TEST, LogLevel.INFO, Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError());
      }
      else{
        successfulNumberOfTests++;
      }
    }
    catch (Exception e){
      logger.log(ModuleID.TEST, LogLevel.WARNING, "Exception >> "+e);      	
    }
      
    //result logging 
    logger.log(ModuleID.TEST, LogLevel.INFO, "\nOverall Result:");
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Tests: "+Integer.toString(totalNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests));
    logger.log(ModuleID.TEST, LogLevel.INFO, "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests));
  }
	
}
