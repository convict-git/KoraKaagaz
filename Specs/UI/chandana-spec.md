# UI Module Specification Document - Cursor

#### Documented by Chandana S K (111701009)

## UI Module Team Members
- K. Sai Deekshith
- Anagha M S
- Anish Jain
- Chandana S K
- E. Sajith Kumar
- J. Jaya Madhav
- K. Shiva Dhanush




## Objective
- The big goal is to build a multiuser Whiteboard Java based application, KoraKaagaz. The UI Module team's objective is to provide the user interface to draw and view the drawing of the other participants in the session and also to chat with others in the session.

- The **Cursor** that is proposed in this document is one of the feature that will be provided to the user with the following options:
	- **Select** (Display username and Highlight the selected shape/object)
	- **Change Colour** (change the colour of the selected component)
	- **Delete** (delete the selected component of the drawing)
	- **Rotate** - 90&deg;, 180&deg;, 270&deg; (rotate the selected object by 90&deg;/180&deg;/270&deg;)
	
	
## Design Choices

### Publisher-Subscriber

- The components of the system often need to provide information to other components as events happen. 
- In our case also, frequent communication with the processing module and content module is essential in order to send and receive data.
- The functioning of the Cursor feature depends greatly on the interaction between the UI module and the processing module, we will subscribe to the processing module send the respective notification handler.
- So, any change in the data at the processing module will be notified to UI module through the notification handlers provided by the processing module.
- Whenever any data needs to be sent to the processing module, an object is created and the data is sent using the functions present in it.
- So, for better interaction we are going to use the **Publisher-Subscriber** pattern as it beneficial in many ways:
	- It decouples subsystems that still need to communicate. Subsystems can be managed independently, and messages can be properly managed even if one or more receivers are offline.
	- It increases scalability and improves responsiveness of the sender. The sender can quickly send a single message to the input channel, then return to its core processing responsibilities. The messaging infrastructure is responsible for ensuring messages are delivered to interested subscribers.
	- It improves reliability.
	
	
### JavaFX vs Swing

- To desing the GUI of a desktop application there are many tools available eg : JavaFX, Swing, AWT etc.
- Currently JavaFX and Swing are the important tools used for UI development.
- **Swing** is a GUI widget toolkit for Java.Swing was developed to provide a more sophisticated set of GUI components than the earlier Abstract Window Toolkit (AWT).Swing components are not implemented by platform-specific code hence they are platform independent.
- **JavaFX** is a set of graphics and media packages that enables developers to design, create, test, debug, and deploy rich client applications that operate consistently across diverse platforms.
- JavaFX is the successor of Swing which includes FXML and CSS files which eases the designing and formatting of the web pages.
- Hence we are opting for ***JavaFX*** instead of Swing.

### FrameWorks
- For better and clean coding purpose there ara many patterns available. Some of the famous pattern which can be used well with JavaFX are MVC and MVVM patterns.
- The main difference between the MVC and MVVM is that in MVC we have a Controller whereas in MVVM we have a ViewModel.

     -  **Model**
    The Model describes the business logic and it is characterized by a set of classes. It works on to design business rules for data on how the data is changed or handled.

    - **View**
    The View here represents the UI components. The View displays the data which the controller sends back as a form of the result. The Model also can be converted into the UI using View.

    - **Controller**
    For proceeding incoming requests, the controller is highly responsive. Through the Model to View, the Controller gets the data of the user. Between the Model and the View, the Controller acts as a facilitator.

    - **ViewModel**
    It is the responsibility of the View-Model to present methods and functions. It needs to present commands to operate the model, uphold the state of the View and activate the events in the View.
    
- MVC is advantageous over MVVM in TDD(Test Driven Development) and supports multiple view pages.
- In MVVM, it supports two way data binding which is not exactly neccessary for our project.
Finally ***MVC*** is decided due to all the advantages provided.


### Pixel

- In order to send the data and receive the data across the network we need to properly define the basic unit of canvas.
- Pixel is the basic unit of canvas which has the properties of colour and (x,y) cordinates.
- But when we are drawing on a canvas, there are many points covered. Hence the data is huge. How to optimise?
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

## Overall Features

The overall features of KoraKaagaz that would be implemented by the UI Module is mentioned below: 

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
- Clean my work
- Chat Window
- Leave Session

### Optional
- TextBox
- Zoom in and Zoom out
- Download the white board of the session as PNG.

*I'll be working on the Cursor feature.*

## Cursor (in detail)

The **Cursor** provides the following options to the user:

	- **Select** (Display username and Highlight the selected shape/object)
	- **Change Colour** (change the colour of the selected component)
	- **Delete** (delete the selected component of the drawing)
	- **Rotate** - 90&deg;, 180&deg;, 270&deg; (rotate the selected object by 90&deg;/180&deg;/270&deg;)
	

**Select (Display UserName and Highlight)**

Upon selecting the cursor option and clicking the drawing, it displays the last person edited the particular pixel and highlights the object selected.
If there is no drawing at a position it will not show anything.
    
- When the user clicks on a particular point (onMouseClick), the position of the pixel is obtained.
- The Pixel Position is sent to the Processing Module by calling the ***select*** API of Processing Module. It then returns the ArrayList of all the pixels of the corresponding object at that position along with the username(String).
- To display the username, either a label can be created and positioned at the selected position or just print the string (username) at the selected position.
- The ArrayList of pixels can be highlighted either by changing the colour or drawing a border around the object.

[select.png](https://drive.google.com/drive/u/0/folders/1AloTq81hQHoe3MZbq_z7msQYDjbtii3s)

**Right Click option:**

To create a popup menu to select among ‘Delete’, ‘Rotate’, ‘Change Colour’, a ContextMenu is created.

- When the user rightClicks on a particular position, first the  position is obtained and sent to the processing module to get the object (using ***select***).
- A popup menu is shown with 3 options 
	- Change Colour
	- Delete
	- Rotate
		- 90 deg
		- 180 deg
		- 270 deg

**Delete** 

- If the user chooses the Delete option, the processing module is notified about the delete option.
-The current pixel position is known to the processing module through the ***select (ArrayList <Position>)*** function, when the right-click was done.
- The ***delete()*** of the Processing Module is called to delete the selected object. 
- If any changes are made, the Processing Module will notify and ***getChanges()*** is called to know the changes and correspondingly the pixels are updated. 

**Rotate**

- First user has to select the object for which it wants to rotate.
- The position of the mouse click and angle of rotation will be passed to the Processing Module and it returns the object at that position if present.
- The rotate(double angleCCW) of the Processing module takes the angle of rotation and rotates the object.
- If any changes are made, the Processing Module will notify and getChanges() is called to know the changes and correspondingly the pixels are updated.

**Colour change**
- First the user has to select the object which it wants to change the colour. The position of the mouse click and Intensity (RGB values) will be passed to the Processing Module and it returns the object at that position if present.
- The colourChange(Intensity intensity) is called to change the colour of the object selected.
- If any changes are made, the Processing Module will notify and getChanges() is called to know the changes and correspondingly the pixels are updated. 

```java
/*
ContextMenu to select among the 3 actions to be performed on right-click when the cursor option is chosen
*/
public class ContextMenu extends Application{

    ContextMenu contextMenu = new ContexMenu();

    MenuItem delete = new MenuItem("Delete");

    MenuItem changeColour = new MenuItem("Change Colour");

    MenuItem rotate = new MenuItem("Rotate");
    MenuItem ninety = new MenuItem("90 deg");
    MenuItem oneEighty = new MenuItem("180 deg");
    MenuItem twoSeventy =  new MenuItem("270 deg");

    rotate.getItems().addAll(ninety,oneEighty,twoSeventy);
    contextMenu.getItems().addAll(delete,colour,rotate);

}
```

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

```java
public interface IChanges {

    void getChanges(rrayList<Pixel>);
    
}
```
[rightClick.png](https://drive.google.com/drive/u/0/folders/1AloTq81hQHoe3MZbq_z7msQYDjbtii3s)
