# Shapes and Processing Module Listener Specification Document

## Objective
-To provide the user to draw different shapes and update the canvas with the incoming data stream from the processing module


### Publisher-Subscriber model
- We need to communicate with the Processing module in order to send the changed pixels and to receive the pixels from other users.
- In order of better interaction purpose, we would be using the Publisher-Subscriber pattern.
- Publisher-Subscriber is a message pattern, where the message senders (Publishers) do not program the messages to be sent directly to specific receivers (subscribers), but instead categorize published messages into classes without knowledge of which subscribers, if any, there may be. Similarly, subscribers express interest in one or more classes and only receive messages that are of interest, without knowledge of which publishers, if any, there are. This is how the Publisher-Subscriber model works.
- Here, the UI module will subscribe to the processing module and send the respective notification handler.
- So when there is message at the processing module, they will notify the UI module by calling the provided notification handlers.
- And when we need to send the message to the processing module, we will create the object and send the message using the functions present in it.

### JavaFX and Scene Builder
- To desing the GUI of a desktop application there are many tools available eg : JavaFX, Swing, AWT etc.
- Currently we would be using javaFX for adding shapes to our canvas.
- **JavaFX** is a set of graphics and media packages that enables developers to design, create, test, debug, and deploy rich client applications that operate consistently across diverse platforms.
- JavaFX is the successor of Swing which includes FXML and CSS files which eases the designing and formatting of the web pages.
- **JavaFX Scene Builder** is a visual layout tool that lets users quickly design JavaFX application user interfaces, without coding. Users can drag and drop UI components to a work area, modify their properties, apply style sheets, and the FXML code for the layout that they are creating is automatically generated in the background. The result is an FXML file that can then be combined with a Java project by binding the UI to the application’s logic.
- We would be using javaFX Scene Builder to create the view of shapes to select.
- Scene Builder will be beneficial because we can add various options and features of UI (eg: Button, Checkbox etc... ) easily.


### FrameWork : MVC Pattern

-  Model
    The Model describes the business logic and it is characterized by a set of classes. It works on to design business rules for data on how the data is changed or handled.
- View
    The View here represents the UI components. The View displays the data which the controller sends back as a form of the result. The Model also can be converted into the UI using View.

- Controller
    For proceeding incoming requests, the controller is highly responsive. Through the Model to View, the Controller gets the data of the user. Between the Model and the View, the Controller acts as a facilitator.


    
- Here, the view will be built using javaFX scene builder. It will be a fxml file. CSS properties can be used for providing various attributes like size of text area, colour etc...


### Pixel
- For pixel representation we will be using the below code:

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

- Position(double x, double y)
- Intensity(int r, int g, int b)
- Pixel (Position pos , Intensity intensity)

### Shapes interface
- On click provides an menu of standard shapes like circle, rectangle, square and line segment.
- Also provides the option to select the colour of the shape.
- Any shape can be drawn on the canvas by dragging the mouse after selecting the shape. 
``` java
public interface IDrawShapes {
    // parameter: Pixel start - position of mouse drag started with RGB values
    //                 Pixel end -  position of mouse drag ended with RGB values
    void drawCircle(Pixel start, Pixel end);
    void drawSquare(Pixel start, Pixel end);         
    void drawRectangle(Pixel start, Pixel end); 
    void drawLine(Pixel start, Pixel end);        
    void drawOval(Pixel start, Pixel end); 
    void drawTriangle(Pixel start, Pixel end);   
    void drawRightTriangle(Pixel start, Pixel end); 
}
```
- There is a fixed border size for all the shapes.
- We will be using the methods inside Canvas.GraphicsContext() class to draw shapes.
- Given are some methods we are going use:
```java
//Parameters: w - Width of rectangle
//	     h - Height of rectangle
//                   x - x value of start point
//	     y - y value of start point
// Width and Height can be calculated with start and end positions of mouse drag 

strokeRect(double x, double y, double w, double h)
//Strokes a rectangle using the current stroke paint.

strokeOval(double x, double y, double w, double h)
//Strokes an oval using the current stroke paint.

//Parameters:  x2 - x value of end point
//	     y2 - y value of end point
//                   x1 - x value of start point
//	     y1 - y value of start point
strokeLine(double x1, double y1, double x2, double y2)
//Strokes a line using the current stroke paint.
```

### Processing Module Listener

```javascript=
public interface IChanges {
    void getChanges(ArrayList<Pixel>);
}
```

- We can update the canvas with setColor method provided by Canvas.GraphicsContext.PixelWriter() class
```java
void setColor(int x, int y, Color c)
//Stores pixel data for a Color into the specified coordinates of the surface.
//Parameters: x - the X coordinate of the pixel color to write
//	    y - the Y coordinate of the pixel color to write
//	    c - the Color to write or null
```


 
