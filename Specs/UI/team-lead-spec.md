# UI Module Specification Document

## Team Members
- K. Sai Deekshith 
- J. Jaya Madhav
- Anish Jain
- Chandana S K
- K. Shiva Dhanush
- E. Sajith Kumar
- Anagha M S


## Objective
- To provide the user interface to draw and view the drawing of the other participants in the session and also to chat with the others in the session.




## Design Choices

### Subscriber-Publisher
- We need to communicate with the processing module and content module in order to send the data and receive the data.
- For better interaction we are going to use the Publisher-Subscriber pattern.
- Here the UI module will subscribe to the processing module and content module and send the respective notification handler.
- So when there is data at the processing module or content module they willl notify us by calling the provided notification handlers.
- And when we need to send the data to the processing module or content module we will create the object and send the data using the functions present in it.

### JavaFX vs Swing
- To desing the GUI of a desktop application there are many tools available eg : JavaFX, Swing, AWT etc.
- Currently JavaFX and Swing are the important tools used for UI development.
- **Swing** is a GUI widget toolkit for Java.Swing was developed to provide a more sophisticated set of GUI components than the earlier Abstract Window Toolkit (AWT).Swing components are not implemented by platform-specific code hence they are platform independent.
- **JavaFX** is a set of graphics and media packages that enables developers to design, create, test, debug, and deploy rich client applications that operate consistently across diverse platforms.
- JavaFX is the successor of Swing which includes FXML and CSS files which eases the designing and formatting of the web pages.
- Hence we are opting for JavaFX instead of Swing.

### FrameWorks
- For better and clean coding purpose there ara many patterns available. Some of the famous pattern which can be used well with javaFX are MVC and MVVM patterns.
- The main difference between the MVC and MVVM is that in MVC we have a Controller in MVVM we have ViewModel.

     -  Model
    The Model describes the business logic and it is characterized by a set of classes. It works on to design business rules for data on how the data is changed or handled.

    - View
    The View here represents the UI components. The View displays the data which the controller sends back as a form of the result. The Model also can be converted into the UI using View.

    - Controller
    For proceeding incoming requests, the controller is highly responsive. Through the Model to View, the Controller gets the data of the user. Between the Model and the View, the Controller acts as a facilitator.

    - ViewModel
    It is the responsibility of the View-Model to present methods and functions. It needs to present commands to operate the model, uphold the state of the View and activate the events in the View.
    
- MVC is advantageous over MVVM in TDD(Test Driven Development) and supports multiple view pages.
- In MVVM in supports two way data binding which is not exactly neccessary for our project.
- Finally MVC is decided due to all the advantages provided.






### Canvas
- The White Board application must be accessible in different laptops with different resolutions.
- Hence when we run the application the canvas should be constant across the platforms.
- To ensure it we are having canvas of fixed length and width.
- To support the clients with less resolution than the dimensions of the canvas we are having scroll bars to the canvas.

### Pixel
- When we are drawing on the canvas we change the colour where we are drawing.
- In order to send the data and receive the data across the network we need to properly define the basic unit of canvas.
- Pixel is the basic unit of canvas which is having the properties of colour and (x,y) cordinates.
- But when are drawing on canvas there are many points covered hence the data is huge. How to optimise?
- We are defining the pixel as (x,y) coordinate with a fixed size around. Here the size depends on the precision of the javaFX canvas. Since the canvas is independent of the resolution, pixel is also independent of the resolution.
- Now for large brush sizes the number of pixel in a simple stroke are greater than smaller brush stroke.

```java
class Intensity {    
    int r, g, b;         // RGB values at a position
}

class Position {
    int x, y;            // pixel position
}

class Pixel {            // pixel position with RGB values
    Intensity intensity;    
    Position position;
}
```

### Testing
- UI needs to tested based on the action and the pixels which are changed due to the action.
- As we are using MVC pattern whenever there is any change in the UI then there is corresponding change in the model of the corresponding view.
- Hence when simulate a particular tool of the UI we will get the updates in the corresponding model. Hence we can verify if the updates in the model is same as pixels which are changed by the simulation.
- There are many UI testing tools available like TestFX, Selenium, Squish.
- TestFX is found to be very useful as it can automate the testing and also run tests for multiple tools and multiple test cases.

## Features

### Session options
- Join Session
- New Session

### Menu Options
- Brush
- Eraser
- Shapes
- Cursor
- Undo
- Redo
- Clean
- Chat Window
- Leave Session

### Optional
- TextBox
- Zoom in and Zoom out
- Download the white board of the session as PNG.


**1. New Session :**
- New session takes in the username, the ip address of the server and profile picture.
- Sends the profile picture,ip address and the username to the content module.
- Sends the username and the ip address to the processing module.
- Gets the boardID from the processing module and displays it to the client to share.
- Provides an buon to Start Session.
- Create a new plain board with all the menu options.
- getUserDetails API of the IUser interface is called to send the data to processing module.
- sendDetailsToContent of contentIcommunicator is called to send data to content module.

**2. Join Session :**
- Asks for the boardID , user name,ip address and profile picture.
- Provides an buon to Start Session.
- Sends the boardID, ip address and username to the processing module.
- Sends the boardID,ip address username and profile picture to the content module.
- Create a new plain sheet with all the menu options.

```java
public interface IUser {
    String getUserDetails(String userName, String ipAddress, String boardId);
    String getUser(ArrayList<Position>);
    void stopBoardSession();
    void subscribeForChanges(String identifier,IChanges handler);
}
```

**3. Brush :**
- Brush is used to draw on the canvas.
- When we click on brush we ask the client to provide brush color and the brush size.
- When the brush is used to draw on the canvas we will calculate the pixels which are changed and store it in a array.
- When is the mouse drag event ends we call draw_curve API of the object of type IDrawErase and send the array of pixels.

**4. Eraser :**
- Eraser is used to clean drawings on the canvas.
- Eraser supports different sizes similar to brush size.
- When the eraser is used to clean the canvas we will calculate the pixels which are changed and store it in a array.
- When is the mouse drag event ends we call erase API of the object of type IDrawErase and send the array of pixels.

```java
public interface IDrawErase {
    // parameter: ArrayList<Pixel> - an ArrayList of pixel with RGB values for random curve object 
    void drawCurve(ArrayList <Pixel>);
    // parameter: ArrayList<Position> - an ArrayList of pixel position for erase object
    void erase(ArrayList <Position>);
}
```
**5. Shapes :**
- On click provides an menu of standard shapes like circle, rectangle, square and line segment.
- Also provides the option to select the colour of the shape.
``` java
public interface IDrawShapes {
    // parameter: Pixel center - center position with intensity for circle
    //            radius - radius of circle
    void drawCircle(Pixel center, float radius);
    // makes an object for square parallel to the whiteBoard 
    // parameter: Pixel start - start position for square with RGB values in whiteBoard
    //            float length - length of the sqaure
    void drawSquare(Pixel start, float length);         
    // parameter: Pixel start - top left position with RGB values for rectangle in whiteBoard
    //            Pixel end - bottom right position with RGB values for rectangle in whiteBoard
    void drawRectangle(Pixel start, Pixel end); 
    // makes an object for a line segment parallel to the whiteBoard 
    // parameter: Pixel start - start position with RGB values
    //            Pixel end - end position with RGB values
    void drawLine(Pixel start, Pixel end);        
}
```
- There is a fixed border size for all the shapes.
- Upon drawing the circle it sends the center and radius of the cirlce using the IDrawShapes interface.
- For rectangle we are sending the pixels at the end of one of the two diagonals.
- For square we are sending one of the vertex and the length of the side of the square.

**6. Cursor :**
- It provides the following features:
    - Select (Display UserName and Highlight)
    - Change Colour
    - Delete
    - Rotate
- **Select**:
    - Upon selecting the cursor options and hovering over the drawing it displays the last person edited the particular pixel.
    - If there is no drawing at a position it will not show anything.
    - getUser API of IUser is used to get the username when we pass the position where the cursor is present.

- Upon clicking at a particular position it shows a menu of options.
- It sends the position selected to the processing module and gets the pixels related to that drawing.
- **Change Colour**:
    - Upon selecting the change colour option it asks the user to provide the new colour.
    - The colourChange API is called .
    - The updated pixels are received from the IChanges interface  and the canvas is updated.

- **Delete :**
    - Upon selecting the delete option it calls the API of the IOperation interface.
    - The updated pixels are received from the IChanges interface  and the canvas is updated.

- **Rotate :**
    - Upon selecting the rotate option the user is provided with the option to rotate the drawing either 90,180 and 270.
    - The selected option is passed to the processing module using the interface and the updated pixels are received from the IChanges interface and the canvas is updated.

```java
public interface IOperation {
    // returns the pixel position for selected object if  an object exist at selected position  
    ArrayList<Position> select (ArrayList <Position>);
    // deletes the selected object
    void delete ();
    // changes color of selected objected to specified intensity 
    void colorChange (Intensity intensity);
    // rotates selected object by specified angle in counter clockwise direction
    void rotate (double angleCCW);
    // removes all the object drawn by the particular user 
    void reset ();
}
```
```javascript=
public interface IChanges {
    void getChanges(ArrayList<Pixel>);
}

```
**7. Clean**
- Upon selecting this option all the drawings of the user are deleted.
- The chat message are not changed.
- reset API of the IOperation interface is called.
- The updated pixels are received from the getChanges API of IChanges interface the canvas is updated.


**8. Undo and Redo**
- Undo option reverts the last drawing of the user.
- It can only undo the changes made by that user.
- If there is no previous state then there is no change.
- Redo options performs the last operation which was reverted.
- If the current state is the latest state then there is no update of the canvas upon redo.
- When undo or redo option is selected the particular API is selected from the interface.
- The updated pixels are received from the getChanges API of IChanges interface the canvas is updated.

```java
public interface IUndoRedo {
    // redo the work done by a particular user
    void redo();
    // undo the work done by a particular user
    void undo();
}
```

**9. Chat Window**
- Chat window is present to the right of the canvas. The chat window is of fixed dimension.
- The messages received are displayed along with the profile picture and the username who has sent it.
- Upon entering a message in the text area and press the send button the message is sent to the content module using the interface.
```java
package contentManager;

public interface contentICommunicator{
    void notifyUserExit();
    void initialiseUser(String username, String ip, String      											image);
    void sendMessageToContent(String message);
    
    void subscribeForNotifications(String identifier, 			contentNotificationHandler handler);

}
```
- The contentNotification interface is implemented by the UI to display the messages recieved and also display the name of the new user joined.

```java
package contentManager;

public interface contentNotificationHandler {
	void onMessageReceived(String username, String message, String image);
	void onNewUserJoined(String username);
    void onUserExit(String username);
}
```
**10. Leave Session**
- Upon selecting this option both the processing module and the content module are notified that the client is leaving the session.
- Pops up an warning indicating all of your drawings will be erased.
- Upon accepting it clears all the objects and return to the home page.
- stopBoardSession API of the IUser interface is called. No input is provided and no output is expected.
- notifyUserExit  API of the contentICommunicator interface of content module is called. No input is provided and no output is expected.

## Control Flow
![](https://i.imgur.com/oc7IWxV.png)

## Dependencies with in the Module
- There is a defined order for the smooth development of the UI module.
    1. We need to develop the Join Session and New Session at the start as we need to have initialise the canvas after we join the session.
    2. The remaining features of the menu bar can be done independently.
    3. Initially we ensure that given pixel array are we able represent them in the UI.
    4. When we use a particular feature are we able to get the positions or pixel array properly.
    5. Then we need to communicate with the other modules and check if the data is sent properly and received properly.

## Development Tools
- For the development purpose the UI needs to show th log messages when testing is done.
- The format of the log messages is [YYYY:MM:DD HH:MM:SS] [Error/Warning/Info/Success] [moduleName] logmessage.

## Class Diagram
![](https://i.imgur.com/ZGK9edQ.png)






## Tasks and Allocation
|   Team Member    |                    Task                     |
|:----------------:|:-------------------------------------------:|
| K. Shiva Dhanush | Join Session, New Session and Leave Session |
|  J. Jaya Madhav  |   Chat Window and Content Module Listener   |
|    Anish Jain    |              Brush and Eraser               |
| E. Sajith Kumar  |    Shapes and Processing Module Listener    |
|    Anagha M S    |        Undo, Redo and Clean my work         |
|   Chandana  S K  |                    Cursor                   |
|K. Sai Deekshith  | Development Tools and  UI module testing    |
