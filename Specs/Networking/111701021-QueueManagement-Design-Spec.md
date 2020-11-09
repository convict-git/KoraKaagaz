# Queue Management Design Spec

Author : Pandravisam Shiva Santhosh


## Main Tasks (In a NutShell)

- Define the Structure/Class that the Queue Data Structure will hold.
- Provide the concurrent access of the Queue to the threads. 
- Provide the interface of the standard Queue operations to other components in the Networking module and implement that operations efficiently. 

# Design Choices:

## Introduction

A Data Structure is needed to hold the messages that are sent by the Processing and the Content module and also to hold the messages that are recieved from the sockets. This Data Structure must also provide the functionalities of storing and retrieving data in a FIFO (First In First Out) manner. A Queue Data Structure is a natural choice for such requirement.  

## Concurrent Queues (Blocking Queue in Java)

Choosing Concurrent Queues is one of optimal choice beacuse in our application several threads concurrently tries to push the data to queue and we want the data to be inserted properly without any concurrency issues, and also there is a thread which always tries to push the data and a another thread which always tries to dequeue the data from the queue, both threads may access the queue at the same time and might get into deadlock which is also not desirable, So to handle such situations Concurrent Queues are used and they are very powerful.

Concurrent Queues also known as Blocking Queue in Java, represents a queue which is thread safe to put elements into, and take elements out of from. In other words, multiple threads can be inserting and taking elements concurrently from a Java BlockingQueue, without any concurrency issues arising.

A BlockingQueue is typically used to have one thread produce objects, which another thread consume. The producing thread will keep producing new objects and insert them into the BlockingQueue, until the queue reaches some upper bound on what it can contain. It's limit, in other words. If the blocking queue reaches its upper limit, the producing thread is blocked while trying to insert the new object. It remains blocked until a consuming thread takes an object out of the queue.

 ![](https://i.imgur.com/04acsaA.png)
A BlockingQueue with one thread putting into it, and another thread taking from it.
(ImageSource: http://tutorials.jenkov.com/java-util-concurrent/blockingqueue.html)

The consuming thread keeps taking objects out of the BlockingQueue to processes them. If the consuming thread tries to take an object out of an empty queue, the consuming thread is blocked until a producing thread puts an object into the queue. 

With such powerful features explained above , Concurrent Queues (Blocking Queue in Java) is definintely one of best choice to implement.   


# Structure of the Data that Queue holds

Queue acts as a bridge between any two components in the Networking Module. So, its the responsibily of the Queue Management Component to fix the structure of the Data that the Queue will hold for the mutual agreement of the components. In total they are 3 Queues needed for the Networking Module as per the design. There is one Sending Queue and two Receiving Queue.

Note that the Queue implementation is of generic type but while instantiating the queue, it should be instantiated with this structure

## Sending Queue

Interaction of the Queue is with the Send component(used by Processing and Content Module) and SendQueueListener Component.

![](https://i.imgur.com/yC3St6s.png)

Sending Queue will hold the objects of the the class OutgoingPacket. The OutgoingPacket class contains the following details,
- message string, this is the string that needs to be send across the network
- destination string, this is the string that will contain the IP address of the destination machine.
- identifier string, this is the string that is used in indentifing whether it is Processing Module and Content Module.

```Java
public class OutgoingPacket{
    String message;
    String destination;
    String identifier;
}
```

## Receiving Queue

Interaction of the Queue is with SocketListener Component and ReceieveQueueListener Component.
There are two Receiving Queues 
- One is for Processing Module, the messages that the Queue will hold is to delivered to the processing module
- Other is for Content Module, the messages that the Queue will hold is to delivered to the content module.

![](https://i.imgur.com/kw7sTgM.png)



Receiving Queue will hold the objects of the the class IncomingPacket. The IncomingPacket class contains the following details,
- message string, this is the string that needs to deliver to the corresponding module. It is also suffice to have that information in the queue.
- identifier string,this is the string that is used in indentifing whether it is Processing Module and Content Module.This is also needed for calling the interrupt handler, used in RecieveQueueListener Component.


```Java
public class IncomingPacket{
    String message;
    String identifier;
}
```

# Relationship with Other Components

Queue is highly used by various components in the Networking Module and acts as a buffer to store and retrieve messages in FIFO manner. The components that use Queue are
- **Send** Component mainly uses the **Sending Queue** to enqueue the message which is supposed to be delievered to the other user.
- **SendQueueListener** Component mainly uses the **Sending Queue** to dequeue the message which was enqueued by the Send Component.
- **SocketListener** Component enqueues the message that is received from the socket into the corresponding the **Receiving Queue** 
- **Receive Queue** Component dequeues the message from the corresponding **Receiving Queue** and delivers it to the corresponding module.

The Communicator Factory instantiates the instance of Queue objects

            
--------------------------------------**Dependency Diagram**--------------------------------------
![](https://i.imgur.com/yWLOdYG.png)



# Interface Provided to Other Components.

```Java
public interface IQueue< T >{

    void clear(); #deletes all the elements in the queue

    void Enqueue(T item); # enqueues the item into the queue. 
    
    void Dequeue();# dequeues/removes the front item of the queue.
    
    T Front(); # returns the front item of the queue, here T is the data type of the item.
    
    boolean IsEmpty(); # returns true, if the queue is empty or else it returns false. 
    
    int Size(); #returns the size of the Queue.
}
```


# UML Class Diagram 

![](https://i.imgur.com/IyF4M0j.png)

![](https://i.imgur.com/ffxsSVr.png)

![](https://i.imgur.com/2JxZ0Kq.png)

# Coding Style

We the Networking Module is going to follow : [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html)
