# Test Harness Specification Docume

## Objective
- To provide an interface for the developers of different modules to create and manage tests.
- To discover and populate the different tests assemblies.
- Formatting the test results of different modules when tested and saving them in a loggerfile


## Features available to Developers of interface
- Each module will inplement the the interface ITest for each test case.
- The tests will be stored in the following format for easier identification: [Testname]_Test.java
- Tests should be stored in a folder named "tests" in each module which gives us the names of the tests defined for that module


## Features available to Developers of Test Harness 
- The tester can call the test harness class functions to run their tests 
- Tests can be run based on either test name, Module Name, priority level or all tests by calling respective functions
- Each Test has customized description of it's end result, purpose, error i.e the reason of failure.
- The tests have 3 levels of priority represented with an integer as 0,1,2 being low,medium,high respectively
- The each of the run functions of test harness function at the core of will call the run of the implemented test to get the boolean result 
- The number of successful and failed tests are tracked and the failed tests and the reason of failure along with the tracked numbers are logged to a results file using the logger object

### Function Elaboration Test Harness
- Run all the Tests: Using this will allow the module to run all the tests irrespective of category
``` java =
public void runAll()
        {
        foreach(string testName in testNames){
        runbyName(testName);
        }
}
```
- Run By Category : Using this the dev can run all the tests belonging to a particular category with no specification.

```java = 

public void runByCategory(Category category)
        {
        List<string> tests = new List<string>;
        // populate the tests names
        foreach(string c in tests.category){
        if(c == category){
        //run the test
        }
        else//if the test is not belonging to the category skip    
        }
}

```
- Run by Priority: If we want to run all the tests which are to be run according to the priority, this is used. We have 3 integers 0,1,2 having low,medium,high respectively.
- First we will be running tests with high priority, then medium, then low

```java=
public void runBypriority()
        {
        List<string> tests_1 = new List<string>;
        List<string> tests_2 = new List<string>;
        List<string> tests_3 = new List<string>;
        
        foreach(string name in tests){
        if(test.priority == 1)tests_1.add(name);
        if(test.priority == 2)tests_2.add(name);
        if(test.priority == 3)tests_3.add(name);
        //run the test
        }
        // now run all the tests by using runByname in loop in the following manner
}

```
- Run by Name: This allows the dev to run a particular test by specifying the name of the test given in the description

```java=
    public bool runByName(testName n,Category c){
        Object test = Class.forName(testName).newInstance();
        bool result = test.run()
        // create an instance of the test and then run the test. We will get the boolean value of either being a success or failure
        return result;
    }
```
- STRING PATH: The result of the tests run must be stored. This path will mentioned and stored in the form of a string.

- Set Save Path: This will allow to set the path to where we must save the results of the run

- Get Save Path: This will allow to get the path of where the dev want the result to be stored, thus allowing the result display to vary

### Function Elaboration ITest Interface 
- To get the 
``` java =
public String getDescription()
        {
        //return the description of the test case
        return description;
        }
}
```

- Run 
``` java =
public String getError()
        {
        //return the error, helpful in the case when a test case is failed
        return error;
        }
}
```

- Run 
``` java =
public String getCatagory()
        {
        //return the catagory of the test case which is the moudle name of the test case  
        return catagory;
        }
}
```


- Run 
//set the description of the test case
``` java =
public void setDescription(String des)
        {
            description = des;
        }
}
```

- Run 
//set the error occured int the test case 
``` java =
public void setError(String err)
        {
            error=err;
        }
}
```


- Run 
//set the catagory of the test case 
``` java =
public void setCategory(String c)
        {
            catagory=c;
        }
}
```

- Run 
//run method to run the interface 
``` java =
public bool run()
        {   
            //the method will compare the actual result with expected result 
            if(actualRes==expectedRes) return true;
            else {
            //set the error occured in the test case
            setError(err);
            return false
            }
        }
}
```

## Class Diagram




![](https://i.imgur.com/0DoSP9S.png)


## Test Cases:
- The dev's who will be working on preparing their respective tests will be explained here.
- Using the ITest Interface elaborated earlier, the dev create their test classes using the implementation of Itest Interface.
- The examples given here are the test classes Enqueue Test, Dequeue Test, Profile Image Test. Dev can prepare their own test classes in similar manner but should be saved in the format mentioned at the top.
- Dev can  then can use the Test harness class to run the tests to their usuage and convinence.


## Work Distribution
-Abhishek Saran: ITest Interface, Test Harness methods runByName and runAll
