#Brush & Eraser Specification Document


## Objective
- To provide the user a brush to draw and erase fluent free handed drawing without using any shapes or other objects (shapes are covered by other team members).

## Design choice 

- We first considered many patterns and found out that subscriber-publisher pattern is the best.

- We found out that JavaFX was a better language to work with better than Swing

- And also MVC is a better model according to the team lead spec.


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

where: 
- Position(double x, double y)
- Intensity(int r, int g, int b)
- Pixel (Position pos , Intensity intensity)


**1. Brush:**

- Especially for a brush we need to set the size first and once we select it using the cursor on a button.
- The below code is not in exact form but is close to what is to be done.

- Tasks done by brush can be classified into three parts that can be implemented through the mouse functioning:

* Mouse press - From when the mouse is pressed we get the info of the pixel values that are on the click according to the brush size selected and store them in an ArrayList

* Mouse Drag - From the mouse press till the mouse release we count it as drag and the pixels that we pass through during the process is also added into the an ArrayList

* Mouse Release - It is the part at the end of drag and it includes the end points and marked as the end of the particular stroke and points are added to ArrayList


- Initially we see if there is a mouse press then we do the folllowing:

```java
canvas.setOnMousePressed(e->{
  if(drawbtn.isSelected()) {
    gc.setStroke(cpLine.getValue());
    gc.beginPath();
    gc.lineTo(e.getX(), e.getY());
  });
```

- What the above code will do is get the information on the pixels from the point we start drawing and keep it in a ArrayList.

```java
canvas.setOnMouseDragged(e->{
  if(drawbtn.isSelected()) {
    gc.lineTo(e.getX(), e.getY());
    gc.stroke();
  });
```

- What the above second part does is check and add more items to the Arraylist till the drag operation of the mouse is allowed.

```java
canvas.setOnMouseReleased(e->{
  if(drowbtn.isSelected()) {
    gc.lineTo(e.getX(), e.getY());
    gc.stroke();
    gc.closePath();
  });
```

- What the third part does is that it collects all the points and closes the path and ends the stroke.


- Finally the ArrayList is passed on to the processing team and the changed pixels are made to be in the canvas of all the boards after certain time intervals.



**2. Eraser:**

- Consider the initial as:

```java
public interface IDrawErase {
    // parameter: ArrayList<Pixel> - an ArrayList of pixel with RGB values for random curve object 
    void draw_curve(ArrayList <Pixel>);
    // parameter: ArrayList<Position> - an ArrayList of pixel position for erase object
    void erase(ArrayList <Position>);
}
```


- For an eraser we need to fix the size of the object(say square for us to be convinient in implementation).

- Then that small pixel object selected and pressed and the same way as brush the pixels changes are calculated.

- It is done in 3 steps;

* Mouse press - Initially the pixel pointing and according to size of object taken as eraser data of the (now white pixels) after the pressing of the mouse button in an ArrarList.

```java
canvas.setOnMousePressed(e->{
  if(drawbtn.isSelected()) {
    gc.setStroke(cpLine.getValue());
    gc.beginPath();
    gc.lineTo(e.getX(), e.getY());
  });
```

* Mouse Drag - When an object is moved while pressed we take in the data and calculate all the pixels affected and add them into the array

```java
canvas.setOnMouseDragged(e->{
  if(drawbtn.isSelected()) {
    gc.lineTo(e.getX(), e.getY());
    gc.stroke();
  });
```

* Mouse Release - The eraser when released gives the final array of points where the erasor ends and we select another function (brush or shape) to be done.

```java
canvas.setOnMouseReleased(e->{
  if(drowbtn.isSelected()) {
    gc.lineTo(e.getX(), e.getY());
    gc.stroke();
    gc.closePath();
  });
```

- Now in the eraser we need to add a few more calculations as we consider it as an square shaped object and we need to consider pixels that are erased according to the size of the eraser.

- Calculation or selecction of pixel will be easy if we consider erasor as white brush with a square of portion covered on each pixel it visits.

## Assembled with UI module
- The brush can be a different class / there is a draw class which contains all the shaped and free mouse draw.

- The above we will be doing as different class but if to look more sophisticated it can be included in one.

## Additional things that can be done if time permits
- Brush size is changed can be implemented as a popup.

- Eraser object type other than square.
