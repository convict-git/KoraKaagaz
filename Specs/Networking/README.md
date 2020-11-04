# Networking Module Design Spec
## Team Members:
1. Dasari Sravan Kumar(111701010) - Team Lead
2. Pandravisam Shiva Santhosh(111701021)
3. Sirpa Sahul (111701028)
4. Marella Shiva Sai Teja (111701018)
5. Madaka Srikar Reddy (111701017)
6. Pulagam Prudhvi Vardhan Reddy (111701023)
7. Drisya Ponamari (111701031)



## SubTasks:

- All the below components are required to log the data or errors using Logging module from the Infrastructure Team.

1. Queue Management - Shiva Santhosh
    - Define data type for queue
    - Provide concurrent access of the queue to the threads
    - Able to support enqueue and deque efficiently
2. Send() function ,Stop and Start Communication  - Srikar Reddy 
    - Add Headers to the data received from the other modules and push it into the send queue.
    - Also implement start() ,stop() in the LanCommunicator class.


3. sendQueueListener  - Sahul
    - Thread Listening on the send queues and send the data over the network

4. receiveQueueListener  - Drisya
    -   Thread Listening on the receiving queues and calls the  handlers of the the corresponding data. 

5. SocketListener -  Shiva Sai Teja 
    - Thread listening on socket and  pushes data to the corresponding receiving queue

6. CommunicatorFactory   - Prudhvi
    - Define an ICommunicator interface and create a communicatorFactory class following Singleton factory design pattern.
    - Write getPort() and subscribeForNotifications().

7. Testing and Integration - Dasari Sravan kumar
    - Write Unit tests and review the code written by team members.

## Dependency within the Module:
![](https://i.imgur.com/XXUaYgA.png)



## Design Choices:

### Protocol Used : TCP
#### TCP:
* It is a communications protocol, using which the data is transmitted between systems over the network.
* In this, the data is transmitted into the form of packets.
* It includes error-checking, guarantees the delivery and preserves the order of the data packets.
#### TCP vs UDP: 

| TCP      |   UDP    | 
| -------- | -------- |
| TCP provides error checking and guaranteed delivery     | UDP provide basic error checking and does not gurantee delivery     |
|    Maintain the order of the packets      |       Does not maintain the order of packets |
|  TCP is slower and less efficient in performance as compared to UDP.  |  UDP is faster and more efficient than TCP  |
| Retransmission is possible   |   Retransmission is not possible |


To maintain consistent state we found TCP as more reliable than udp as udp may often suffers packet loss and might lead to inconsistency in the  UI state. TCP also gurantees the order of packet which is required in the application.

### Communication Method: Sockets
#### Sockets vs Remote Method Invocation in java:
The Communication method we decided to use is socket programming. The reason to not use RMI is because of less familiarity and time constraint.

### Network Type: LAN 
All the functionalities of this module assumes that all the nodes are in the Same local area network.First,This module will be built on the LAN .Assuming this would be completed in less time, later this module will be extended to work over internet as well (If the resources are enough) .



#### Different methods to send data over the internet:
1. Using a Server hosted in the internet and establishing a two way TCP connection between the server and the client. 
    ##### Drawback:
    Routers and firewalls block incoming traffic from the ports other than 22,80 etc.

2. Form a virtual Prvate network of all the nodes on the application , in this all nodes will behave as if they are in the same local area network. In this case the implementation will be the same as LAN.
    #### Drawback:
    This is not a desirable solution in a real world  , where one was not expected to connect to a private network to access an application.
    
3. Setting up a peer to peer connection by a strategy called [NAT hole punching](https://bford.info/pub/net/p2pnat/)  .This is NAT traversal technique used by some peer to peer applications like Skype .This can be acheived by using STUN(Session Traversal of User Datagram Protocol) server.
    #### Drawbacks:
     -   This is complex to implement because of time constraint and less familiarity.
     -   This is not reliable in some types of NAT like Symmetric NAT,  where one would need to relay data of one client to other  using a  dedicatd server which is bit inefficient.
    
4. Using web sockets , web Sockets provide full duplex communication between the client and the server.The duplex connection is allowed because the communication happen on port 80, which is allowed by almost all firewalls.




### Data Handling: Message Queues 
 The reason to use Message Queues is, it decouples the producer and consumer. 

In this module we are required to use three concurrent message  queues , one queue for sending and the other two queues for receving.

The queues should be able to support concurrent access as we are using  one thread for pushing the data and other thread to consume the data, both threads may access the queue at the same time , might get into deadlock which is not desirable.This is a Producer-Consumer bounded buffer problem.
So in a ideal scenario, the producer thread should wait for the other thread to consume when the buffer is full. And also when the queue is empty  the consumer thread has to wait until any data is pushed into queue by the producer thread.
This can be handled by using concurrent queues (Blocking queues in Java).










## Design:

This Module is only going to Communicate with processing, Content and Logger Modules.


### Singleton Factory Pattern:
Networking Module will be providing a FactoryClass called ```CommunicatorFactory``` which provides a method called getCommunicator to get a Communicator object of ICommunicator Interface.


The reason to use factory pattern is that it provides loose coupling. Factory pattern encapsulate object creation logic which makes it easy to change it later when you change how object gets created or you can even introduce new object with just change in one class. Later when we add a feature to send data over Internet, We might have to have a seperate communicator Class ,Suppose the class name can be ```internetCommunicator```,but  the other modules need not be aware of these details, it would simply call ```getCommunicator(portNumber)```.

As there is only a single socket to send the data there should not be more than one instance of the Icommunicator object,So The Factory Class should create the Icommunicator object only once. So the Factory class should follow Singleton Creation design Pattern.

![](https://i.imgur.com/kbyA5Xf.png)


### Publisher - Subscriber Patern:
Here the **Networking module** will be the publisher and **Content and Processing** modules will be the Subscribers. So Content and Processing Modules will be subscribing the data from the Network Module. When these modules want to subscribe for the data then these module would have to implement ```INotificationHandler``` Interface. And after the creation of ```Communicator``` object , they would have to call ```subscribeForNotifications```method of the ```Communicator``` object by passing the ```INotificationHandler```object as an argument.
So when the Networking Module receives the data ,it would call the handler provided as an argument.

![](https://i.imgur.com/tGU0YDE.png)



## Flow


 ### Data Outgoing:
![](https://i.imgur.com/J7joRD7.png)

1. Initially a message come from either of the modules comes to the networking module via send function.
2. We will put some headers like data identifier (chat, UI) and the message id 
3. The data is pushed into the queue.
4. There will be a thread  listening on the other side of the queue which will dequeue the data from the queue.
5. The thread will send the data over the network.


### Data Incoming:

![](https://i.imgur.com/Wgc73by.png)


1) A socket will be listening on some port initially
2) when any data is received , we will identify the message based on the identifier
3) If the message is UI object message , then we will enqueue into the queue of UI message otherwise we will enqueue into the chat message queue
4) There will be two threads Listening on each queue which waits for the data in the queues.
5) If there is any data in the queues then these Listener threads will deque the data from the corresponding queue and call the handler of the module of the  corresponding message.


## Interfaces

```Java
public interface ICommunicator {
    #Start communication , initalize all the threads 
    void start();
    #Stop the Communication ,
    void stop();
    #Send the data to the destination IP
    void send(String destination, String message, String identifier);
    #Subscribe the module for receiving the data from the network module
    void subscribeForNotifications(String identifier, INotificationHandler handler);
   
}

```


```Java
//This interface needs to implemented by the processing and content module
public interface INotificationHandler {
    void onMessageReceived(String message);    
}

```


## Class Diagram:
![](https://i.imgur.com/uojs9Bd.png)


## Activity Diagrams:

### Data Incoming:
![](https://i.imgur.com/VLy0HX1.png)


### Data Outgoing:
![](https://i.imgur.com/iEnJwL8.png)
