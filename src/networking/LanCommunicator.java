package networking;
/**
 *
 * @author Madaka Srikar Reddy
 */

class LanCommunicator implements ICommunicator{

    /* This method is used for initializing the queues, 
     * starting worker threads of sendQueueListener, socketListener, 
     * processingReceiveQueueListener and contentReceiveQueueListener 
     */
    public void start(){
        return;
    }
    
    /* This method will help to terminate all the threads
     * initialized in the start method
     */
    public void stop(){
        return;
    }
    
    /* This method takes destination which contains ip and port,
     * message and the identifier then creates a object with this info
     * and enqueues this object into the sendQueue
     */
    public void send(String destination, String message, String identifier){
        return;
    }
    
    /* This method takes identifier and handler
     * It maps them so that they can be passed to the ReceiveQueueListeners
     */
    public void subscribeForNotifications(String identifier, INotificationHandler handler){
        return;
    }
    
}
