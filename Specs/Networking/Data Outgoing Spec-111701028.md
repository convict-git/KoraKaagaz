---
title: 'Data Outgoing Spec'
---
# Data Outgoing (Network Module)

### Sirpa Sahul (111701028)

## Introduction
This sub-module is part of the networking module and it will implement the data outgoing logic.

The main work of this sub-module is to listen/poll data from the sendQueue, then dequeue the data from the queue and then validate the destination IP address and the port number and identifier. Once it is validated then create a client socket and connect to the server and then send data over the network.

Whenever another module(content/processing) want's to send the data then they will call the send function which will add enqueue the data into the SendQueue.

This sub-module will be implemented by assuming the application will run over **LAN** (Local Area Network).

## Flow
The working flow of the sub-module is explained below.
1. Continuously poll on the sendQueue
1. Then take the message from the queue and then dequeue it.
1. Then validate the destination IP address and port and the identifier.
1. Now create the client socket and then connect to the server using the server IP address and port number.
1. Encode the message and Identifier together as a string and then send it over the network using TCP.
1. Close the socket and continue to step 1.



![](https://i.imgur.com/apIEOk3.jpg)


## Relation to Other Modules
The Send module is not interacting with any other modules. It's interacting only with sendQueue and which is part of the networking module itself. So all the interactions are internal. It depends on the sendQueue as it will take the sending data/messages from the queue.

## Design Choices

We are going to use sockets and the transport protocol that we are going to use is TCP and fragmentation is not implemented. The reasons for the design choices are given below.

* ### Transport Protocol.
    In the transport layer, we can use either use Transmission Control Protocol(TCP) and User Datagram Protocol(UDP).
    * Messages from either content module or processing module can not be lost in the network so we need TCP for it.
    * Compared to TCP, UDP is faster and more efficient but as this application doesn't contain more than 30 participants at a time so we can tradeoff efficiency over reliability.

* ### Sockets.
    We are using Sockets over RPC. And we are establishing the connection between the client and server using sockets then sending the data using TCP protocol through sockets. We are not using remote procedure calls because to use remote procedure calls anyway we need to call the procedure which is on the remote machine and we need to pass the arguments to the remote machine which is exactly we are doing using sockets for. Here the parameters are more critical then procedures/functions. So that's why we are using sockets over RPC.
    
    Sockets are easier to implement and safe from bugs, and it is easy to understand. For implementing the client side of sockets I am going to use sockets provided by <code>java.net</code> library.
* ### Fragmenatation
    Fragmentation occurs when the message is too big to be sent over the network through the one packet. If a message is big then we will divide it into chunks then we will send it one by one and we need a mechanism to distinguish the end of the message. In the Networking module, we are not implementing the fragmentation because of the following analysis.
    
    In one TCP packet, we can send 65,535 bytes (65KB).In a worst-case lot of data is transferred when a new user joins the Whiteboard, then we need to send the full screen. So in this case suppose the Whiteboard application is running for 60 minutes and 10 members were already present in and assuming averagely 10 objects drawn in every minute so it turnout we have to send 6000 objects ( 10 * 30 * 10) and assuming each object takes around 100 bytes then we have to send 60000 bytes which is possible as we can send around 65000 bytes.
    
## Class Diagram
This sub-module is not using any other modules and it's not being used by any other modules. It will only use a blocking queue (sendQueue), but it is not instantiated by this sub-module. The only logic that is going to be implemented in this sub-module is just for sending the data over the network through sockets. So the class diagram contains only two classes out of it send class will be an implementation of a runnable interface and it will contain the logic of this sub-module and it also extensively uses sendQueue so we included it in the class diagram.

![](https://i.imgur.com/RJ7GCTj.jpg)

## Implementation Details
The main task of this sub-module is to take data from the sendQueue and then send it over the network.
This sub-module runs like a thread so, it needs to implementation of the runnable interface(This is specific to the java programming language.).
The sub-module logic is implemented in the run function(public void run() provided by runnable interface). It can contain some helper functions which can be useful for the implementation of the submodule(example: for validation of Ip address and port number).

## Activity Diagram

![](https://i.imgur.com/iEnJwL8.png/)

## Coding Style

We are going to follow [google's java style guide](https://google.github.io/styleguide/javaguide.html).
