/**
 * @author        Abhishek Saran
 * @author        Manas Sanjay
 * module         Infrastructure module 
 * team           Test Harness
 * description    Test Harness main class
 * summary        To provide functionality to tester to run test case sets in various ways, 
 *                Methods such as runAll(), runByName(), runByCategory() and runByPriority() are provided. 
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
      File[] modules = new File("src/").listFiles(File::isDirectory); 
      for(File module : modules){
        if(module.isDirectory()){
          File f = new File(module.getPath()+"/tests/");
          File[] tests  = f.listFiles(File::isFile);
          for (File test : tests){
            if(test.getName().endsWith("Test.java")){
              allTests.add(test); 
            }
          } 
        }     
      }
    }
    //If any module does not contain test directory or test directory is empty
    catch (Exception e){
    	logger.log(
    	  ModuleID.TEST, 
    	  LogLevel.WARNING, 
    	  "Exception >> "+e
    	);      	
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
      String path = "src/" + category + "/tests";

      File directoryPath = new File(path);
      //get all the test cases in tests directory of respective category
      File[] tests  = directoryPath.listFiles((d, name) -> name.endsWith("Test.java"));
      totalNumberOfTests = tests.length;
      
      //create object of each test case class to run and get the result of each test
      for(int i=0; i<tests.length; i++){
        //Get full qualified class name from absolute path of testcase
        String absPath = tests[i].getCanonicalPath();
        String[] arrOfStr = absPath.split("src/", 2); 
        arrOfStr = arrOfStr[1].split(".java", 2);
        String relPath = arrOfStr[0];
        String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object obj = testClass.getDeclaredConstructor().newInstance();
        ITest test = (ITest) obj;

        if(category.equals(test.getCategory())){
          boolean result = test.run();

          if(result == false){
            failedNumberOfTests++;
            if(failedNumberOfTests == 1){
              logger.log(
                ModuleID.TEST, 
                LogLevel.INFO, 
                "Failed Tests..."
              );
            }
           	//log the result of failed test cases
            logger.log(
              ModuleID.TEST, 
              LogLevel.INFO, 
              Integer.toString(failedNumberOfTests)+". "+tests[i].getName()+": "+test.getError()
            );
          }
          else{
            successfulNumberOfTests++;
          }   
        }
      } 
    }
    catch (Exception e){
      logger.log(
        ModuleID.TEST, 
        LogLevel.WARNING, 
        "Exception >> "+e
      );
    }
    
    //result logging
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "\nOverall Result:"
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Tests: "+Integer.toString(totalNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests)
    );  
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

      for (File testFile : allTests){
        //Get full qualified class name from absolute path of testcase
        String absPath =  testFile.getCanonicalPath();
        String[] arrOfStr = absPath.split("src/", 2); 
        arrOfStr = arrOfStr[1].split(".java", 2);
        String relPath = arrOfStr[0];
        String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object obj = testClass.getDeclaredConstructor().newInstance();
        ITest test = (ITest) obj;
        
        if(priority == (test.getPriority())){
          totalNumberOfTests++;
          boolean result = test.run(); 
          if(result == false){
            failedNumberOfTests++;
            if(failedNumberOfTests == 1){
              logger.log(
                ModuleID.TEST, 
                LogLevel.INFO, 
                "Failed Tests..."
              );
            }
            //log the result of failed test cases
            logger.log(
              ModuleID.TEST, 
              LogLevel.INFO, 
              Integer.toString(failedNumberOfTests)+". "+testFile.getName()+": "+test.getError()
            );
          }
          else{
            successfulNumberOfTests++;
          }  
        }  
      }    
    }
    catch (Exception e){
      logger.log(
        ModuleID.TEST, 
        LogLevel.WARNING, 
        "Exception >> "+e
      );      	
    }

    //result logging
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "\nOverall Result:"
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Tests: "+Integer.toString(totalNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests)
    );
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
        String absPath =  testFile.getCanonicalPath();
        String[] arrOfStr = absPath.split("src/", 2); 
        arrOfStr = arrOfStr[1].split(".java", 2);
        String relPath = arrOfStr[0];
        String fullQualifiedClassName = relPath.replace("/",".");

        Class<?> testClass = Class.forName(fullQualifiedClassName);
        Object obj = testClass.getDeclaredConstructor().newInstance();
        ITest test = (ITest) obj;

        boolean result = test.run();  
         
        if(result == false){
          failedNumberOfTests++;
          if(failedNumberOfTests == 1){
            logger.log(
              ModuleID.TEST, 
              LogLevel.INFO, 
              "Failed Tests..."
            );
          }
          //log the result of failed test cases
          logger.log(
            ModuleID.TEST, 
            LogLevel.INFO, 
            Integer.toString(failedNumberOfTests)+". "+testFile.getName()+": "+test.getError()
          );
        }
        else{
          successfulNumberOfTests++;
        }
      }
    }
    catch (Exception e){
      logger.log(
        ModuleID.TEST, 
        LogLevel.WARNING, 
        "Exception >> "+e
      );      	
    }
    
    //result logging 
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "\nOverall Result:"
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Tests: "+Integer.toString(totalNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests)
    );
  } 
	 
	  
 /**
  * Void method to give result of the Test Case Class
  * @param testNamePath The relative path to file name of test case class starting from src/ including .java extension as a string
  */
  public void runByName(String testNamePath){
    int totalNumberOfTests = 0;
    int successfulNumberOfTests = 0;
    int failedNumberOfTests = 0;
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    try{
      totalNumberOfTests = 1;

      String[] arrOfStr = testNamePath.split("src/", 2);
      //extracting test name
      String testName = arrOfStr[1].split("tests/",2)[1];
      //Extract full qualified class name from absolute path of testcase
      arrOfStr = arrOfStr[1].split(".java", 2);
      String relPath = arrOfStr[0];
      String fullQualifiedClassName = relPath.replace("/",".");

      Class<?> testClass = Class.forName(fullQualifiedClassName);
      Object obj = testClass.getDeclaredConstructor().newInstance();
      ITest test = (ITest) obj;

      boolean result = test.run();
      if(result == false){
        failedNumberOfTests++;
        if(failedNumberOfTests == 1){
          logger.log(
            ModuleID.TEST, 
            LogLevel.INFO, 
            "\nFailed Test..."
          );
        }
        //log the result of failed test cases
        logger.log(
          ModuleID.TEST, 
          LogLevel.INFO, 
          Integer.toString(failedNumberOfTests)+". "+testName+" :"+test.getError()
        );
      }
      else{
        successfulNumberOfTests++;
      }  
    }
    catch (Exception e){
      logger.log(
        ModuleID.TEST, 
        LogLevel.WARNING, 
        "Exception >> "+e
      );      	
    }
      
    //result logging 
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "\nOverall Result:"
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Tests: "+Integer.toString(totalNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Successful Tests: "+Integer.toString(successfulNumberOfTests)
    );
    logger.log(
      ModuleID.TEST, 
      LogLevel.INFO, 
      "Total Number of Failed Tests: "+Integer.toString(failedNumberOfTests)
    );
  }
  
  
 /**
  * Void main method to drive the test harness
  * The args in main method must follow the below pattern-
  * To run tests by category>> runByCategory categoryName
  * To run tests by priority>> runByPriority priorityLevel
  * To run all tests>> runAll
  * To run a single test by its name>> runByName pathToTheTestStartingFrom-/src
  */
  public static void main(String args[]){
    
    ILogger logger = LoggerFactory.getLoggerInstance();
    TestHarness testHarness = new TestHarness(); 
    try{
    
      if(args[0].equals("runByCategory")){
        boolean validCategoryName = false;
        for(ModuleID category : ModuleID.values()){
          if(category.name().toLowerCase().equals(args[1].toLowerCase()) && !(args[1].toLowerCase().equals("test"))){
            validCategoryName = true;
            break;
          }
        }         
        if(validCategoryName){
          testHarness.runByCategory(args[1]);
        }
        else{
          throw new IllegalArgumentException("Invalid category name!");
        }
      }
       
      else if(args[0].equals("runByPriority")){
        int priority = Integer.parseInt(args[1]);
        if(priority < 0 || priority > 2) {
          throw new IllegalArgumentException("Invalid priority level!");
        }
        testHarness.runByPriority(priority);
      }
       
      else if(args[0].equals("runAll")){
        if(args.length > 1){
          throw new IllegalArgumentException("More arguments provided than placeholders specified!");          
        }
        else{
          testHarness.runAll();
        }
      }
       
      else if(args[0].equals("runByName")){
        File testFile = new File(args[1]); 
        if(!args[1].startsWith("src/")){
          throw new IllegalArgumentException("Test file path doesn't start from src/");
        }
        else if(!args[1].endsWith("Test.java")){
          throw new IllegalArgumentException("Test file path doesn't end with Test.java!");
        }
        else if(!testFile.exists() || !testFile.isFile()){
          throw new IllegalArgumentException("Test file doesn't exist!");
        }
        else{
          testHarness.runByName(args[1]);
        }
      }
      
      else{
        throw new IllegalArgumentException(
          "No such method to call!!\n"
          + "Please pass the argument in below manner:\n"
          + "for runByCategory()>> runByCategory categoryName \n"
          + "for runByPriority()>> runByPriority priorityLevel \n"
          + "for runAll()>> runAll \n"
          + "for runByName()>> runByName pathOfTestFileStartingFrom-/src"
        );
	    }
    }
    catch (Exception e){
      logger.log(
        ModuleID.TEST, 
        LogLevel.WARNING, 
        "Exception >> "+e
      );      	
    }
    
  }
	
}
