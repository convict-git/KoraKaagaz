# Design Spec
> By Ahmed Zaheer Dadarkar

# Objective

* Build utility class `Serialize` which would contain functions to serialize and deserialize objects which are to be sent accross the network
    * Write a static function `serialize` of the class `Serialize` to take an 
      object (which is serializable) and convert it into a serialized string
      which can be used to completely recover the object at the receiver end.
    * Write a static function `deserialize` of the class `Serialize` to take
      a serialized string and return the corresponding object. The dynamic type
      can be inferred from the `getClass` method of the object.
* Implement the functionality to build different kinds of shapes
    * Provide a `BoardObject` class which would generalize each shape by
      containing the list of pixels belonging to the object.
    * Write static functions for the `BoardObjectBuilder` class to create
      different kinds of shapes like Circles, Squares, Triangles and
      Line Segments as `BoardObject` objects.
* Add Persistence Support Functions at the Server Side
    * Write a store function to take a filepath, convert the Board State
      into a file and store it at that filepath. 
    * Write a load function to take a filepath and acquire the Board State
      from the file at that filepath.
* Add Utility Classes
    * Add utility classes for Username, User ID, Object ID, Intensity,
      Position, Pixel and Filepath.

# Serializing Objects

## Serialize a Serializable Object

1. First get the dynamic type of the object
2. Then convert the bytes of the object into a String
3. Just return the String

## Deserialize a Serialized String

2. Convert the data String of the object into an Object of Serializable type.
3. Return the Constructed Serializable Object

Notes:

- Here, by dynamic type I mean runtime type of the object as opposed to the
  static type determined by a base class or an interface.
- One may recover the original object with its accurate type by first calling
  the deserialize function on the serialized String and extracting the object
  and it's dynamic type, then (downcast) casting based on the dynamic type
  to get the accurate typed object.

# Shape functionality

Following are the provided standard shapes and the parameters
required to construct a `BoardObject` object of that shape.

### Circle

Requires center pixel position and radius in terms
of pixel units. An algorithm to compute the pixels from these parameters
would be provided in the `drawCircle` function of `BoardObjectBuilder`.

### Rectangle

Requires top left corner pixel position of square and bottom right
corner pixel position of rectangle. An algorithm to compute the pixels
from these parameters would be provided in the `drawRectangle` function of
`BoardObjectBuilder`.

### Triangle

Requires three pixel positions which would denote the three vertices
of the triangle. An algorithm to compute the pixels from these
parameters would be provided in the `drawTriangle` function
of `BoardObjectBuilder`.

### Line Segment

Requires two pixel positions which would denote the end points of the
line segment. An algorithm to compute the pixels from these
parameters would be provided in the `drawSegment` function
of `BoardObjectBuilder`.

# Persistence Support Functions at the Server Side

### Store Board State

Given the board state and local file path, the board state
is serialized and stored at the given file path.

### Load Board State

Given the local file path corresponding to a file which represents
a board state, we would build the board state using that file.

# Utility Classes

Utility classes for Username, User ID, Object ID, Intensity, Position, 
Pixel and Filepath would be provided. These classes are provided
for better readability.

# Interfaces

```java
    // The Serialize class provides the serialize and deserialize
    // functions for Serializable objects. The `deserialize`
    // function when applied on a string serialized by the
    // `serialize` function outputs the exact same object
    // and its dynamic type.
    public class Serialize {
        // The `serialize` function takes as input an object
        // `serialObj` which implements the Serializable interface
        // and outputs the object as a serialized String
        public static String serialize (Serializable serialObj);
        
        // The `deserialize` function takes as input a
        // String `serialString` which is the serialized form
        // of a Serializable object, and returns the object
        public static Serializable deSerialize (String serialString);
    }
```

```java
    
    // This functions in `BoardObjectBuilder` class
    // are responsible for computing the pixels
    // from the given parameters, updating
    // the board state maps, inserting into
    // the list of objects in the board state
    // and returning the built board object to the
    // caller.
    public class BoardObjectBuilder {
        // Construct a circle based on the
        // center, radius and color
        public static BoardObject drawCircle (
            Position center,
            Radius radius,
            Intensity intensity
        );
        
        // Construct a rectangle based on the
        // top left, bottom right positions and
        // the color
        public static BoardObject drawRectangle (
            Position topLeft,
            Position bottomRight,
            Intensity intensity
        );
        
        // Construct a triangle based on three
        // verticex coordinates and the color
        public static BoardObject drawTriangle (
            Position vertA,
            Position vertB,
            Position vertC,
            Intensity intensity
        );
        
        // Construct a line segment based on end
        // point coordinates of the segment 
        // and the color
        public static BoardObject drawSegment (
            Position pointA,
            Position pointB,
            Intensity intensity
        );
    }
    
    // Supportive Datatype for BoardObjectOperation
    // This datatype would be helpful to get the
    // type of operation that should be performed
    // on the object without giving information
    // about the parameters of the operation
    public enum BoardObjectOperationType {
        CREATE,        // Non-parametric operations
        DELETE,
        ROTATE,        // Parametric operations
        COLOR_CHANGE
    }
    // Note that CREATE and DELETE are non-parametric
    // operations in the sense that they do not require
    // parameters, while ROTATE and COLOR_CHANGE are
    // parametric operations because they require
    // the angle and intensity respectively
    
    // Note: If all operations were non-parametric, then
    // we could have just used the enum `BoardObjectOperationType`,
    // but since they are not we use the following interface
    // to represent the operations
    
    // This interface would provide the operation that would
    // be performed on the object.
    public interface BoardObjectOperation {
        // This method would give the type, which
        // would then help us to (downcast) cast
        // it into its original dynamic type for
        // retrieving the parameters
        public BoardObjectOperationType getOperationType();
    }
    
    // The Creation Operation - It has no parameters
    public class CreateOperation implements BoardObjectOperation {
        public CreateOperation ();
        public BoardObjectOperationType getOperationType ();
    }
    
    // The Deletion Operation - It has no parameters
    public class DeleteOperation implements BoardObjectOperation  {
        public DeleteOperation ();
        public BoardObjectOperationType getOperationType ();
    }
    
    // The Rotation Operation - It has the angle parameter
    public class RotateOperation implements BoardObjectOperation  {
        private Angle angleCCW;
        public RotateOperation (Angle angleCCW);
        public BoardObjectOperationType getOperationType ();
    }
    
    // The Color Change Operation - It has the intensity
    // parameter (i.e. color)
    public class ColorChangeOperation implements BoardObjectOperation  {
        private Intensity intensity;
        public ColorChangeOperation (Intensity intensity);
        public BoardObjectOperationType getOperationType ();
    }
    
    // The `BoardObject` class represents a shape
    // by storing the list of pixels which represent
    // that shape. An object of this class is what
    // is sent accross the network when it is created
    // or an operation is performed on it
    public class BoardObject implements Serializable {
        private ArrayList <Pixel> pixels;     // List of Pixels Representing the object
        private BoardObjectOperation boardOp; // The operation performed on this object
        private ObjectId objectId;            // The object ID
        private Timestamp timestamp;          // Time of creation of this object
        private UserId userId;           // User ID of the user who owns this object
        private ArrayList <Pixel> prevPixelIntensities; // previous intensities (color) of
                                                        // this object - required by undo
        private boolean isReset;         // Is this object a reset object ?
        // By reset object we mean an object which is built by an erase or clear
        // screen operation. This object then, cannot be rotated or color changed
        
        // Construct a Board Object using the list of pixels,
        // object ID, timestamp and user ID, and whether object is
        // reset object or not
        public BoardObject (
            ArrayList <Pixel> pixels,
            ObjectId objectId,
            Timestamp timestamp,
            UserId userId,
            boolean isReset
        );
        public BoardObjectOperation getOperation (); // Get the operation corresponding to this shape
        // Set the operation corresponding to this shape
        // This operation has special functionality for color change
        // operation - it stores previous intensities into the
        // `prevPixelIntensities` variable - required by undo
        public void setOperation (BoardObjectOperation boardOp);
        // Get the board object's list of pixels
        public ArrayList <Pixel> getPixels ();
        // Set the pixels using the given list of pixels
        public void setPixels (ArrayList <Pixel> pixels);
        // Get the user's User ID who owns this object
        public UserId getUserId();
        // Set user ID of the object
        public void setUserId(UserId userId);
        // Get object ID
        public ObjectId getObjectId();
        // Get timestamp of creation of object
        public Timestamp getTimestamp();
        // Is this a reset object ?
        public boolean isResetObject();
        // Get previous intensities of pixels
        public ArrayList <Pixel> getPrevIntensity();
    }
```

```java
    // This class provides functions for storing board
    // state at a location in the filepath, and retrieving
    // it back from the filepath
    public class PersistanceSupport {
        // Store Board State as file given by the filepath
        public static void storeState (
            BoardState boardState,
            Filepath filepath
        );
        
        // Load the Board State from file given by the filepath
        public static BoardState loadState (
            Filepath filepath
        );
    }
```

```java
    // This class corresponds to the Username
    public class Username {
        // The Username is internally stored as a String
        private String username;
        
        // Build username as string
        public Username(String username);
        public String toString(); // Conver Username to String
    }
    // This class corresponds to the User ID
    public class UserId {
        // The User ID is internally stored as a String
        private String userId;
        
        // Build user ID using the IP Address and
        // user name of the user
        public UserId(String ipAddress, Username username);
        public String toString(); // Convert User ID to String
    }
    
    // This class corresponds to the Object ID
    public class ObjectId {
        // The Object ID is internally stored as a String
        private String objectId;
        
        // Build Object ID using the user ID and timestamp
        public ObjectId(UserId userId, Timestamp timestamp);
        public String toString(); // Convert Object ID to String
    }
    
    // This class corresponds to RGB values
    public class Intensity {
        public int r, g, b; 
    }
    
    // This class corresponds to row and column of a position
    public class Position {
        public int r, c;
    }
    
    // This class corresponds to position
    // and intensity at that position
    public class Pixel {
        public Position position;
        public Intensity intensity;
    }
    
    // This class corresponds to the
    // file path
    public class Filepath {
        private String filepath;
        
        public Filepath(String filepath);
        public String toString();
    }
    
    // This class corresponds to the radius
    public class Radius {
        public double radius;
    }
    
    // This class corresponds to the angle
    public class Angle {
        public double angle;
    }
    
    // This class corresponds to the dimension
    public class Dimension {
        public int numRows, numCols;
    }
```

# Class diagram for Board Object and Related Classes

![](https://i.imgur.com/Hs8tzEB.png)

# Design Analysis

* All those classes mentioned above which have only static members will
  not be instantiated for objects. They are only used to group together
  similar functions.
* We thought about using JSON for storing the class type within the serialized
  String, but now we found that it is not needed since Java provides a way
  to get the dynamic type of the object using the `getClass` method of the
  topmost `Object` class.
* The `Serializable` interface does not require any methods to be implemented.
* The algorithms for computing the pixels from the parameters of different shapes
  would use slight approximations for digits behind the decimal point for the
  computed pixels since the screen is a 2D array of pixels (smallest object
  that can be manipulted is the pixel). 
* One may notice that the `BoardObject` class contains an arrayList of `Pixels`
  and not `Positions`. Although this may not be required in the current design
  since we only support single-colored objects. But, we can extend this to
  multi-colored objects later (if we have sufficient time). At a cost of adding
  an intensity along with every pixel position of the object, we may later be
  able to allow gradient like patterns for multi-colored objects.
* One may notice that we could have provided all operations that could be
  performed on objects as member functions (methods) of the `BoardObject`
  class. But we chose not to do so since we planned to instead create different
  classes (with static members) for each operation (like rotate, color change,
  delete) and let other team members handle it.
* Information about the operation to be performed on the board object is present
  within the `BoardObject` in the form of an object of `BoardObjectOperation`
  class named `boardOp`. Here, if all operations were non-parameteric (i.e. did
  not have parameters like angle or color), then we could have used just an enum
  type, but since that is not the case, we ended up building a single interface
  and a class for each operation.
* We would be providing utility classes which are inherently Strings itself.
  The reason for doing is that we can identify the object by the object name
  as well as its type. For example, we know from this `UserId userId` that
  `userId` represents a user's ID, this provides extended readibility (extension
  to what the object name provides, i.e. `userId`).

# Summary and Conclusion

* The serialize and deserialize methods can work on any object that is Serializable
  i.e. not just on board objects. This would be useful for other serializable 
  objects like the board state that have to be transferred using the network.
* The various algorithms for drawing different standard shapes would be present
  as static functions within the `BoardObjectBuilder` class.
* The `BoardObject` class is the class whose objects would be operated upon by
  various different operations like rotate, delete, undo, redo, color change.
  Objects of this class would be sent accross the network to inform the server
  and other clients about an operation performed on the object by a client.
* The store and load functions for storing and loading the board state in the
  the server's file system would be provided for use by the server which has
  to maintain persistance of the board object.