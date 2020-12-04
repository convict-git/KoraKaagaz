package networking.utility;

/**
 * This file contains the class that is going to used by SockListener-Component for adding the data
 *  to the queue and ReceiveQueueListener-Component for retrieving the data from the queue.
 *
 * @author Pandravisam Shiva Santhosh
*/

public class IncomingPacket{

    /** The content of the message that needs to be delivered to the corresponding module */
    String message;
    
    /** To indicate which module is sending the message */
    String identifier;

    /**
     * Empty Constructor, can be used to instantiate the object and later assign the values to the
     * variables in the class
     */
    public IncomingPacket(){
    }

    /**
     *  Overloaded Constructor which initialises all the variables in the class
     *
     * @param message the string that initialises the message variable in the class.
     * @param identifier the string that initialises the identifier variable in the class
     */
    public IncomingPacket(String message, String identifier){
        this.message = message;
        this.identifier = identifier;
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
     * This method "setMessage" sets the value of message content that needs to delivered
     *
     * @param message assigns the message variable in the class
     */
    public void setMessage(String message){
        this.message = message;
    }

    /**
     * This method "getMessage" returns the content in the message variable.
     *
     * @return the content in the message variable of the class.
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
        if(obj instanceof IncomingPacket){
            return (message == ((IncomingPacket) obj).message
                    && identifier == ((IncomingPacket) obj).identifier);
        }
        else {
            return false;
        }
    }
}
