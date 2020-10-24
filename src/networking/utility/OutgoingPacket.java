package networking.utility;

/**
 *
 * @author Pandravisam Shiva Santhosh
 */

/*
    This file contains the class that is going to used by Send-Component for adding the data to the queue
     and SendQueueListener-Component for retrieving the data from the queue.
 */

public class OutgoingPacket {

    // Destination IP address of the host for which the message should be delivered
    String destination;

    // The content of the message that needs to be delivered
    String message;

    // To indicate which module is sending the message
    String identifier;

    // Empty Constructor, can be used to instantiate the object and later assign the values to the variables in the class
    public OutgoingPacket(){
    }

    // Overloaded Constructor which initialises all the variables in the class
    public OutgoingPacket(String destination, String message, String identifier) {
        this.destination = destination;
        this.message = message;
        this.identifier = identifier;
    }

    //This method "setDestination" sets the value destination IP address
    public void setDestination(String destination){
        this.destination = destination;
    }

    //This method "setMessage" sets the value of message content that needs to delivere
    public void setMessage(String message){
        this.message = message;
    }

    //This method "setIdentifier" sets the value of the identifier.
    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }

    //This method "getDestination" returns the destination
    public String getDestination(){
        return destination;
    }

    // This method "getMessage" returns the message
    public String getMessage(){
        return message;
    }

    // This method "getIdentifier" returns the identifier
    public String getIdentifier(){
        return identifier;
    }

    /*
    This utility method takes a object as a argument and returns true if both the objects are equal or
    else returns false
     */
    public boolean isEqual(Object obj) {
        if(obj instanceof OutgoingPacket){
            if(destination == ((OutgoingPacket) obj).destination && message == ((OutgoingPacket) obj).message
                    && identifier == ((OutgoingPacket) obj).identifier) {
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