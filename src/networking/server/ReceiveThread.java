package networking.server;
import java.io.*; 
import java.util.HashMap;

import org.json.JSONObject;

import networking.queueManagement.*;
import infrastructure.validation.logger.*;


/**
 *The class "ReceiveThread" implements  ReveiveThread class which reads data from the client and extracts
 *message and puts the message into the allocated queue for the destination client.
 * 
 * @author Drisya P
 * @since 2021-11-10
 */
public class ReceiveThread extends Thread{

    /**
     * object of java DataInputStream class which allows to read data from 
     * the input stream.
     */
    private DataInputStream dis;
    
    /**
     * HashMap that stores id of the client as key and the ConcurrentQueue 
     * associated with it.
     */
    private HashMap<String, IQueue<String>> map;
    
    /**
     * Logger instance for logging errors and activities.
     */
    ILogger logger = LoggerFactory.getLoggerInstance();
   
    /**
     * Creates a new ReceiveThread with the given DataInputStream and HashMap.
     * @param dis      object for java DataInputStream class 
     * @param map      stores information of client id and corresponding queue
    */
    public ReceiveThread(DataInputStream dis, HashMap<String, IQueue<String>> map ){
        
        this.dis = dis;
        this.map = map;
    }
    
    /**
     * The method "run" is used to reads data from the client and extracts
     * message and puts the message into the allocated queue for the destination client.
     * 
     * @param Nothing
     * @return Nothing
     * @exception Exception If unable to call read data from input stream
     * @see Exception
     */
    
    @Override
    public void run() {
        
        /**
         *Stores the message to be sent.  
         */
        String message;
        
        /**
         * Id of the destination client.
         */
        String id;
      
        
        /**
         * Queue for the destination client.
         */
        IQueue<String> queue;
        
        while(true){
            
            try{
              
                    /**
                     * Read a string that has been encoded using the UTF-8 format.
                     */
                    message = dis.readUTF();
                    logger.log(ModuleID.NETWORKING, LogLevel.INFO, "Message received");
                    
                    /**
                     * Obtaining the destination client id from the message received
                     */
                    JSONObject json = new JSONObject(message);
                    id = json.getString("destination").split(":")[1];
                    
                    /**
                     * Puts the message into the  queue associated with the destination client if the queue is allocated to it.
                     */
                    if( map.containsKey(id)){
                        
                        queue = map.get(id);
                        queue.enqueue(message);
                        
                    } else {
                        
                        /**
                         * Error shown if there is no queue allocated to the destination client, that means 
                         * there is no entry in the map corresponding to the destination client id.
                         */
                        logger.log(ModuleID.NETWORKING, LogLevel.ERROR, "Cleint of Id, " + id + " is not present in the Map");
                    }    
                    
            } catch(Exception e){
                
                logger.log(ModuleID.NETWORKING, LogLevel.ERROR, "Error in reading message from datainput message " +  e);
                return;
            }
        }
        
    }
    
    
}

