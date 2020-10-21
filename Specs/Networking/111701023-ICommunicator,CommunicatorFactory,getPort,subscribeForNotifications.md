# **DESIGN SPECIFICATION Document**
                                                 by Prudhvi Vardhan Reddy Pulagam
    
# Table of Contents
* [Team Members](#Team-Members)
* [Role](#Role)
* [Overview](#Overview)
* [Tasks Assigned](#Tasks-Assigned)
* [Design](#Design)
    * [Singleton Factory Pattern](#Single-Factory-Pattern)
    * [Publisher - Subscriber Pattern](#Publisher-Subscriber-Pattern)
* [Objectives](#Objectives)
    * [ICommunicator Interface](#ICommunicator-Interface)
    * [CommunicatorFactory Class](#Communicator-Class)
    * [getPort() method](#getPort()-method)
    * [subscribeForNotifications(String identifier, INotificationHandler handler) method](#subscribeForNotifications()-method)
* [Class Diagram](#Class-Diagram)

# [Team Members](https://)
Networking Module Team Members

| Name | Rollno |
| -------- | -------- |
| Prudhvi Vardhan Reddy Pulagam     | 111701023    |
| Pandravism Shiva Santhosh         | 111701021|
| Sirpa Sahul| 111701028|
| Shiva Sai Teja|111701018|
| Madaka Srikar Reddy| 111701017|
| Drisya Ponamari| 111701031|

Team Lead: Dasari Sravan Kumar(Rollno: 111701010)

# Role
Member of Networking Module.
# [Overview](https://)
We are developing whiteboard application, Where anonymous user can join and share single whiteboard over LAN. There is chat communication between the users.
# [Tasks Assigned](https://)
* Define an ICommunicator interface and create a communicatorFactory class following Singleton factory design pattern.
* Write getPort() and subscribeForNotifications() functions in the LanCommunicator class.
# [Design](https://)
* ## [Singleton Factory Pattern](https://)
    &ensp; Factory pattern is one of the core design principles to create an object, allowing clients to create objects of a library in a way such that it doesn’t have tight coupling with the class hierarchy of the library.Factory pattern encapsulate object creation logic which makes it easy to change it later when you change object creation logic or you can even introduce new object with just change in one method.
   &ensp; The specific use of Factory pattern is when we have add a feature to send data over internet. We might have to use a seperate communicator Class or change some part of the class , But by using Factory pattern only creation logic is changed.
    &ensp; So Networking Module will be providing a Factory class called CommunicatorFactory which provides a method called getCommunicator to get a ICommunicator object of ICommunicator Interface.
    &ensp; As there is only need of single socket to send the data there should not be more than one instance of the ICommunicator object, So The Factory Class should create the ICommunicator object only once. So the Factory class should follow Singleton Creation design Pattern, Where a class creates single instance of that class, there won't be multiple instances. A Class variable is used for this purpose.
    <br />
    
    ![](https://i.imgur.com/MJbJsci.png)





* ## [Publisher - Subscriber Pattern](https://)
     &emsp; The publisher–subscriber is a messaging pattern where senders of messages, called publishers, do not program the messages to be sent directly to specific receivers, called subscribers, but instead categorize published messages into classes without knowledge of which subscribers, if any, there may be. Similarly, subscribers express interest in one or more classes and only receive messages that are of interest, without knowledge of which publishers.
     &emsp; So here the Networking module acts as publisher for recieving messages over the network, By providing subscribeForNotifications() method of the ICommunicator object, And The Content and Processing module acts subscirbers. Subscribers subscribe to recieving messages by passing handler to subscribeForNotifications() method of ICommunicator object. So When the Networking Module receives the data, it would call the appropriate provided handler(as an argument).
![](https://i.imgur.com/0HElJDr.png )
# [Objectives](https://)
* ## [ICommunicator Interface](https://)
    This Interface is description of overall functions that the Networking Module implements.
```
public interface ICommunicator {

    //Start communication by initalizing all the threads needed for communication. 
    void start();
    
    //Stop the Communication.
    void stop();
    
    //Send the data to the destination host.
    void send(String destination, String message, String identifier);
    
    //Subscribe the module for receiving the data from the network module.
    void subscribeForNotifications(String identifier, INotificationHandler handler);
}
```
* ## [CommunicatorFactory Class](https://)
     - This is class used for implementation of ICommunicator Interface. This class has to follow Singleton Factory design pattern, for which the getCommunicator() method is designed in this class. The method getCommunicator() takes integer(which is free port number) and returns ICommunicator object of CommunicatorFactory class. This getCommunicator() method can be accessed before instantiation of the object of it's own class(CommunicatorFactory class).
    - For following Factory pattern getCommunicator() calls the constructor method of the this class and then constructor the creates an object and returns to getCommunicator() method and thus object is in initialised in this way.
    - For following Singleton pattern a class variable is maintained in CommunicatorFactory class and logic in getCommunicator() method helps in following Single pattern.
```
class CommunicatorFactory(){
    //Constructor which creates the object.
    private CommunicatorFactory(int port){
        ...
    }
    //This method helps instantiation of object.
    public static ICommunicator getCommunicator(int port){
        ...
    }
    //This method returns available free port in the system.
    public static int getPort(){
        ...
    }
    //Subscribe the module for receiving the data from the network module.
    void subscribeForNotifications(String identifier, INotificationHandler handler){
        ...
    }
    .
    .
    .
}
```
* ## [getPort() method](https://)
    &ensp; This method is defined in ICommunicator Interface and implemented in CommunicatorFactory class. This method used to get free port available in system. This method takes no arguments and returns an integer, Which is port number. With the returned port number the ICommunicator object is created. Where ICommunicator object is object of CommunicatorFactory class.**<i> So the getPort() method is used before instantiation of it's own class.</i>**
* ## [subscribeForNotifications(String identifier, INotificationHandler handler) method](https://)
    &ensp; This function is defined in ICommunicator Interface and implemented in CommunicatorFactory class. This method used to implement Publisher-Subscriber pattern. This method takes two arguments Identifier and handler and returns nothing. Based on the Identifier the handler is provided(as an argument) and subscriber gets the data.
# [Class Diagram](https://)
Below shows the class diagram for the ICommunicator Instance and CommunicatorFactory class.

![](https://i.imgur.com/js1I9yl.png)



