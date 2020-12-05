# Implemention of LanCommunicator ( Start, Stop, Send) , InternetCommunicator and ServerManager

### Srikar Reddy Madaka (111701017)

## Overview

In this module, we will be implementing LanCommunicator ( Start , Stop , Send ) , InternetCommunicator and ServerManager

* **LanCommunicator** : This class implements the ICommunicator Interface Start, Stop and Send Methods which is based on the communication between the host through the Local Area Network ( LAN ).
* **InternetCommunicator** : This class also implements the ICommunicator Interface methods but the communication between the hosts is through the Internet where a server on the internet act as an intermediate to send the messages.
* **ServerManager** : This acts an intermediate which runs on Internet Server, between the hosts in way of communication through internet.


## DESIGN

* ### Publisher-Subscriber Pattern
    **Publishers** are the entities who create/publish a message on a topic. **Subscribers** are the entities who subscribe to messages on a topic. In a topic based Publish-Subscribe pattern, Publishers tag each message with the a topic instead of referencing specific Subscribers. Messaging system then sends the message to all Subscribers who have asked to receive messages on that topic. Publishers only concern themselves with creating the original message and can leave the task of servicing the Subscribers to the messaging infrastructure (this is where pattern comes into picture). So here the Networking module acts as publisher for recieving messages over the network, By providing SubscribeForNotifications() method of the LanCommunicator object and InternetCommunicator object, And The Content and Processing module acts subscirbers. Subscribers subscribe to recieving messages by passing handler to subscribeForNotifications() method of LanCommunicator or InternetCommunicator object. This handler must be the object of the class which implements INotificationHandler interface. So When the Networking Module receives the data, it would call the appropriate provided handler onMessageReceived method. The method onMessageReceived() present in that object does the needful, Which in turns indicates that the message is sent from networking module to other approriate module.
    [![](https://i.imgur.com/oealj4j.png)
](https://)
    

## IMPLEMENTATION

Both LanCommunicator and InternetCommunicator are the implementations of the ICommunicator Interface.

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

* ## LanCommunicator

#### START

In this method we will be starting the communication and initialize all the threads in the networking module.

These threads are initialized for starting the communication :
* **Thread for Send Queue Listener** which takes the message from the queue and send it over the network.
* **Thread for Socket Listener** which listens on the network and pushes the message based on its identifier into their respective queues.
* **Thread for Receive Queue Listeners**
    * **For Processing Module** which takes message from processing receive queue and sends to the processing module.
    * **For Content module** which takes message from content receive queue and sends to the content module.

**Map data structure** is used for storing the notification handlers.

At the start of the application we will be calling this method.


#### STOP

In this method we will be closing the communication and stop all the threads that we initialized in the start method. Stop method will be implemented by the listeners for doing this.

At the time of closing the application we will be calling this method.


#### SEND

In this method we will take the message from the processing module and communication module and pass it into a Send Queue which is a locking queue.

* Processing module and Content module calls the send method with arguments desination containing IP address, port number; message and the identifier which specifies that the method is called by the processing module or the content module.
* Then the message is pushed into the send queue after converting the message with a particular structure structure which is implemented by the Queue module.

The send Queue will be concurrent queue as we should not mutate the state of the queue at the same time and provide inconsitencies.

These messages in the queue will be sent over the network by the [Send Queue Listener Thread](https://) which dequeues message from the queue and send it to the destination IP.



* ## InternetCommunicator

Most part coincides with the LanCommunicator implementation. The main difference is that the messages are sent through the internet instead of Local Area Network.

#### START

In this method we will be starting the communication and initialize all the threads in the networking module. We also create a socket with the intermediate server on the internet and create dataInputStream and dataOutputStream for reading and writing through this socket.

These threads are initialized for starting the communication :
* **Thread for Internet Send Queue Listener** which takes the message from the queue and send it to the intermediate server on the internet with the dataOutputStream given to it while intializing it.
* **Thread for Client Message Receiver** which listens on the internet through the dataInputStream and pushes the message based on its identifier into their respective queues.
* **Thread for Receive Queue Listeners**
    * **For Processing Module** which takes message from processing receive queue and sends to the processing module.
    * **For Content module** which takes message from content receive queue and sends to the content module.


#### STOP

In this method we will be closing the communication and stop all the threads that we initialized in the start method. Stop method will be implemented by the listeners for doing this. We also close the socket which is created in the Start method by the InternetCommunicator Class.


#### SEND

This method will be same as the LanCommunicator implementation.

#### SUBSCRIBEFORNOTIFICATIONS

In this method we implement the publisher-subscriber pattern. This method takes two arguments handler and an identifier. Other modules use this for subscribing to the messages received by them through the networking module. We do this by calling the handler with the respective message received by them through receiveQueueListeners.

* ## ServerManager

ServerManager will be running on the server and used for checking if client is connected to internet, to get id for a client and to create two threads (for each client) for listening the messages from a client and another for sending it back.

```java=
public class ServerManager {
    
    /**
     * This is the main method that will be running in the server that is used as 
     * intermediate between clients
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        while(1){
            if(msg == "CHECK_INTERNET"){
                // Will send back a message denoting that it has internet
            }
            else if(msg == "GET_CLIENT_INFO"){
                // Will send a cliend Id specific to the client
            }
            else if(msg == "clientId:CONNECT"){
                // Creates a queue for this client and 
                // will spawn two threads for 
                // receiving and sending message to clients
            }
        }
    }
    
}
```

The two threads that will be spawned are :

* **ReceiveThread** : This will take the message from the client and puts the message into the respective destination client queue.
* **SendThread** : This will listen to its only client queue and sends the messages to the client if there are any messages present in its queue.


## CLASS DIAGRAM

![](https://i.imgur.com/21EU8Yk.png)

## Coding Style

We are going to follow [google’s java style guide](https://google.github.io/styleguide/javaguide.html).