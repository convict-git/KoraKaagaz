package networking.server;

import networking.queueManagement.*;
import infrastructure.validation.logger.*;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *  This file contains the implementation of the class SendThread which runs on the server and sends the message
 *  received from the RecieveThread on server to the SocketListener which runs on client machine. This is a part
 *  of the server which is required for the implementation of communication over internet.
 *
 *  @author Pandravisam Shiva Santhosh
 */

public class SendThread extends Thread {

    /**
     * Declaring the DataOutputStream variable which is used for writing to the client socket
     */
    final DataOutputStream dos;

    /**
     * Declaring the queue variable which is used to dequeue the required the message
     */
    IQueue<String> q;

    /**
     *Constructor which initialises DataOutputStream variable and queue from the arguments passed
     *
     * @param dos this is the DataOutputStream object which is used for writing
     * @param q this the queue that we are going to interact to get the message
     */
    public SendThread(DataOutputStream dos, IQueue<String> q){
        this.dos = dos;
        this.q = q;
    }

    /**
     *  Creating a Logger Instance for logging mainly the errors
     */
    ILogger logger = LoggerFactory.getLoggerInstance();

    /**
     *  This method "run" starts executing the thread and listens for the data continously to send it0 to the respective
     *  client socket which is received from RecieveThread on server via the queue.
     */
    @Override
    public void run(){
        /** Listen continously for the messages */
        while (true){
            /**
             * If the queue is not empty, get the data which is the front element of the queue and remove that element
             * from the queue
             */
            if (!q.isEmpty()){
                String data = q.front();
                q.dequeue();
                try{
                    /** Write the data which is got from the queue to the client socket*/
                    dos.writeUTF(data);
                }
                catch (IOException e){
                    /** If any exception araises while writing the socket, log the info and stop the thread*/
                    logger.log(ModuleID.NETWORKING, LogLevel.ERROR, "An I/O Exception has been raised on server in SendThread");
                    return;
                }
            }
        }
    }
}
