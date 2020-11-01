
package networking;
import java.util.HashMap;
import networking.queueManagement.*;
import networking.utility.*;
import infrastructure.validation.logger.*;
/**
 * 
 * The class "ReceiveQueueListener" helps in dequeuing the receiving queue and calling handler method
 * of the corresponding module ( either processing module or content module) by passing the message.
 * 
 * @author Drisya P
 * @since 2021-11-31
 */
public class ReceiveQueueListener implements Runnable{
    
    /**
     * Receiving queue of the processing module or content module based.
     */
    private IQueue<IncomingPacket> receivequeue;
    
    /**
     * Logger instance for logging errors and activities.
     */
    ILogger logger = LoggerFactory.getLoggerInstance();
    
    /**
     * Map stores module identifiers ( processing / content module ) and corresponding handler. 
     */
    private HashMap<String, INotificationHandler> handlerMap;
    
    /**
     * Creates a new ReceiveQueueListener with the given queue and handlerMap.
     * @param receivequeue     processing or content module's receiving queue.
     * @param handlerMap       map which contains identifier and corresponding module INotificationHandler handler.
    */
    public ReceiveQueueListener( IQueue<IncomingPacket> receivequeue, HashMap<String, INotificationHandler> handlerMap ) {
        
        this. receivequeue = receivequeue;
        this.handlerMap = handlerMap;
        
    }
    
    
    /**
     * The method "run" is used to dequeue from the queue continuously when the queue is non empty and the thread is running and call
     * helper function to send the message to corresponding module.
     * @param Nothing
     * @return Nothing
     * @exception Exception If unable to call sendMessage method.
     * @see Exception
     */

    @Override
    public void run() {
       
        
        while( LanCommunicator.getStatus() ){
            
            if( !receivequeue.isEmpty() ){
            
                /**
                 * Incoming packet which contains message and identifier is removed from the front of the queue if queue is non empty.
                 */
                IncomingPacket packet = receivequeue.front();
                receivequeue.dequeue();
                
                /**
                 * Calling helper function "sendMessage" for sending message to corresponding module.
                 */
                try {
                    
                    sendMessage(packet);
                    
                } catch (Exception exception) {
                    
                    logger.log(ModuleID.NETWORKING, LogLevel.ERROR, "Exception in calling sendMessage " + exception );
                }
                
            }
        }
        
    }
    
    /**
     * The method "sendMessage"  will call the handler method of the corresponding module by passing the message.
     * @param packet        the packet which contains the identifier and the message to be sent.
     * @return  Nothing
     */
    
    private void sendMessage(IncomingPacket packet){
     
        /**
         * Retrieving the message to be sent.
         */
        String message = packet.getMessage();
        
        /**
         * Retrieving the identifier.
        */
        String identifier = packet.getIdentifier();
        
        /**
         * Obtaining the module ( either processing or content module )handler from the handlerMap corresponding the identifier.
         */
        INotificationHandler handler = handlerMap.get(identifier);
        
        /**
         * Calling the module handler method by passing the message.
         */
        if ( handler != null ){
            
            handler.onMessageReceived(message);
            
        } else {
            
            logger.log(ModuleID.NETWORKING, LogLevel.ERROR, "Handler is null for the identifier " + identifier );
        }
                
    }
    
}
