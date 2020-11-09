# Chat Window Specification Document

## Objective
- To provide the user interface to support chat feature among multi-users in the session.


## Design Choices for the Chatbox

### Publisher-Subscriber model
- We need to communicate with the content module in order to send the messages and receive the messages from other users.
- In order of better interaction purpose, we would be using the Publisher-Subscriber pattern.
- Publisher-Subscriber is a message pattern, where the message senders (Publishers) do not program the messages to be sent directly to specific receivers (subscribers), but instead categorize published messages into classes without knowledge of which subscribers, if any, there may be. Similarly, subscribers express interest in one or more classes and only receive messages that are of interest, without knowledge of which publishers, if any, there are. This is how the Publisher-Subscriber model works.
- Here, the UI module will subscribe to the content module and send the respective notification handler.
- So when there is message at the content module, they will notify the UI module by calling the provided notification handlers.
- And when we need to send the message to the content module, we will create the object and send the message using the functions present in it.

### JavaFX and Scene Builder
- To desing the GUI of a desktop application there are many tools available eg : JavaFX, Swing, AWT etc.
- Currently we would be using javaFX for designing the chat window.
- **JavaFX** is a set of graphics and media packages that enables developers to design, create, test, debug, and deploy rich client applications that operate consistently across diverse platforms.
- JavaFX is the successor of Swing which includes FXML and CSS files which eases the designing and formatting of the web pages.
- **JavaFX Scene Builder** is a visual layout tool that lets users quickly design JavaFX application user interfaces, without coding. Users can drag and drop UI components to a work area, modify their properties, apply style sheets, and the FXML code for the layout that they are creating is automatically generated in the background. The result is an FXML file that can then be combined with a Java project by binding the UI to the application’s logic.
- We would be using javaFX Scene Builder to create the view of the chatbox.
- Scene Builder will be beneficial because we can add various options and features of UI (eg: Button, Checkbox etc... ) easily.


### FrameWork : MVC Pattern

-  Model
    The Model describes the business logic and it is characterized by a set of classes. It works on to design business rules for data on how the data is changed or handled.
- View
    The View here represents the UI components. The View displays the data which the controller sends back as a form of the result. The Model also can be converted into the UI using View.

- Controller
    For proceeding incoming requests, the controller is highly responsive. Through the Model to View, the Controller gets the data of the user. Between the Model and the View, the Controller acts as a facilitator.


    
- Here, the view will be built using javaFX scene builder. It will be a fxml file. CSS properties can be used for providing various attributes like size of text area, colour etc...
- The controller class for the chatbox controls the data flow into the model class and updates the view whenever there are incoming messages.






## Chatbox Design
- Chat window is present to the right of the canvas. The chat window is of fixed dimension. This is a sample outline of how the chatbox looks like. (Colours will be added for buttons and text area).
![](https://i.imgur.com/oyg8mP9.png)

Note: We would be using the "label" feature in javaFX to display the messages.
## Features

The features supported by the Chatbox are
- SEND button to send the message
- If a message is recieved from another user, the user's message along with his name and his image will be shown.
- The Text area has no limit on characters to enter.
- The Text area is supported with a vertical scrollbar, if at all the message size is huge.
- Will try to support emoji symbols if time permits (optional)
- A new user who joins the session will not have access to the previous message history.
- All other characters which are compatible with the Text area field will be supported.
## Outline of Chat controller

```java=
public class chatbox extends Application {
    
    void base64StringToImage(string image);
	@Override
	public void start(Stage primaryStage) throws IOException         {
		
		Parent root = FXMLLoader.load(getClass().getResource("/chat.fxml"));
        //This is to load the view into the controller
        //...
        }
        
    
}
```

## Chat Window interaction with the Content Module

- The messages received are displayed along with the profile picture and the username who has sent it.
- Upon entering a message in the text area and press the send button, the message is sent to the content module using the interface.
```java
package contentManager;

public interface contentICommunicator{
    void notifyUserExit();
    
    void sendMessageToContent(String message);
    
    void subscribeForNotifications(String identifier, contentNotificationHandler handler);

}
```
- The contentNotification interface is implemented by the UI to display the messages recieved and also display the name of the new user joined.

```java
package contentManager;

public interface contentNotificationHandler {
	void onMessageReceived(String username, String message, String image);
	
}
```


## Control Flow
- The user first enters the message in the text area provided at the interface.
- The moment he clicks the "SEND" button, the user who had sent the message will see that the message he sent will appear at the right side in the chatbox.
- On clicking the "SEND" button, we take the meassage in string format and user name also in string format.
- This will be delivered to the content module to send it over the network to other users.
- The content module responds back with the message string and image string (which will be converted to image) and the message should be delivered to all the other users.
- The message will be present at the left side of the chatbox, along with the user name and image of the user who sent the message.

## Design Modifications at the interface
- Right now, the chat window will send the message string and we would be receiving from content module the corresponding message string and image string of the user who sent the message are delivered to the UI module from the content module.
- But, we thought of formatting by using Json. We would be sending and receiving Json object which will have attributes like username, message and image string( base64 format).(To be reviewed again)


## Class Diagram

![](https://i.imgur.com/2VRdYdM.png)




