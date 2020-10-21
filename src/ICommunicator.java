package sw;
/**
 *
 * @author Prudhvi Vardhan Reddy Pulagam
 */
public interface ICommunicator {

    //Start communication by initalizing all the threads needed for communication. 
    void start();
    
    //Stop the Communication.
    void stop();
    
    //Send the data to the destination host.
    void send(String destination, String message, String identifier);
    
    //Subscribe the module for receiving the data from the network module.
    void subscribeForNotifications(String identifier, INotificationHandler handler);
}