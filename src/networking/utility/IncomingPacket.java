package networking.utility;

/**
 *
 * @author Pandravisam Shiva Santhosh
*/

/*
    This file contains the class that is going to used by SockListener-Component for adding the data
    to the queue and ReceiveQueueListener-Component for retrieving the data from the queue.
 */

public class IncomingPacket{

    // The content of the message that needs to be delivered to the corresponding module
    String message;

    // Empty Constructor, can be used to instantiate the object and later assign the values to the variables in the class
    public IncomingPacket(){
    }

    // Overloaded Constructor which initialises all the variables in the class
    public IncomingPacket(String message){
        this.message = message;
    }

    //This method "setMessage" sets the value of message content that needs to delivere
    public void setMessage(String message){
        this.message = message;
    }

    // This method "getMessage" returns the message
    public String getMessage(){
        return message;
    }

    /*
    This utility method takes a object as a argument and returns true if both the objects are equal or
    else returns false
    */
    public boolean isEqual(Object obj) {
        if(obj instanceof IncomingPacket){
            if(message == ((IncomingPacket) obj).message) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
