#### CS5617 Software Engineering
## Design Specifications

### Processing Team Member: Satchit Desai

## Objectives
* Processing curves
    * To create object of class `BoardObject` using `drawCurve`
    * The object is created from the pixels of the curve
    * Along with the pixels timestamp is also taken for the process of other functionalities
* Processing Eraser tool
    * Create a class `EraseCurve` which processes the eraser tool on the whiteboard
    * The object is created from the positions, it will be a white curve 
* Reset the entire whiteboard
    * Create class `ClearBoard` to remove all objects from the whiteboard
    * Supporting undo and redo
* Review Select and Delete
* Review Threading

## Design
#### 1. Processing Curves
* When a user draws a curve using the brush tool, UI provides all the pixels created between the MouseDown to MouseUp event.
* Unlike other shapes, a random curve cannot be formed from an expressable mathematical equation so here all pixels are needed to create an object
* Along with the pixels, timestamp is also taken in as input to provide execution of other functionalities like undo, redo, select and delete.
* The object is pushed into the client side stack for Undo-Redo functions
* The object is mapped from pixel to Priority queue based on Timestamp for implementation of Select and Delete.
* The above design is implemented in class `CurveBuilder` with a static class `drawCurve`.
* `drawCurve` is called without creating instance of `CurveBuilder`.

#### 2. Processing Eraser tool 
* When a user picks eraser, UI provides with the positions of the pixels which are to be erased.
* Similar to creating an object, here intensity of the pixels for all three channels will be 255.
* Therefore, a `BoardObject` object of the erased pixels is created which is then used for further processing.
* Along with all the information, there will be a flag named operation in the design. The flag suggests that the object created cannot be selected, deleted, rotated and colour-changed.
* The flag is not used for undo and redo. So the erased pixels can be withdrawn back and given forth.
* The above design is implemented in class `CurveBuilder` with a static class `eraseCurve`.
* `eraseCurve` is called without creating instance of `CurveBuilder`.
#### 3. Clear Board
* When a user wants clear the board, on click event a rectangle of dimensions same as that of the whiteboard with intensity 255 is put up on the board.
* Datatype Dimension is used to represent dimensions of the board. 
* To make it more selective, method `getDimension` gets dimensions of a rectangle which is to be cleared. Not the whole board but a part of it would be cleared.
* Method `setDimension` makes a white rectangle of given dimensions.
* `resetScreen` clears the whole white board without taking input of dimensions.
* Object created from this class has a flag similar to that of eraser.
* Thus, only undo can be done and the rectangle cannot be rotated, selected or deleted.

Design used here is an object based design and not a pixel conversion. Object based design gives scalability, flexbility and efficiency in the software. New features can be added to the software by adding new classes and methods. Numerous features can be implemented using objects and appropriate data structures. Pixel conversion would be easy to implement but adding features like undo, redo, select and delete would not come out to be feasible. An object has all necessary information that makes it flexible to use. Pixel based operations would often require to traverse through the whole board making it very time consuming. The design here implements all processing on objects making it highly efficient. 

## Activity Diagram
![](https://i.imgur.com/B4vf0s4.png)

    Fig.1 Flow diagram of the implementation is shown

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

public interface IDrawErase {
    // parameter: ArrayList<Pixel> - an ArrayList of pixel with RGB values for random curve object 
    void draw_curve(ArrayList <Pixel> pixels);
    // parameter: ArrayList<Position> - an ArrayList of pixel position for erase object
    void erase(ArrayList <Position> positions);
}
```

```java=
//Curve Brush

public class CurveBuilder
{
    // creates a new boardobject
    // extracts all the information
    // push object to undo stack
    //add object to map
    public static BoardObject drawCurve(ArrayList<Pixel> pixels); 
    // create a BoardObject for erase 
    // attach a flag variable with it
    //for identification to not to do 
    // rotate select delete
    public static BoardObject eraseCurve(ArrayList<Position> positions);
    // create a BoardObject with similar 
    // properties as the passed object
    // Note: this is a proxy function and will be called 
    // on undo for some deleted object 
    // therefore the created object is not pushed in undo stack
    public static BoardObject createObjectUtil(BoardObject obj);
}
```

```java=
// Clear Board

public class Reset
{
    // dimension datatype for dimensions of whiteboard
    private static Dimension dimension;
    
    // extracts dimensions from whiteboard
    public static Dimension getDimension();
    
    //set dimensions for a smaller part to be cleared
    public static void setDimension(Dimension dimension);
    
    //clear the whole board by 
    //putting a white rectangle on top everything
    public static BoardObject resetScreen();   
}
```

## Analysis
The processing of curves is done with an object based design. The design is much more robust, reliable in the terms of working and implementing. Class `CurveBuilder` is an umbrella for all the similar functions. Members it consists are static, so they can be called without instantiating the outer class.

`drawCurve` gives `BoardObject` which is an object and not pixel changes. Comparing to pixel  oriented method, here regardless of the pixels coming in we do not traverse the whole of white board for the changes. Pixel oriented takes O(n^2) to implement drawCurve changes. For Undo-Redo, `createObjectUtil` is called and not `drawCurve` because if an object is deleted and undo needs to be done then `createObjectUtil` creates the same object without pushing it into stack. Similarly, when an object is to be rotated, a `drawCurve` function is called with a newly mapped pixels.

`eraseCurve` gives `BoardObject` with a flag variable. Erase with pixel oriented method would be quite cumbersome because all positions should be changed to white intensity. And further modifications and functionalities cannot be supported. Worst case on pixel based erasing would be O(n^2) and that too without creating an object.

`createObjectUtil` gives `BoardObject` but here the object is not added to Undo stack. Rather this function is used as an proxy to undo an operation. For example, when an element on the board is deleted then to undo this operation the same object is created with all the information of the original object.

`Reset` clear whole or a part of the board. This class contains static objects and private variables to be used as utilities. Clear whole board is simply changing all pixels to white. But a simpler yet powerful idea is to make a white filled rectangle of the size board. This easily gives flexibilty that an object has. With an additional feature that is yet to be thought out with proper discussion is of clear just a part of board.

## Summary and Conclusion
* An efficient method to implement brush tool and process its curves was presented in this report.
* Eraser tool is implemented with subtle changes that in the brush tool.
* A proxy for draw and erase is created to reduce redundant processing in undo.
* A class to clear the board is created and reset the whole board, supporting undo.
* This design is purely based on objects and well thought through.
* We look forward to implement it.



