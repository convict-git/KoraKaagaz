Infrastructure Team Leads Spec
======
##### *Prepared by Rahul R, 121701021 on 04-10-2020*
The Team
======

| Members   |
|:---       |
| Rahul R(Team Lead)   |
| Abhishek Saran   |
| Akash Nayak   |
| Badal Kumar   |
| Md Talha Yaseen Khan   |
| Manas Sanjay |
| Navaneeth M Nambiar   |

Modules 
======

* Content module
* Tools and Validation module
    * Test Harness 
    * Diagnostic/Logging Framework
    * Static Code Analysis

## Dependencies


* All components of content module and tools and validation module(Except static code analysis) are initialised by the Executive module. Static code analysis is done by a standalone script.
* Test harness uses the logging framework and Java reflection
* Logger object is used by all modules
* Static code analysis depends on the Java Reflection API

Deliverables
======
## Test Harness
* Provide an interface for developers to create tests
* Provide a way of discovering new tests and populating the test assembly
* Format the test results

## Content Module
* Provide chat service to the user
* Provide the option of profile image to the user
* Maintain user details

## Logging Framework
* Provide a logger interface to all modules
* Output a log file with a fixed format
* Provide output toggling option for console logging
* Provide formatted console logs

## Static Code Analysis
* Check the code for style concurrence according to set style guidelines

# Tools and Validation Module
## Test Harness

### Interface
``` java
public interface ITest {
    
    // Data Members
    String category;
    String error;
    String description;
    int priority;
    
    //Methods
    String getDescription();
    String getError();
    String getCategory();
    String getPriority();
    String setDescription();
    String setError();
    String setCategory();
    String setPriority();
    
    boolean Run();
}
```

### Class Diagram
![Test Harness](https://i.imgur.com/GKLpf15.png)

### Features
* Tests can be chosen based on either test name, category(module name), priority level or all tests
* Each test has a description string to it describing the purpose of the test and expected result
* Each test has a error string used to store the reason of failure of the test
* Each test has a priority level with 0 - Low priority, 1 - Medium priority and 2 - High priority which can be selected as filter
* The number of successful and failed tests are tracked and the failed tests and the reason of failure along with the tracked numbers are logged to a results file using the logger object
* The results are printed to console using the logger object

### Design Decisions
* The tests will be stored in the following format for easier identification:
[Testname]_Test.java
* Tests are found in a folder named "tests" in each module which gives us the names of the tests defined for that module 
* Java reflection is used to create an instance of the test from the name of the test
* The results are stored in a folder name TestResults with the following format: 
[Current Date]_[All/Category/Priority/Name]__[Test Number].txt

## Logging Framework

### Interface
``` java
public interface ILogger
{
        //Methods
        void LogError(int moduleIdentifier, string logMessage);
        void LogWarning(int moduleIdentifier, string logMessage);
        void LogSuccess(int moduleIdentifier, string logMessage);
        void LogInfo(int moduleIdentifier, string logMessage);
}
```

### Class Diagram

![Logging framework](https://i.imgur.com/cfKAHWh.png)

### Design Decisions

* Between two different designs of having one instance of logger for each module and a single global instance for all modules, the latter was chosen as the format of the logs should be consistent across modules
* Separate log functions for each log priority was chosen over a single log function with log priority as a string argument to avoid invalid usage of logger outside of defined values
* File logging and Console logging has been combined into a single logging class where console logging can be toggled On or Off
* All the log functions will write to a file whose filepath can be configured
* The logs will be written to file in the following format:
[YYYY:MM:DD HH:MM:SS] [Error/Warning/Info/Success] [moduleName] logMessage 
* The logs files will be named in the following format:
[Current Date]_[Log Number].txt
## Static Code Analysis

### Design Decisions

Java Reflection API is being used for code analysis and the script is being run in the pipeline before the code is  pushed to github.

All the style checks are done according to this coding standard: https://google.github.io/styleguide/javaguide.html

### Features
The below code style checking features are provided by the static code analysis script:
* Parenthesis format checking 
* Checking for well structured comments
* Indentation checking
* Checks for spacing between variables

# Content Module

## Interfaces

## Between UI module and Content Module
```Java
public interface contentICommunicator{
    //Methods
    void notifyUserExit();
    void initialiseUser(String username, String ip, String image);
    void sendMessageToContent(String message);    
    void subscribeForNotifications(String identifier, contentNotificationHandler handler);
}
```
```Java
public interface contentNotificationHandler {
    //Methods
    void onMessageReceived(String username, String message, String image);            
    void onNewUserJoined(String username);
}

```
## Between Content Module and Networking module

``` Java
public interface INotificationHandler {
    void onMessageReceived(String message);
}
```
``` Java
public interface ICommunicator {
    #Start communication
    void start();
    #Stop the Communication
    void stop();
    #Send the data to the destination IP
    void send(String destination, String message, String identifier);
    #Subscribe the module for receiving the data from the network module
    void subscribeForNotifications(String identifier, INotificationHandler handler);
}
```
### Design Decisions
* Content module interacts with UI module and Networking module parallel to the processing module
* The content module uses the Icommunicator interface exposed by the Networking module to send data to the server
* It uses the INotificationHandler interface exposed by the Networking to receive data from the server
* A similar setup is used between content module and UI module for exchange of data
* The messages are sent in plain text as no formatting requirement was given from UI side
* The data is converted into a JSON format before sending to the server
* The following fields was agreed for the JSON format as it gave complete information about the data being sent
[Meta]: Information on the type of data being sent(Image/Chat) and the client/user identifier(Username)
[Data]: The actual data being sent
* The message identifier being given as the value for the argument 'identifier' in the send() API given by networking, is given separately and also packaged into the JSON message so that the networking module can easily identify the type of message on the receiving client and send to the appropriate module without having to read the JSON message
* Since the content module is handling sending and receiving on both interfaces, two queues are being used for the data flows 
UI -> Content -> Networking and Networking -> Content -> UI
* Due to simultaneous operation of publishing and listening on the UI-Content interface, an additional thread will be used 
* The content module will receive the username, profile picture and server ip once from the UI at client initialisation and notify the server of the new username and profile image so that the server can update its map of users. The server will then notify other clients of the new user by sending its updated map of users to all clients. This map is then locally stored on the respective content modules of each client. This way the load on the network is reduced since the profile images are stored locally on each client.

## Test Plan 
## Content Module
* Unit test the queues used in the module
* Unit test the Serialization and Deserialization algorithm used for converting data to JSON string
* Unit test the interfaces between UI and Content Module
* Unit test the interfaces between Content and Networking module

## Test Harness
* Unit test the test harness using pre-defined test cases that are known to give deterministic outputs

## Logging framework
* Unit tests for the file logging

## Work Distribution

### Content Module
* Badal Kumar
* Talha Yaseen

### Logging Framework
* Navneeth Nambiar

### Test Harnes
* Abhishek Saran
* Manas Sanjay

### Static Code Analysis
* Akash Nayak

