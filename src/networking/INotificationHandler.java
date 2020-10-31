package networking;

/**
* This file contains INotificationHandler interface provided by networking module.
* This interface acts as notifier for other subscribed modules.
* This interface should be implemented by other modules who are in need for the messages received over the network.
*
* @author Prudhvi Vardhan Reddy Pulagam
*
*/

public interface INotificationHandler{
	
	/**
	* This method is invoked by networking module(particularly by ReceiveQueueListener) on receiving the message over network.
	* This method should do the processing or other needful of the message received over network.
	*
	* @param message The message received over network.
	*/
	public void onMessageReceived(String message);
}
