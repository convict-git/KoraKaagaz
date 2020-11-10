package networking;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

import networking.queueManagement.*;
import networking.utility.*;
import infrastructure.validation.logger.*;

/**
 * This file contains implementation of sendQueueListener class which is
 * part of networking module and the class in this file is responsible for
 * sending the data over the network.
 * This class is implemenation of runnable interface so it has the capability
 * of running in a thread.
 * 
 *  @author Sirpa Sahul
 */

public class SendQueueListener implements Runnable {
    
    /**
     * It is the sendqueue which will contain the outgoing packets which needs to be 
     * send over the network.
     */
    private IQueue<OutgoingPacket> SendQueue;

    /** 
	 * logger object from the LoggerFactory to log messages
	*/
	Ilogger logger = LoggerFactory.getLoggerInstance();

    /**
     * Constructor for this sendQueueListener class.
     * @param SendQueue, which require the sendqeue as parameter.
     */
    public SendQueueListener(IQueue<OutgoingPacket> SendQueue){

        /**
         * logging when the instance of the class is created
         */
        logger.log(ModuleID.NETWORKING, LogLevel.INFO, "Send Queue Listener object created");

        this.SendQueue = SendQueue;
    }


    /**
     * This method simply splits the destination address into ip address and port number.
     * @param destination, destination address assumed as ip:port format
     * @return , return the array which will separate ip and port.
     */
    private String[] splitAddress(String destination){
        return destination.split(":");
    }


    /**
     * The following method will check whether the ip address is valid or not.
     * @param IP, IPV4 address in string format.
     * @return boolean, if it's valid address then returns true else returns false.
     */
    private boolean isValidIpaddress(String IP){
        
        /**
         * regular expression for integer between 0 and 255
         */
        String zeroTo255 
            = "(\\d{1,2}|(0|1)\\"
              + "d{2}|2[0-4]\\d|25[0-5])";

        /**
         * regular epression for ip address.
         */
        String ipreg = zeroTo255 + "\\."
        + zeroTo255 + "\\."
        + zeroTo255 + "\\."
        + zeroTo255; 
        
        /**
         * if ip address matches regular expression return true
         * else return false.
         */
        return Pattern.matches(ipreg, IP);
    }


    /**
     * The following method will check whether the port number is valid or not.
     * @param port, port in string format, ex: "8080"
     * @return boolean, if it's valid port then returns true, else return false
     */
    private boolean isValidPort(String port){
        String intPattern = "[0-9]+";
        if(Pattern.matches(intPattern, port)){
            int value = Integer.parseInt(port);
            //it will return true if port is valid else false.
            return (value > 0) && (value <= 65535);
        }
        return false;
      
    }


    /**
     * The method will check whether ip:port address is valid or not.
     * @param destination, is ip, port address as string
     * @return boolean, return true if destination address is valid, false if it's not valid.
     */
    private boolean isValidAddress(String destination){
        String[] dest = splitAddress(destination);
        return ( dest.length == 2 && isValidIpaddress(dest[0]) && isValidPort(dest[1]) );
    }


    /**
     * This method will do the work of taking the data from the queue 
     * and send it over the network.
     */
    public void run(){
        
        /**
         * when the the thread is started running we logged the instance of it.
         */

        logger.log(ModuleID.NETWORKING, LogLevel.INFO, "Send Queue Listener thread started running");

        /**
         * run the while loop as long as the application is running.
         */
        while(LanCommunicator.getStatus()){
            
            /**
             * Check whether queue is empty or not, if it's not empty, 
             * then try to send the message.
             */

            if( !SendQueue.isEmpty() ) {

                /**
                 * Take the outgoing packet from the sendqueue.
                 */
                OutgoingPacket out = SendQueue.front();

                String destination = out.getDestination();

                /**
                 * if port number or ip address is invalid then no need to send the message
                 * simply discard it by contiuing to the next message.
                 */
                if(! isValidAddress(destination)){
                    /**
                     * as the message is invalid simply dequeue and continue for next one.
                     */
                    SendQueue.dequeue();
                    continue;
                }

                /**
                 * Divide the destination address into ip and port from destination.
                 */
                String[] dest = this.splitAddress(destination);

                /**
                 * Take ip address from the dest array
                 */
                String ip = dest[0];

                /**
                 * take port number from the dest array and parse it into integer.
                 */
                int port = Integer.parseInt(dest[1]);
                
                /**
                 * store the message into the message variable
                 */
                String message = out.getMessage();
            
                /**
                 * store the identifier variable into the identifier.
                 */
                String identifier = out.getIdentifier();

                message = identifier + ":" + message;
                
                /**
                 * The following code in try block will try to send the message over the network.
                 */
                try{

                    /**
                     * create socket using ip address and port number.
                     */
                    Socket sock = new Socket(ip, port);

                    /**
                     * create data output stream as we are using tcp.
                     */
                    DataOutputStream dout = new DataOutputStream(sock.getOutputStream());

                    /**
                     * encode the data into UTF format and write it to output stream
                     */
                    dout.writeUTF(message);

                    /**
                     * flush the byte stream into the network.
                     */
                    dout.flush();

                    /**
                     * close the outgoing stream.
                     */
                    dout.close();

                    /**
                     * now close the socket.
                     */
                    sock.close();

                    /**
                     * For every outgoing packet delivered the log the message with destination address.
                     */
                    String logMessage = "Message delivered to destination " + destination;
                    logger.log(ModuleID.NETWORKING, LogLevel.SUCCESS, logMessage);

                } catch (Exception e) {
                    
                    /**
                     * if any exception occurs then log the error.
                     */
                    logger.log(ModuleID.NETWORKING, LogLevel.ERROR, exp);
                }
                
                /**
                 * Now dequeue the message from sendqueue.
                 */
                SendQueue.dequeue();
            }

            /**
             * Logging the infromation that when the thread is going to stop.
             */

            logger.log(ModuleID.NETWORKING, LogLevel.INFO, "Send Queue Listener thread is going to stop running");

        }
    }
}
