package networking;
/**
*
* @author Prudhvi Vardhan Reddy Pulagam
* 
* @INotificationHandler is an interface provided networking module, which acts as notifier for other modules.
* This interface 1 method i.e @onMessageRecieved()
*/
public interface INotificationHandler{
	/*
	This method is used in the process of subscribing the message recieved over network. Which takes 
	argument type String and doesnot return any.	
	*/
	public void onMessageRecieved(String message);
}