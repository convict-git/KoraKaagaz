package networking;
/**
 * This file contains the class LanCommunicator which is the object that other modules
 * use to start the communication , send the messages across the network and subscribe
 * for messages that they should be receiving.
 *
 * @author Madaka Srikar Reddy
 * 
 * @author Prudhvi Vardhan Reddy Pulagam  for subscribeForNotifications method
 */


import java.util.HashMap;
import networking.queueManagement.*;
import networking.utility.*;
import infrastructure.validation.logger.*;

public class LanCommunicator implements ICommunicator {

    /** SendQueueListener that will be listening on the send Queue */
    private SendQueueListener sendQueueListener;
    private Thread sendQueueListenerWorker;

    /** NetworkListener that will be listening on the network */
    private SocketListener socketListener;
    private Thread socketListenerWorker;

    /** ProcessingReceiveQueueListener that will be listening on the Processing Receive Queue */
    private ReceiveQueueListener processingReceiveQueueListener;
    private Thread processingReceiveQueueListenerWorker;

    /** ContentReceiveQueueListener that will be listening on the Content Receive Queue */
    private ReceiveQueueListener contentReceiveQueueListener;
    private Thread contentReceiveQueueListenerWorker;

    /** Send Queue that has the message that should be sent across the network */
    private IQueue < OutgoingPacket > sendQueue;

    /** Receive Queue in which we have the messages that needs to be sent to the processing module */
    private IQueue < IncomingPacket > processingReceiveQueue;

    /** Receive queue in which we have the messages that needs to be sent to the content module */
    private IQueue < IncomingPacket > contentReceiveQueue;

    /** Hashmap contains the handlers for the respective modules processing and content */
    private HashMap < String, INotificationHandler > handlerMap;

    /** Port Number that we will be listening on the network */
    private int portNumber;

    /** Boolean check variable for to check whether program is running */
    private boolean isRunning = false;

    /** Logger for logging errors and activities */
    ILogger logger = LoggerFactory.getLoggerInstance();

    /**
     * constructor that takes port and also initializes a hashmap
     * 
     * @param port that we will be passing to the socketListener
     */
    public LanCommunicator(int port) {
        portNumber = port;
        handlerMap = new HashMap < > ();
    }

    /**
     * This method is used to set the status
     * 
     * @param status
     */
    private void setStatus(boolean status) {
        isRunning = status;
    }

    /**
     * This method is used by threads for getting the status
     * 
     * @return isRunning
     */
    private boolean getStatus() {
        return isRunning;
    }

    /**
     * This method is used for initializing the queues, 
     * starting worker threads of sendQueueListener, socketListener, 
     * processingReceiveQueueListener and contentReceiveQueueListener 
     */
    @Override
    public void start() {
        if (!getStatus()) {

            setStatus(true);
            /** 
             * Initializing the queues required for the networking module
             * Send Queue - 1 with OutgoingPacket object in it.
             * Receive queue - 2 (processing and content) with IncomingPacket objects in it. 
             */
            sendQueue = new ConcurrentBlockingQueue < OutgoingPacket > ();
            processingReceiveQueue = new ConcurrentBlockingQueue < IncomingPacket > ();
            contentReceiveQueue = new ConcurrentBlockingQueue < IncomingPacket > ();

            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "1 sendQueue and 2 receive queues created"
            );

            /** 
             * The listener which listens on the send queue and transfer the messages on the lan network to the destination IP
             * A thread is spawn to perform this continuously whenever we have packets in the queue 
             */
            sendQueueListener = new SendQueueListener(sendQueue);
            sendQueueListenerWorker = new Thread(sendQueueListener);
            try {
                sendQueueListenerWorker.start();
            } catch (Exception e) {
                logger.log(
                    ModuleID.NETWORKING, 
                    LogLevel.ERROR, 
                    "sendQueueListenerWorker is not able to start " + e.toString()
                );
                return;
            }

            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "sendQueueListener thread started"
            );

            /** 
             * The listener that will be listening on the network and that receives the packet sent by the sendQueueListener
             * This listener will distingush between processing module message and content module's message and push into their respective queues 
             */
            socketListener = new SocketListener(portNumber, processingReceiveQueue, contentReceiveQueue);
            socketListenerWorker = new Thread(socketListener);
            try {
                socketListenerWorker.start();
            } catch (Exception e) {
                logger.log(
                    ModuleID.NETWORKING, 
                    LogLevel.ERROR, 
                    "socketListenerWorker is not able to start " + e.toString()
                );
                return;
            }

            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "socketListener thread started"
            );

            /** 
             * This listener will be listening on the receive queue which is for the processing modules message
             * It will send the message which is pushed by network listener through the processing module handler 
             */
            processingReceiveQueueListener = new ReceiveQueueListener(processingReceiveQueue, handlerMap);
            processingReceiveQueueListenerWorker = new Thread(processingReceiveQueueListener);

            try {
                processingReceiveQueueListenerWorker.start();
            } catch (Exception e) {
                logger.log(
                    ModuleID.NETWORKING, 
                    LogLevel.ERROR, 
                    "processingReceiveQueueListenerWorker is not able to start " + e.toString()
                );
                return;
            }

            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "processingReceiveQueueListener thread started"
            );

            /** 
             * This listener will be listening on the receive queue which is for the content modules message
             * It will send the message which is pushed by network listener through the content module handler 
             */
            contentReceiveQueueListener = new ReceiveQueueListener(contentReceiveQueue, handlerMap);
            contentReceiveQueueListenerWorker = new Thread(contentReceiveQueueListener);
            try {
                contentReceiveQueueListenerWorker.start();
            } catch (Exception e) {
                logger.log(
                    ModuleID.NETWORKING, 
                    LogLevel.ERROR, 
                    "contentReceiveQueueListener is not able to start"
                );
                return;
            }

            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "contentReceiveQueueListener thread started"
            );
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "Communication is started"
            );

        }
    }

    /**
     * This method will help to terminate all the threads
     * initialized in the start method by setting isRunning to false
     * and also changing all the instances to null
     */
    @Override
    public void stop() {
        if(getStatus()){
            setStatus(false);
            sendQueueListener.stop();
            socketListener.stop();
            processingReceiveQueueListener.stop();
            contentReceiveQueueListener.stop();
            sendQueueListener = null;
            sendQueueListenerWorker = null;
            socketListener = null;
            socketListenerWorker = null;
            processingReceiveQueueListener = null;
            processingReceiveQueueListenerWorker = null;
            contentReceiveQueueListener = null;
            contentReceiveQueueListenerWorker = null;
            sendQueue = null;
            processingReceiveQueue = null;
            contentReceiveQueue = null;
            handlerMap = null;
            CommunicatorFactory.freeCommunicator();
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.INFO, 
                "Communication is stopped"
            );
        }
    }


    /**
     * This method creates a object with the params
     * and enqueues this object into the sendQueue
     * @param destination contains the ip and port
     * @param message that needs to be sent
     * @param identifier specifies who want to send it
     */
    @Override
    public void send(String destination, String message, String identifier) {
        /** Spliting the destination into ip and port */
        String[] dest = destination.split(":");
        /** checking if we have the destination in the required pattern ip:port */
        if (dest.length != 2 || dest[0] == "" || dest[1] == "") {
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.WARNING, 
                "Invalid destination : " + destination
            );
            return;
        }
        /** checking if identifier is empty */
        if (identifier == "") {
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.WARNING, 
                "Empty identifier"
            );
            return;
        }
        /** Creating the object for the outgoing packet that is being pushed into the send queue. */
        OutgoingPacket packet = new OutgoingPacket(destination, message, identifier);
        sendQueue.enqueue(packet);
        logger.log(
            ModuleID.NETWORKING, 
            LogLevel.INFO, 
            "Pushed the message into the send queue"
        );
    }

    /**
     * This method used to implement Publisher-Subscriber pattern. This method takes two arguments Identifier and handler. Based on the Identifier,
     * the provided handler gets the data accordingly. The handler is the object of the class which implements INotificationHandler interface.
     * Upon receiving the message through network, the networking module transfers the received message to subscribed module by invoking the onMessageReceived method
     * of the provided handler object.
     *
     * @param identifier specifies the module subscribing.
     *
     * @param handler should handle message transfered from networking module.
     */
    @Override
    public void subscribeForNotifications(String identifier, INotificationHandler handler) {

        /** validating the handler */
        if (handler == null)
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.WARNING, 
                "Provide a valid handler"
            );

        /** validating the identifier */
        else if (identifier == "" || identifier == null)
            logger.log(
                ModuleID.NETWORKING, 
                LogLevel.WARNING, 
                "Provide a valid identifier"
            );

        else {

            /** logging the info as the handler might be overridden if the specified identifier exists */
            if (handlerMap.containsKey(identifier))
                logger.log(
                    ModuleID.NETWORKING, 
                    LogLevel.INFO, 
                    "Already have the specified identifier"
                );

            /** inserting the specified identifier and handler into the hashmap */
            handlerMap.put(identifier, handler);
        }
    }
}
