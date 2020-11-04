
# ReceivingQueueListener
## INTRODUCTION

This task consists of listening/poll data from the receivingQueues , dequeue the data, and call the handler of the module ( Processor Module or Content Module) of the corresponding message. Processing Queue and Chat Message Queue are the queues that store the incoming message.

Whenever the messages reach in the receiving queue, it will send to the corresponding modules.


Here the Networking module is the publisher and the Content and Processing modules are the Subscribers. So Content and Processing Modules will be subscribing to the data from the Network Module. When the Networking Module receives the data, it would call the handler provided as an argument.


![](https://i.imgur.com/el9jcMV.png)


**1.** Continuously poll on the Receiving Queues
**2.** Dequeue the message string from the queue
**3.** Call the corresponding message's Module handler 

## DESIGN CHOICES


Two queues are used in this task. Processing Queue stores the information to be sent to the Processor Module and Chat Message Queue stores the messages to be sent to the Content Module. Using one receiving queue will delay the delivery of chat messages ( Content Module messages) delivery since the processing of UI (Processing Module ) messages takes more time. Using two different queues for Content as well as Processing Module will solve this issue effectively.

**Publisher -Subscriber** is a messaging pattern where senders of messages, called publishers, do not program the messages to be sent directly to specific receivers, called subscribers, but instead categorize published messages into classes without knowledge of which subscribers if any, there may be. Similarly, subscribers express interest in one or more classes and only receive messages that are of interest, without knowledge of which publishers, if any, there are. Here Network Module acts as a publisher. Processing and Content module act as a subscriber pattern.

![](https://i.imgur.com/lFJiGXC.png)




## Interface

```{java}
//This interface needs to implemented by the processing and content module
public interface INotificationHandler {
    void onMessageReceived(String message);    
}

```

When the network module receives the message, the handler is called by passing the message. The content and Processing module will subscribe to the data from the Network module. When the data arrives in the receiving queue, ```onMessageReceived```  is called for the corresponding module handler.

## CLASS DIAGRAM



![](https://i.imgur.com/SXMD5FD.png)


Queue ( Processing queue or ChatMessage queue )and corresponding ```INotificationHandler``` object are passed to the ReceiveQueueListener class. This will prevent the implementation of two classes with the same logic. Hence the ReceiveQueueListener runs in a thread, so it has to be implemented using the runnable interface. In the start function of the Networking Module, two threads will be instantiated for the Processing module and Content Module respectively. 

The class diagram methods are not mentioned exactly as it's going to be implementation-specific. But dependency on the other classes is mentioned.

## ACTIVITY DIAGRAM
![](https://i.imgur.com/CVbMtur.png)

## CODING STYLE
We are going to follow [google’s java style guide](https://google.github.io/styleguide/javaguide.html).