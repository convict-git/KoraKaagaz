package networking.utility;

/**
 * This file contains the class that is going to used by Send-Component for adding the data to the queue
 * and SendQueueListener-Component for retrieving the data from the queue.
 *
 * @author Pandravisam Shiva Santhosh
 */

public class OutgoingPacket {

    /** Destination IP address of the host for which the message should be delivered */
    String destination;

    /** The content of the message that needs to be delivered */
    String message;

    /** To indicate which module is sending the message */
    String identifier;

    /**
     *  Empty Constructor, can be used to instantiate the object and later assign the values to the variables
     *  in the class
     */
    public OutgoingPacket(){
    }

    /**
     * Overloaded Constructor which initialises all the variables in the class
     *
     * @param destination  initialises the destination variable of the class
     * @param message initialises the message variable of the class
     * @param identifier initialises the identifier variable of the class
     */
    public OutgoingPacket(String destination, String message, String identifier) {
        this.destination = destination;
        this.message = message;
        this.identifier = identifier;
    }

    /**
     * This method "setDestination" sets the value destination IP address
     *
     * @param destination assigns the destination variable of the class
     */
    public void setDestination(String destination){
        this.destination = destination;
    }

    /**
     * This method "setMessage" sets the value of message content that needs to delivere
     *
     * @param message assigns the message variable of the class
     */
    public void setMessage(String message){
        this.message = message;
    }

    /**
     * This method "setIdentifier" sets the value of the identifier.
     *
     * @param identifier assigns the identifier variable in the class
     */
    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }

    /**
     * This method "getDestination" returns the destination
     *
     * @return the destination variable content as a string
     */
    public String getDestination(){
        return destination;
    }

    /**
     * This method "getMessage" returns the message
     *
     * @return the message variable content as a string
     */
    public String getMessage(){
        return message;
    }

    /**
     * This method "getIdentifier" returns the identifier
     *
     * @return the identifier variable content as a string
     */
    public String getIdentifier(){
        return identifier;
    }

    /**
     * This utility method "isEqual" takes a object as a argument and returns true if both the objects are equal or
     * else returns false
     *
     * @param obj any object.
     * @return true, if the current class object equals to the object that was as a argument or else false.
     */
    public boolean isEqual(Object obj) {
        if(obj instanceof OutgoingPacket){
            return (destination == ((OutgoingPacket) obj).destination
                    && message == ((OutgoingPacket) obj).message
                    && identifier == ((OutgoingPacket) obj).identifier);
        }
        else {
            return false;
        }
    }
}
