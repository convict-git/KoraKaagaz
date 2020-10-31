package networking;
/**
 * This file contains the class LanCommunicator which is the object that other modules
 * use to start the communication , send the messages across the network and subscribe
 * for messages that they should be receiving.
 *
 * @author Madaka Srikar Reddy
 */


import java.util.HashMap;
import networking.queueManagement.*;
import networking.utility.*;
import infrastructure.validation.logger.*;

public class LanCommunicator implements ICommunicator{

    /** SendQueueListener that will be listening on the send Queue */
    SendQueueListener sendQueueListener;

    /** NetworkListener that will be listening on the network */
    SocketListener socketListener;

    /** ProcessingReceiveQueueListener that will be listening on the Processing Receive Queue */
    ReceiveQueueListener processingReceiveQueueListener;

    /** ContentReceiveQueueListener that will be listening on the Content Receive Queue */
    ReceiveQueueListener contentReceiveQueueListener;

    /** Send Queue that has the message that should be sent across the network */
    IQueue<OutgoingPacket> sendQueue;

    /** Receive Queue in which we have the messages that needs to be sent to the processing module */
    IQueue<IncomingPacket> processingReceiveQueue;

    /** Receive queue in which we have the messages that needs to be sent to the content module */
    IQueue<IncomingPacket> contentReceiveQueue;

    /** Hashmap contains the handlers for the respective modules processing and content */
    HashMap<String, INotificationHandler> handlerMap;
    
    /** Port Number that we will be listening on the network */
    int portNumber;
    
    /** Boolean check variable for listeners to use */
    static boolean isRunning = false;
    
    /** Logger instance for logging errors and activities */
    ILogger logger;

    /**
     * constructor that takes port and also initializes a hashmap
     * 
     * @param port that we will be passing to the socketListener
     */
    public LanCommunicator(int port) {
    	portNumber = port;
        handlerMap = new java.util.HashMap<>();
    }

    /**
     * This method is used for initializing the queues, 
     * starting worker threads of sendQueueListener, socketListener, 
     * processingReceiveQueueListener and contentReceiveQueueListener 
     */
    @Override
    public void start(){
    	if(!isRunning) {
    		
    		isRunning = true;
	    	/** 
	    	 * Initializing the queues required for the networking module
	    	 * Send Queue - 1 with OutgoingPacket object in it.
	    	 * Receive queue - 2 (processing and content) with IncomingPacket objects in it. 
	    	 */
	        sendQueue = new ConcurrentBlockingQueue<OutgoingPacket>();
	        processingReceiveQueue = new ConcurrentBlockingQueue<IncomingPacket>();
	        contentReceiveQueue = new ConcurrentBlockingQueue<IncomingPacket>();
	        
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "1 sendQueue and 2 receive queues created");
	
	        /** 
	         * The listener which listens on the send queue and transfer the messages on the lan network to the destination IP
	         * A thread is spawn to perform this continuously whenever we have packets in the queue 
	         */
	        sendQueueListener = new SendQueueListener(sendQueue);
	        sendQueueListenerWorker = new Thread(sendQueueListener);
	        sendQueueListenerWorker.start();
	        
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "sendQueueListener thread started");
	        
	        /** 
	         * The listener that will be listening on the network and that receives the packet sent by the sendQueueListener
	         * This listener will distingush between processing module message and content module's message and push into their respective queues 
	         */
	        socketListener = new SocketListener(processingReceiveQueue, contentReceiveQueue, portNumber);
	        socketListenerWorker = new Thread(socketListener);
	        socketListenerWorker.start();
	
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "socketListener thread started");
	        
	        /** 
	         * This listener will be listening on the receive queue which is for the processing modules message
	         * It will send the message which is pushed by network listener through the processing module handler 
	         */
	        processingReceiveQueueListener = new ReceiveQueueListener(processingReceiveQueue, handlerMap);
	        processingReceiveQueueListenerWorker = new Thread();
	        processingReceiveQueueListenerWorker.start();
	        
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "processingReceiveQueueListener thread started");
	        
	        /** 
	         * This listener will be listening on the receive queue which is for the content modules message
	         * It will send the message which is pushed by network listener through the content module handler 
	         */
	        contentReceiveQueueListener = new ReceiveQueueListener(contentReceiveQueue, handlerMap);
	        contentReceiveQueueListenerWorker = new Thread(contentReceiveQueueListener);
	        contentReceiveQueueListenerWorker.start();
	        
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "contentReceiveQueueListener thread started");
	        logger.log(ModuleID.NETWORKING, logLevel.INFO, "Communication is started");
	    
    	}
    }

    /**
     * This method will help to terminate all the threads
     * initialized in the start method by setting isRunning to false
     */
    @Override
    public void stop(){
    	isRunning = false;
    	logger.log(ModuleID.NETWORKING, logLevel.INFO, "Communication is stoped");
    }

    /**
     * This method creates a object with the params
     * and enqueues this object into the sendQueue
     * 
     * @param destination contains the ip and port
     * @param message that needs to be sent
     * @param identifier specifies who want to send it
     */
    @Override
    public void send(String destination, String message, String identifier){
    	/** Creating the object for the outgoing packet that is being pushed into the send queue. */
        OutgoingPacket packet = new OutgoingPacket(destination, message, identifier);
        try {
        	sendQueue.enqueue(packet);
        } catch(Exception exception) {
        	logger.log(ModuleID.NETWORKING, logLevel.ERROR, "Unable to enqueue the outgoing Packet into send queue");
        }
    }

    /**
     * This method takes identifier and handler
     * and maps them so that they can be passed to the ReceiveQueueListeners
     * 
     * @param identifier
     * @param handler
     */
    @Override
    public void subscribeForNotifications(String identifier, INotificationHandler handler){
    	try {
    		handlerMap.put(identifier, handler);
    	} catch(Exception exception) {
    		logger.log(ModuleID.NETWORKING, logLevel.ERROR, "Unable to put the handler into the queue : " + exception);
    	}
    }

}