# Implemention of LanCommunicator ( Start, Stop, Send)

### Srikar Reddy Madaka (111701017)

## Introduction

In this module, we will be implementing the start, stop and send functions of the ICommunicator interface.

The start function is implemented for initializing all the threads in the networking module and other required data structures. In the stop function we will destroy the threads and the things that are initialized in the start function. In send function we will be enquing the message from processing module and the content module after adding headers to the data into the send queue which is a concurrent queue implemented by the [Queue Module](https://). And these messages are sent to the desination address over the network and we assume that our application is running on a LAN - Local Area Network for now (later through Internet).

``` java
public interface ICommunicator {
    #Start communication , initalize all the threads 
    void start();
    #Stop the Communication
    void stop();
    #Send the data to the destination IP
    void send(String destination, String message, String identifier);
    #Other functions
    ...
}
```
## START

In this function we will be starting the communication and initialize all the threads in the networking module.

These threads are initialized for starting the communication :
* **Thread for Send Queue Listener** which takes the message from the queue and send it over the network.
* **Thread for Socket Listener** which listens on the network and pushes the message based on its identifier into their respective queues.
* **Thread for Receive Queue Listeners**
    * **For Processing Module** which takes message from processing receive queue and sends to the processing module.
    * **For Content module** which takes message from content receive queue and sends to the content module.

**Map data structure** is used for storing the notification handlers.

At the start of the application we will be calling this function.


## STOP

In this function we will be closing the communication and stop all the threads that we initialized in the start function.

At the time of closing the application we will be calling this function.


## SEND

In this function we will take the message from the processing module and communication module and pass it into a Send Queue which is a locking queue.

* Processing module and Content module calls the send function with arguments desination containing IP address, port number; message and the identifier which specifies that the function is called by the processing module or the content module.
* Then the message is pushed into the send queue after converting the message with a particular structure structure which is implemented by the Queue module.

The send Queue will be concurrent queue as we should not mutate the state of the queue at the same time and provide inconsitencies.

These messages in the queue will be sent over the network by the [Send Queue Listener Thread](https://) which dequeues message from the queue and send it to the destination IP.


![](https://i.imgur.com/yKfKkeK.png)

## Class Diagram

![](https://i.imgur.com/ZXtkD3C.png)



## Activity Diagram

![](https://i.imgur.com/Li4YEuS.png)

## Coding Style

We are going to follow [google’s java style guide](https://google.github.io/styleguide/javaguide.html).