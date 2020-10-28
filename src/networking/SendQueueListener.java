package networking;

import networking.queueManagement.*;
import networking.utility.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

/** 
 * @author Sirpa Sahul
 */

/*
    This file contains class which is sendQueueListener which is
    part of networking module and the class in this file is responsible for
    sending the data over the network.
 */

public class SendQueueListener implements Runnable {
    
    //This method simply splits the destination address into ip address and port number.
    public String[] splitAddress(String destination){
        return destination.split(":");
    }

    // The following function will check whether the ip address is valid or not.
    public boolean isValidIpaddress(String IP){
        //regular expression for integer between 0 and 255
        String zeroTo255 
            = "(\\d{1,2}|(0|1)\\"
              + "d{2}|2[0-4]\\d|25[0-5])";

        //regular epression for ip address.
        String ipreg = zeroTo255 + "\\."
        + zeroTo255 + "\\."
        + zeroTo255 + "\\."
        + zeroTo255; 
        
        /*
            if ip address matches regular expression return true
            else return false.
        */
        return Pattern.matches(ipreg, IP);
    }
    //This method will do the work of taking the data from the queue 
    //and send it over the network.
    public void run(){
        
        // run the while loop as long as the application is running.
        while(LanCommunicator.isRunning){
            
            //Take the outgoing packet from the sendqueue.
            OutgoingPacket out = LanCommunicator.SendQueue.front();

            //Divide the destination address into ip and port from destination.
            String[] dest = this.splitAddress(out.destination);

            //Take ip address from the dest array
            String ip = dest[0];

            //take port number from the dest array and parse it into integer.
            int port = Integer.parseInt(dest[1]);

            
            //store the message into the message variable
            String message = out.message;
        
            //store the identifier variable into the identifier.
            String identifier = out.identifier;

            //Encode the identifier into the message itself.
            if(identifier == "processing"){
                message = "p" + message;
            }
            else if(identifier == "content"){
                message = "c" + message;
            }
            else{
                continue;
            }

            //The following code in try block will try to send the message over the network.
            try{

                //create socket using ip address and port number.
                Socket sock = new Socket(ip, port);

                //create data output stream as we are using tcp.
                DataOutputStream dout = new DataOutputStream(sock.getOutputStream());

                //encode the data into UTF format and write it to output stream
                dout.writeUTF(message);

                //flush the byte stream into the network.
                dout.flush();

                //close the outgoing stream.
                dout.close();

                //now close the socket.
                sock.close();
            } catch (Exception e) {

                //if any exception occurs then we are printing the error to console.
                System.out.println(e);
            }
            
            //Now dequeue the message from sendqueue.
            LanCommunicator.SendQueue.dequeue();
        }
    }
}
