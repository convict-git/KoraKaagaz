---
title: 'Data Incoming Spec'
---

# Data Incoming

### Marella Shiva Sai Teja(111701018)

## Introduction

The work of **Network Module** is basically communication, In our project as we need to transfer messages from one host to another we definitely need network module. 
When content or processing module needs to send data, they give it to network module providing the required details which in turn sends the data to respective IP addresses as provided.

For now this module will be implemented assuming the application will run over LAN.

## Task
Receive message from the sender and push the received message to respective queue either content module queue or processing module queue based on the identifier of the message.

## Communication : Socket vs RMI

### Socket
A socket represents one end of the connection between client and server.     
    ![](https://i.imgur.com/OQUOuAW.png)
Socket(): Function to create a socket.
Bind(): Bind function binds the socket to the address and port number specified
Listen(): It puts the server socket in a passive mode, where it waits for the client to approach the server to make a connection

Bind and Listen are only for server as client doesn't need to listen for any messages.

* A **listening socket** is used by a server process to wait for connections from remote clients.
    In Java, use ServerSocket to make a listening socket, and use its accept method to listen to it.   
* A **connected socket** can send and receive messages to and from the process on the other end of the connection. 
    It is identified by both the local IP address and port number plus the remote address and port, which allows a server to differentiate between concurrent connections from different IPs, or from the same IP on different remote ports.
    In Java, clients use a Socket constructor to establish a socket connection to a server. 
Servers obtain a connected socket as a Socket object returned from ServerSocket.accept

### RMI
Java Remote Method Invocation is a Java API that performs remote method invocation, the object-oriented equivalent of **Remote procedure calls**(RPC).

RPC is the service and protocol offered by the operating system to allow code to be triggered for running by a remote application. It has a defined protocol by which procedures or objects can be accessed by another device over a network. 
### Advantages of Sockets over RMI
* Sockets seem easier to control, where something like  could take a lot of time to learn.
* Efficient and Safe from Bugs.
* Time Constraint and Familiarity with Socket Programming.  


## Protocol : TCP
* It is a communication protocol, using which the data is transmitted between systems over the network.
* Data is transmitted in the form of packets.
* It includes error-checking, guarantees the delivery and preserves the order of the data packets.


## FLOW
1. Create a socket, to listen for incoming messages.
1. Bind the socket to a port and start listening.
1. Whenever a message is received, It will be in the form a packet which will have some headers as well as the required message.
1. Identify the message as either processing module or content module using the headers and push it to respective queue.
![](https://i.imgur.com/m5SkPbi.png)

## Class Diagram
![](https://i.imgur.com/jRXM7TC.jpg)


## Activity Diagram( Data Incoming )
![](https://i.imgur.com/OZIX55l.png)

## Coding Style
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) will be followed.