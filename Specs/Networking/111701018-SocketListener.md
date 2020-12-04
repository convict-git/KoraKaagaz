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

### Advantages of Sockets
* Sockets are bidirectional i.e., read and writes can be done on a single descriptor.
* Time Constraint and Familiarity with Socket Programming.  


## Protocol : TCP
* It is a communication protocol. Connection-orientation means that the communicating devices should establish a connection before transmitting data and should close the connection after transmitting the data.
* TCP provides extensive error checking mechanisms. It is because it provides flow control and acknowledgment of data.
* It is slower than that of UDP but more reliable.

## Fragmentation
Fragmentation occurs when the message is too big to be sent over the network through the one packet. As we are using TCP we can only send 65,535 bytes in a single packet. So, if the information that need to be sent is more than the threshold we divide the message into chunks that can be sent over the network using TCP.

## Flow
1. Create a socket that listens for client requests on a port.
2. Whenever there is a request, It accepts( blocking manner ) them and receives the message.
3. If the message is fragmented, We combine all the fragments to form the complete message.
4. Then identify the message received based on the identifier.
5. Push the message into either processing module or content module queue based on the identifier.
![](https://i.imgur.com/m5SkPbi.png)

## Class Diagram
![](https://i.imgur.com/jRXM7TC.jpg)


## Activity Diagram( Data Incoming )
![](https://i.imgur.com/OZIX55l.png)

## Coding Style
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) will be followed.