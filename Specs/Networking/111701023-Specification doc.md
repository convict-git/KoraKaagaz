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
    * [INotificationHandler Interface](#INotificationHandler-interface)
    * [CommunicatorFactory Class](#CommunicatorFactory-Class)
    * [ClientInfo Class](#ClientInfo-Class)
    * [subscribeForNotifications(String identifier, INotificationHandler handler) method](#subscribeForNotifications()-method)
* [Class Diagram](#Class-Diagram)
* [Package](#Package)
* [Coding-Style](#Coding-Style)

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
We are developing whiteboard application, Where anonymous user can join and share single whiteboard over LAN. There is chat communication available between the users.
# [Tasks Assigned](https://)
* Define an ICommunicator interface and create a communicatorFactory class following Singleton factory design pattern and This class should provide private IP address and Port number at client.
* Write subscribeForNotifications() functions in the LanCommunicator class.
* ClientInfo class 
# [Design](https://)
* ## [Singleton Factory Pattern](https://)
    &ensp; Factory pattern is one of the core design principles to create an object, allowing clients to create objects of a library in a way such that it doesn’t have tight coupling with the class hierarchy of the library.Factory pattern encapsulate object creation logic which makes it easy to change it later when you change object creation logic or you can even introduce new object with just change in one method.
   &ensp; The specific use of Factory pattern is when we have add a feature to send data over internet. We might have to use a seperate communicator Class or change some part of the class, And if we use separate communicator class we may face question which communicator object should be instantiated and maintained, but by using Factory pattern only creation logic in one method is changed and rest assured.
    &ensp; So Networking Module will be providing a Factory class called CommunicatorFactory which provides a method called getCommunicator to get a LanCommunicator object of ICommunicator Interface.
    &ensp; As there is only need of single socket to send the data there should not be more than one instance of the LanCommunicator(which implements ICommunicator) object, So The Factory Class should create the LanCommunicator(which implements ICommunicator) object only once. So the Factory class should follow Singleton Creation design Pattern in creating LanCommunicator object, So that there won't be multiple instances of LanCommunicator class. A Class variable is used for this purpose.
    <br />
    
    ![](https://i.imgur.com/RKXlZfG.png)






* ## [Publisher - Subscriber Pattern](https://)
    &emsp; The publisher–subscriber is a messaging pattern where senders of messages, called publishers, do not program the messages to be sent directly to specific receivers, called subscribers, but instead categorize published messages into classes without knowledge of which subscribers, if any, there may be. Similarly, subscribers express interest in one or more classes and only receive messages that are of interest, without knowledge of which publishers.
     &emsp; So here the Networking module acts as publisher for recieving messages over the network, By providing subscribeForNotifications() method of the LanCommunicator object, And The Content and Processing module acts subscirbers. Subscribers subscribe to recieving messages by passing handler to subscribeForNotifications() method of LanCommunicator object. This handler must be the object of the class which implements INotificationHandler interface. So When the Networking Module receives the data, it would call the appropriate provided handler onMessageReceived method. The method onMessageReceived() present in that object does the needful, Which in turns indicates that the message is sent from networking module to other approriate module.  
    <br />
![](https://i.imgur.com/0HElJDr.png)

# Objectives
* ## ICommunicator Interface
    This Interface is description of overall top level functions that the Networking Module implements.
```java=
public interface ICommunicator {

    /**
    * This method used in starting communication by initializing all the threads needed for communication,
    * particularly the threads are SendQueueListener, SocketListener, ProcessingReceiveQueueListener 
    * and ContentReceiveQueueListener. 
    */
    void start();
    
    /**
    * This method will Stop the communication by terminating all threads initialized in the start method.
    */
    void stop();
    
    /**
    * This method helps in sending the data to the destination host by creating object(which consists of message and 
    * information about sender and receiver) and enqueuing the created object into the queue.
    *
    * @param destination contains IP and port of the destination host
    * @param message that needs to be sent.
    * @param identifier specifies the sender.
    */
    void send(String destination, String message, String identifier);
    
    /**
    * This method helps in subscribing the networking module for getting the data or messages(which are received over 
    * the network from source host) from the network module.
    * 
    * @param identifier specifies the receiver.
    * @param handler that handles the message received from networking module(handler should be provided by the processing and content modules)
    */
    void subscribeForNotifications(String identifier, INotificationHandler handler);

}
```
* ## INotificationHandler interface
    This INotificationHandler interface provided by networking module in the processing of implementing Publisher-Subscriber pattern.
    * This interface acts as notifier for subscribed modules.
    * This interface should be implemented by other modules who are in need for the messages received over the network through networking module.
    * When this interface is implemented in other modules and The object of that appropriate classes is provided through subscribeForNotifications, and these objects are the handlers. Then for every message received over network the onMessageReceived method is invoked through the handler provided and then needful is done, which shows the implementation of Publisher-Subscriber pattern.


```java=
package networking;

/**
* 

public interface INotificationHandler{
	
	/**
	* This method is invoked by networking module(particularly by ReceiveQueueListener) on receiving the message over network.
	* This method should do the processing or other needful of the message received over network.
	*
	* @param message The message received over network.
	*/
	public void onMessageReceived(String message);

```
* ## CommunicatorFactory Class
     - This is class used for creation of LanCommunicator object. This class has to follow Singleton Factory design pattern, for which the getCommunicator() method is designed in this class. The method getCommunicator() takes integer(which is free port number available on the client's pc) and returns LanCommunicator object of ICommunicator interface. This getCommunicator() method can be accessed before instantiation of the object of it's own class(CommunicatorFactory class).
    - For following Factory pattern getCommunicator() calls constructor of LanCommunicator classs and it creates an object and getCommunicator() method returns the created LanCommunicator object and thus object of LanCommunicator is initialised in this way.
    - For following Singleton pattern a LanCommunicator class variable is maintained in CommunicatorFactory class and logic is changed such a way that it checks for the stored class variable of CommunicatorFactory class and  getCommunicator() method returns the new or already existing object of the LanCommunicator, which helps in following Single Factory design pattern.
    - The method getClientInfo provides information about Private IP and free port available at the client. This method takes no arguments and returns ClientInfo object which consists of Private IP and free Port available. With the returned port number the LanCommunicator object is created. This method can be invoked many times without creating the object of the CommunicatorFactory class.

    <br />
```java=
public class CommunicatorFactory{
	
	/**
	* This method helps in creating the LanCommunicator object which uses Singleton and factory design pattern in 
	* creating LanCommunicator object.
	*
	* @param port is a free port number available at client.
	*
	* @return LanCommunicator(which implements ICommunicator interface) object.
	*/
	public static ICommunicator getCommunicator(int port){
		return new LanCommunicator();
	}

	/**
	* This method provides information about private IP address of the client and free port available at client without 
	* creating the object of CommunicatorFactory class.
	*
	* @return ClientInfo object which consists of local or private IP address and free port number at client.
	*/
	public static ClientInfo getClientInfo(){
		return new ClientInfo();
	}
}
```
    
* ## ClientInfo Class
    This is utility class which is used to deliver the Private IP address of client and free port number available at the client. These IP and port number can accessed using appropriate get methods.
```java=
public class ClientInfo{

	/**
	* @return String which is client's private IP address.
	*/
	public String getIp(){
		return "";
	}

	/**
	* @return int which is free port number on client's port number. 
	*/
	public int getPort(){
		return 0;
	}
}
```
* ## subscribeForNotifications method
    &ensp; This function is defined in ICommunicator Interface and implemented in LanCommunicator class. This method used to implement Publisher-Subscriber pattern. This method takes two arguments Identifier and handler and returns nothing. Based on the Identifier the provided handler gets the data accordingly. The handler is the object of the class which implements INotificationHandler interface. These classes should be implemented in needful module package and should be accessible to networking package. 
# Class Diagram
Below shows the class diagram for the ICommunicator Interface, LanCommunicator class and CommunicatorFactory class and dependies involved with other modules.

![](https://i.imgur.com/EOeKYGn.png)


## Package
There are various teams delivering respective packages in this project. Our team networking delivers the package named networking.
## Coding Style:
We are going to follow [google’s java style guide](https://google.github.io/styleguide/javaguide.html).



