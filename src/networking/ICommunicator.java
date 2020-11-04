package networking;

/**
 * This file consists of information about ICommunicator interface provided by the networking module.
 * This interface consists of methods which are used in communicaton of the messages or data among
 * the users of our White Board application.
 *
 * @author Prudhvi Vardhan Reddy Pulagam
 */

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
