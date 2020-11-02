# DESIGN SPEC
> by Devansh Singh Rathore - 111701011

### Overview
To implement color change of objects:
* Receive objectID of the object(theoritically from server or UI), userID of new-user who lastly edited the object, and intensity(final one).
* Set intensity of each of the pixel from previous object, as the input intensity. Hence, we create a new pixel set in a local arraylist(`ArrayList <Pixel>`).
* Set `BoardObjectOperation` as COLOR_CHANGE, with previous intensity, into a local variable.
* Call for deletion of the previous object in all the maps, through `removeObjectFromMaps` public static function in `SelectDeleteUtil` class. But in this step, note that the undo-redo stack is not altered. Here a call will take place to update changes to UI, to change previous object's pixel state according to maps.
* Send the received `userID` as new-user's `user-ID`, the pixel set, the local variable for `BoardObjectOperation` and rest of the parameters from previous object stored in local variables, to the `drawCurve` public static function. But in this step, note that the undo-redo stack is not altered. Here a call will take place to update changes to UI, to change new updated object's pixel's state according to the maps.
* Call for updating the undo-redo stack to public static function `pushIntoStack` by passing the `BoardObject`, via the `stackUtil` function in `ParameterizedOperationsUtil`.
* Return the ObjectID.

To implement rotation of objects(angles in multiples of 90$^\circ$):
* Receive objectID of the object(theoritically from server or UI), userID of new-user who lastly edited the object, and angleOfRotation(in counter-clockwise direction).
* From the pixel set of previous object, deduce the center coordinates of object around which rotation will take place.
* Find the rotation matrix using centre coordinates and angle of rotation.
* Create a new local arraylist to store updated pixels after passing the previous object's pixels through rotation matrix.
* Set `BoardObjectOperation` as ROTATE, with anlgeOfRotation, into a local variable.
* Call for deletion of the previous object in all the maps, through `removeObjectFromMaps` public static function in `SelectDeleteUtil` class. But in this step, note that the undo-redo stack is not altered. Here a call will take place to update changes to UI, to change previous object's pixel state according to maps.
* Send the received `userID` as new-user's `user-ID`, the pixel set, the local variable for `BoardObjectOperation` and rest of the parameters from previous object stored in local variables, to the `drawCurve` public static function. But in this step, note that the undo-redo stack is not altered. Here a call will take place to update changes to UI, to change new updated object's pixel's state according to the maps.
* Call for updating the undo-redo stack to public static function `pushIntoStack` by passing the `BoardObject`, via the `stackUtil` function in `ParameterizedOperationsUtil`.
* Return the ObjectID.

To implement test harness:
* `Executive` and `TestHarness` class.
* `ITest` interface.

To review other processing module sections:
* review Undo-Redo
* review `getChanges` that sends updated pixels to UI.

[OPTIONAL] To extend rotation operation to any angle:
* Since there are issues regarding change of number of pixels after rotation, we can modfiy rotation operation by edge preserving technique and some digital image processing techniques.

### UML - Activity Diagram
![](https://i.imgur.com/xxjrJL6.png)


### Design
* The color change and rotation operations are performed by processing module instead of UI (different than the case of 'Object Creation') because UI does not keep record of the objects in a `BoardState` and thus cannot map pixels under cursor to a particular object. So, for the same, processing module intervenes.
* For both the operations(rotation and color-change); classes, interfaces and functions are written in such a way, that it does not depend on, whether the update is done by system user(inputs received from system UI) or from the server processing module(updates done on other client's system). This prevents writing multiple modules with almost similar tasks.
* `Angle`, `Intensity`, `Position`, `UserID`, `ObjectID` classes are specifically created to make it more readable, without considering the details about their primary data types.
* `Ioperation` interface is provided to the UI, which consists of all major operations(are thus abstract) performed by the processing module, and do not need instantiation.
* The `stackUtil` is kept private inside `ParameterizedOperationsUtil`, because it's just a utility function and thus won't be called directly by any other section/modules. This function basically calls the `pushIntoStack` function present in `UndoRedo` class. It gets input from the output received from `drawCurve` operation call.
* `BoardOperation` data member inside the `BoardObject` ensures that when `removeObjectFromMaps` and `drawCurve` are called, the object is never put into 'undo-redo' stack by them, because if it happens the undo stack will have replica of the same object once with DELETE, once with INSERT and once with ROTATE/COLOR_CHANGE; instead of just a single ROTATE/COLOR_CHANGE operation.
#### Color Change
1. Instead of keeping color(intensity) as a property of a `BoardObject`, we have kept color(intensity) for each indivisual pixel in a `BoardObject`. This is to add flexibility in model, incase we allow multiple intensities within a same object, in future.
1. The `colorChange` function in `IOperation` takes input of `Intensity` class, calls public static function `colorChange` in `ParameterizedOperationsUtil` class after retreiving all the necessary parameters from the selected object.
1. The `colorChange` function in `ParameterizedOperationsUtil` class takes `BoardObject`(previous object) and `Intensity`(new intensity) as parameters and returns `ObjectID` of the new object created to the 'Client Side Processing' section, so that it can be forwarded to the processing server according to needs. Since this function gets called from 'Client Side Processing' section, it is kept as a public function.
1. The `colorChangeUtil` function is kept public as it will be called by 'undo-redo' section.
1. The `colorChangeUtil` will be used to store previous object into local variables, update its intensities, send previous object for deletion, creation of new object(with updates in the maps). The call for deletion and creation of object is to avoid rewriting the existing utilities in other sections of processing module.
1. The `colorChange` function sequentially calls `colorChangeUtil`, `stackUtil` functions. The reason of this breakup is: during 'Undo' operation on any object with `BoardOperation` type as COLOR_CHANGE, only `colorChangeUtil` will be called because after undo, the object is shifted into redo stack, and we don't want the object to again enter into the undo stack, which `colorChange` function actually does. So, `colorChange` will be called by 'Client Side Processing' section and `colorChangeUtil` will be called by 'undo-redo' section.
#### Rotate Objects
1. The `rotate` function in `IOperation` takes input of `Angle` class, calls public static function `rotate` in `ParameterizedOperationsUtil` class after retreiving all the necessary parameters from the selected object.
1. The `rotate` function in `ParameterizedOperationsUtil` class takes `BoardObject`(previous object) and `Angle`(angleOfRotation) as parameters and returns `ObjectID` of the new object created to the 'Client Side Processing' section, so that it can be forwarded to the processing server according to needs. Since this function gets called from 'Client Side Processing' section, it is kept as a public function.
1. The `rotateUtil` function is kept public as it will be called by 'undo-redo' section.
1. The `rotateUtil` will be used to store previous object into local variables, get updated pixels in arraylist as `ArrayList <Pixel>`, send previous object for deletion, creation of new object(with updates in the maps). The call for deletion and creation of object is to avoid rewriting the existing utilities in other sections of processing module.
1. The `rotate` function sequentially calls `rotateUtil`, `stackUtil` functions. The reason of this breakup is: during 'Undo' operation on any object with `BoardOperation` type as ROTATE, only `rotateUtil` will be called because after undo, the object is shifted into redo stack, and we don't want the object to again enter into the undo stack, which `rotate` function actually does. So, `rotate` will be called by 'Client Side Processing' section and `rotateUtil` will be called by 'undo-redo' section.
1. For rotation of an object, rotation matrix and vector-maps will be used because they are efficient in time complexity i.e `O(number_of_pixels_to_be_rotated)`.
1. `findCentre` function is a private utility function in `ParameterizedOperationsUtil` class to return a `Position` of centre from passed `ArrayList<Pixel>` of an object. It is kept private because, is not being used by any other section.
1. Similarly `rotationMatrix` function takes an `Angle` and `Position` of centre pixel to deduce the required rotation matrix in a 2D array form. It is just a local utility function, thus kept private.

### Interfaces
```java
/*
 * This interface is used by UI and conducted by 'Client
 * Side Processing', to call every major functionality
 * sections.*/
public interface IOperation {
    /* returns the pixel position for selected object, if
     * an object exists at the selected position
     * */
    ArrayList<Position> select (ArrayList<Position> positions);

    // deletes the selected object
    void delete ();

    /* changes color of selected object to the specified
     * intensity
     * */
    void colorChange (Intensity intensity);

    /* rotates selected object by specified angle, in
     * counter clockwise direction
     * */
    void rotate (Angle angleCCW);

    // removes all the object drawn
    void reset ();
}
```

### Classes

```java
/* Supports operations with parameters like `Angle` and
 *  `Intensity`.
 * */
public class ParameterizedOperationsUtil{
    /*
     * Operation: calculate average x-coordinate &
     * y-coordinate as centre pixel postion
     * Input: pixels of an object
     * Ouput: centre pixel position
     * */
    private static Position findCentre(ArrayList<Pixel> pixels);

    /*
     * Operation: suitable rotation matrix
     * Inputs: angleOfRotation, centre `Position`.
     * Output: 2D rotation matrix
     * */
    private static double[][] rotationMatrix(Angle angle, Position centre);

    /*
     * Operation: call 'pushIntoStack' for undo-redo
     * Inputs: rotated/color-changed object
     * Output: void
     * */
    private static void stackUtil(BoardObject newObj);

    /*
     * Operation: change pixel colors, delete previous
     * object, create updated object
     * Inputs: previous object, and final intensity
     * Output: updated object with color changed
     * */
    public static BoardObject colorChangeUtil(BoardObject obj, Intensity intensity);

    /*
     * Operation: Color change
     * Inputs: Intensity, previous object
     * Output: objectID of updated object
     * */
    public static ObjectId colorChange(BoardObject obj, Intensity intensity);

    /*
     * Operation: find new pixel set after rotation,
     * delete previous object, create updated object
     * Inputs: previous object, and angleOfRotation
     * Output: updated rotated object
     * */
    public static BoardObject rotationUtil(BoardObject obj, Angle angleOf Rotation);

    /*
     * Operation: Rotation
     * Input: previous object, and angleOfRotation
     * Output: Object ID of updated object
     * */
    public static ObjectId rotation(BoardObject obj, Angle angleOfRotation);
}
```

### Helper Classes
```java
// corresponds to pixel color
public class Intensity {    
    // RGB values at a pixel position
    public int r, g, b;         
}

// corresponds to pixel position
public class Position {
    // x and y coordinates repectively
    public int x, y;            
}

// pixel position with RGB values
public class Pixel {            
    public Intensity intensity;    
    public Position position;
}

// corresponds to angle of rotation
public class Angle {
    public double theta;
}

 /* The `BoardObject` class represents a shape by storing
  * the list of pixels which represent that shape.
  * */
public class BoardObject implements Serializable {
    // list of pixels
    private ArrayList <Pixel> pixels;
    // the operation corresponding to this shape
    private BoardObjectOperation boardOp;
    // the Object's ID
    private ObjectId objectId;
    /* This timestamp is set inside the constructor using
     * java library methods
     * */
    private Timestamp timestamp;
    // User ID of user who was last to modify it
    private UserId userId;
    /* previous intensity of object. It is
     * useful during undo-redo when operation is
     * COLOR_CHANGE
     * */
    private Intensity prevIntensity;
    /* Is object a reset object. It helps to prevent user
     * at UI to select white object used to create Reset
     * operation */
    private boolean isReset;
    /* Is object a erase object. It helps to prevent user
     * at UI to select white object used to create erase
     * operation */
    private boolean isErase;

    /* Constructor, by default sets the shape operation to
     *  be CREATE operation
     * */
    public BoardObject (
        ArrayList <Pixel> pixels,
        ObjectId objectId,
        Timestamp timestamp
    );
    // Get operation corresponding to this shape
    public BoardObjectOperation getOperation ();
    // Set the operation corresponding to this shape
    public void setOperation (BoardObjectOperation boardOp);
    // Get the BoardObject's pixels
    public ArrayList <Pixel> getPixels ();
    // Get object ID
    public void setPixels (ArrayList <Pixel> pixels);
    // Get user ID
    public UserId getUserId();
    // Set user ID
    public void setUserId(UserId userId);
    // Get object ID
    public ObjectId getObjectId();
    // Get timestamp
    public Timestamp getTimestamp();
    // Is reset object ?
    public boolean isResetObject();
    // Give previous intensity
    public Intensity peekPrevIntensity();
```

### Analysis
* For the case discussed in bullet point 1, under the section Design/ColorChange, method remains effecient in terms of time complexity in both the cases, i.e. `O(number_of_pixels_in_object)`, because anyway we have to send the updated pixels to UI, which is assembled in time complexity of `O(number_of_pixels_in_object)`. And moreover, it makes our design more scalable.
* The design ensures that interfaces and static functions remain same irrespective of whether it is being called by UI or server(through network). Hence, the design has higher usability.
* As the initial task, rotation is applied with angle of rotation in mulitples of 90$^\circ$. But the same function can be still used for any angle of rotation, adding flexibilty to rotation module.
*  Intensity change as well as applying rotation matrix to pixels, both take a time complexity of `O(number_of_pixels_in_object)`.
*  Rest of the time complexity depends on `O(deletion of object)`, `O(creation of object)`, `O(updating undo-redo stack)`.
* `BoardObjectOperation` type object `boardOp` could have been `enum`, but because Rotation and ColorChange operations needed extra parameters to store its object operation state i.e. Angle and Intensity repectively; thus we have built a interface `BoardObjectInterface` and a class for each of the operation.
* `Angle`, `Intensity`, `Position`, `UserID`, `ObjectID` classes are specifically created to make it more readable, without considering the details about their primary data types.
* Since there are no usage of data-structure resources like maps, stacks, etc., there is no need of keeping functions synchronized. And hence it remains secure from issues like inconsistency.
### Summary and Conclusions
Color change operation is currently based on the assumption that an object possesses mono-colored pixels. But as we have set Intensity to be a property of pixel instead of complete object, the design can be easily manipulated to support multi-colored objects.

Rotation is currently done using Rotation matrix method. But since it has minor issues regarding the distortion of edges(which is nearly negligible in bigger sized objects), rotation can be applied using some Digital Image Processing techniques with minimal possible time complexities involved.
