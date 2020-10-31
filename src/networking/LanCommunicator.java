package networking;
/**
 *
 * @author Madaka Srikar Reddy
 */

class LanCommunicator implements ICommunicator{

    /**
     * This method is used for initializing the queues, 
     * starting worker threads of sendQueueListener, socketListener, 
     * processingReceiveQueueListener and contentReceiveQueueListener 
     */
    public void start(){
        return;
    }
    
    /**
     * This method will help to terminate all the threads
     * initialized in the start method
     */
    public void stop(){
        return;
    }
    
    /**
     * This method creates a object with the params
     * and enqueues this object into the sendQueue
     * @param destination contains the ip and port
     * @param message that needs to be sent
     * @param identifier specifies who want to send it
     */
    public void send(String destination, String message, String identifier){
        return;
    }
    
    /**
     * This method takes identifier and handler
     * and maps them so that they can be passed to the ReceiveQueueListeners
     * @param identifier
     * @param handler
     */
    public void subscribeForNotifications(String identifier, INotificationHandler handler){
        return;
    }
    
}
