package networking;
/**
 *
 * @author Madaka Srikar Reddy
 */


import java.util.HashMap;
import networking.queueManagement.*;
import networking.utility.*;

public class LanCommunicator implements ICommunicator{

    // SendQueueListener class that will be listening on the send Queue
    SendQueueListener sendQueueListener;

    // NetworkListener class that will be listening for incoming messages and inserts into the respective queues ( processing and content )
    SocketListener socketListener;

    // ProcessingReceiveQueueListener that will be listening on the Processing Receive Queue and calls the processing handler with the message in the queue
    ReceiveQueueListener processingReceiveQueueListener;

    // ContentReceiveQueueListener that will be listening on the content receive queue and calls the content handler with the message in the queue
    ReceiveQueueListener contentReceiveQueueListener;
    
    static boolean isRunning = false;

    // Send Queue that has the message that should be sent across the network
    static IQueue<OutgoingPacket> sendQueue;

    // The Receive Queue in which we have the messages that needs to be sent to the processing module
    static IQueue<IncomingPacket> processingReceiveQueue;

    // The receive queue in which we have the messages that needs to be sent to the content module
    static IQueue<IncomingPacket> contentReceiveQueue;

    // this hashmap contains the handlers for the respective modules processing and content
    static HashMap<String, INotificationHandler> handlerMap;

    // constructor
    public LanCommunicator() {
        handlerMap = new java.util.HashMap<>();
    }

    // will be initializing all the threads and starting them
    public void start(){
    	if(!isRunning) {
	    	// Initializing the queues required for the networking module
	    	// Send Queue - 1 with OutgoingPacket object in it.
	    	// Receive queue - 2 (processing and content) with incoming objects in it.
	        sendQueue = new ConcurrentBlockingQueue<OutgoingPacket>();
	        processingReceiveQueue = new ConcurrentBlockingQueue<IncomingPacket>();
	        contentReceiveQueue = new ConcurrentBlockingQueue<IncomingPacket>();
	
	        // The listener which listens on the send queue and transfer the messages on the lan network to the destination IP
	        // A thread is spawn to perform this continuously whenever we have packets in the queue
	        sendQueueListener = new SendQueueListener();
	        sendQueueListenerWorker = new Thread(sendQueueListener);
	        sendQueueListenerWorker.start();
	        
	        // The listener that will be listening on the network and that receives the packet sent by the sendQueueListener
	        // This listener will distingush between processing module message and content module's message and push into their respective queues
	        socketListener = new SocketListener();
	        socketListenerWorker = new Thread(socketListener);
	        socketListenerWorker.start();
	
	        // This listener will be listening on the receive queue which is for the processing modules message
	        // It will send the message which is pushed by network listener through the processing module handler 
	        processingReceiveQueueListener = new ReceiveQueueListener("processor");
	        processingReceiveQueueListenerWorker = new Thread();
	        processingReceiveQueueListenerWorker.start();
	        
	        // This listener will be listening on the receive queue which is for the content modules message
	        // It will send the message which is pushed by network listener through the content module handler 
	        contentReceiveQueueListener = new ReceiveQueueListener("content");
	        contentReceiveQueueListenerWorker = new Thread(contentReceiveQueueListener);
	        contentReceiveQueueListenerWorker.start();
	        
	        isRunning = true;
    	}
    }

    // WIll be destroying all the threads and stops the communication
    public void stop(){
    	isRunning = false;
    }

    public void send(String destination, String message, String identifier){
    	// Creating the object for the outgoing packet that is being pushed into the send queue.
        OutgoingPacket packet = new OutgoingPacket(destination, message, identifier);
        sendQueue.enqueue(packet);
        return;
    }

    public void subscribeForNotifications(String identifier, INotificationHandler handler){
        handlerMap.put(identifier, handler);
    }

}