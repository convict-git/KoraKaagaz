# Design Spec
> By Ahmed Zaheer Dadarkar

# Objective

* Build utility class `Serialize` which would contain functions to serialize and
  deserialize objects which are to be sent accross the network. It would also
  be used to serialize the board state so that it may be stored in (and later
  retrieved from) the filesystem for persistence.
    * Write a static function `serialize` of the class `Serialize` to take an 
      object (which is serializable) and convert it into a serialized string
      from which we can completely recover the original object.
    * Write a static function `deSerialize` of the class `Serialize` to take
      a serialized string and return the corresponding object. The dynamic type
      can be inferred from the `getClass` method of the object.
* Implement the functionality to build different kinds of shapes
    * Provide a `BoardObject` class which would generalize each shape by
      containing the list of pixels belonging to the object.
    * Write static functions for the `BoardObjectBuilder` class to create
      different kinds of shapes like Circles, Squares, Triangles and
      Line Segments as `BoardObject` objects.
* Add Persistence Support Functions at the Server Side
    * Write a function to take a Serialized Board State String and a Board ID and store
      the Board State as a file at the filepath provided. 
    * Write a function to take a Board ID, read the serialized String from the
      file containing that String.
* Add Utility Classes
    * Add utility classes for Angle, BoardId, BrushRadius, Dimension, Filepath,
      Intensity, ObjectId, Pixel, Port, Position, Radius, Timestamp, UserId and
      Username.

# Serializing Objects

## Serialize a Serializable Object

1. First convert the object into a byte stream.
2. Then encode the byte stream to a String using the `ISO-8859-1` charset. The
   reason for using this charset is that this charset is a one-to-one mapping
   between bytes and characters, this allows one to retrieve the exact same bytes
   back from the String by decoding it using `ISO-8859-1` charset.
3. Return the encoded String as the serialized String of the provided object.

## Deserialize a Serialized String

1. Convert the Serialized String into bytes using `ISO-8859-1` charset.
2. Get the object as an object of type `Serializable` from the bytes of
   the object.
3. Return the Serializable object as the recovered object. One may cast it
   back to it's dynamic (actual) type by downcasting it at runtime.

Notes:

- UTF-8 and ASCII charsets do not have one-to-one mapping between bytes and
  characters since UTF-8 is a multibyte encoding and ASCII only has character
  values between 0 to 127 (both inclusive).
- One may recover the original object with its accurate type by first calling
  the `deSerialize` function on the serialized String and extracting the object,
  then downcasting it to its dynamic type.
- Here, by dynamic type I mean runtime type of the object as opposed to the
  static type determined by a base class or an interface.
- One can determine the dynamic type of an object by using the `getClass` method
  of that object.

# Shape functionality

Following are the provided shapes and the parameters required to construct
a `BoardObject` object of that shape. Note that along with the parameters
described below, all these shape functionalities take a color (pixel intensity)
parameter which sets the color of the shape.

### Circle

Functionality to construct a shape representing the boundary of a Circle.
Requires center pixel position and radius in terms of pixel units. `drawCircle`
method of `BoardObjectBuilder` shall provide this functionality.

### Filled Circle

Functionality to construct a shape representing a Filled Circle.
Requires center pixel position and radius in terms of pixel units. `drawCircleFill`
method of `BoardObjectBuilder` shall provide this functionality.

### Rectangle

Functionality to construct a shape representing the boundary of a Rectangle.
Requires the top left corner pixel position and bottom right corner
pixel position of the rectangle. `drawRectangle` method of `BoardObjectBuilder`
shall provide this functionality.

### Filled Rectangle

Functionality to construct a shape representing a Filled Rectangle.
Requires the top left corner pixel position and bottom right corner
pixel position of the rectangle. `drawRectangleFill` method of `BoardObjectBuilder`
shall provide this functionality.

### Triangle

Functionality to construct a shape representing the boundary of a Triangle.
Requires three pixel positions which would denote the three vertices
of the triangle. `drawTriangle` method of `BoardObjectBuilder` shall provide this
functionality.

### Line Segment

Functionality to construct a shape representing a Line Segment.
Requires two pixel positions which would denote the end points of the
line segment. `drawSegment` method of `BoardObjectBuilder` shall provide this
functionality.

# Persistence Support Functions at the Server Side

### Storing the Board State in a File

Given the Board State String and the Board ID, the board state
String is stored in the file system.

### Loading the Board State from a File

Given the Board ID, read the file which contains the Board State String
and return the String.

# Utility Classes

Utility classes for Angle, BoardId, BrushRadius, Dimension, Filepath,
Intensity, ObjectId, Pixel, Port, Position, Radius, Timestamp, UserId and
Username shall be provided. These classes are provided for better readability.

# Interfaces

```java
    /**
     * The Serialize class provides the serialize and deserialize
     * functions for Serializable objects. The `deSerialize`
     * function when applied on a string serialized by the
     * `serialize` function outputs the exact same object
     */
    public class Serialize {
        /**
         * The `serialize` function takes as input an object `serialObj`
         * which implements the Serializable interface and outputs the
         * object as a serialized String
         */
        public static String serialize (Serializable serialObj);
        
        /**
         * The `deSerialize` function takes as input a String `serialString`
         * which is the serialized form of a Serializable object and returns
         * the corresponding object
         */
        public static Serializable deSerialize (String serialString);
    }
```

```java
    
    /** 
     * The functions in `BoardObjectBuilder` class are responsible for computing
     * the pixels of the corresponding shape from the given parameters, updating
     * the board state and returning the built board object to the caller.
     */
    public class BoardObjectBuilder {
    
        /** Constructs a circle based on the center, radius and intensity (color) */
        public static BoardObject drawCircle (
            Position center,
            Radius radius,
            Intensity intensity
        );
        
        /** Constructs a filled circle based on the center, radius and intensity (color) */
        public static BoardObject drawCircleFill (
            Position center,
            Radius radius,
            Intensity intensity
        );
        
        /**
         * Constructs an axis parallel rectangle based on the top left, bottom right
         * positions and the intensity (color) 
         */
        public static BoardObject drawRectangle (
            Position topLeft,
            Position bottomRight,
            Intensity intensity
        );
        
        /**
         * Constructs an axis parallel filled rectangle based on the top left, bottom
         * right positions and the intensity (color) 
         */
        public static BoardObject drawRectangleFill (
            Position topLeft,
            Position bottomRight,
            Intensity intensity
        );
        
        /** Constructs a triangle based on three vertex coordinates and the intensity (color) */
        public static BoardObject drawTriangle (
            Position vertA,
            Position vertB,
            Position vertC,
            Intensity intensity
        );
        
        /**
         * Construct a line segment based on end point coordinates of the line segment
         * and the intensity (color)
         */
        public static BoardObject drawSegment (
            Position pointA,
            Position pointB,
            Intensity intensity
        );
    }
    
    /**
     * Supportive Datatype for BoardObjectOperation. This datatype would be helpful
     * to get the type of operation that should be performed on the object without
     * giving information about the parameters of the operation
     */
    public enum BoardObjectOperationType {
        CREATE,        /** Non-parametric operations begin */
        DELETE,
        ROTATE,        /** Parametric operations begin */
        COLOR_CHANGE
    }
    
    /**
     * Note that CREATE and DELETE are non-parametric operations in the sense that
     * they do not require parameters, while ROTATE and COLOR_CHANGE are parametric 
     * operations because they require the angle and intensity respectively
     *
     * Note: If all operations were non-parametric, then we could have just used the
     * enum `BoardObjectOperationType`, but since they are not we use the following
     * interface to represent the operations
     */
    
    /** This interface would provide the operation that would be performed on the object */
    public interface IBoardObjectOperation {

        /**
         * This method would give the type, which would then help us to (downcast) cast
         * it into its original dynamic type for retrieving the parameters
         */
        public IBoardObjectOperationType getOperationType();
    }
    
    /** The Creation Operation - It has no parameters */
    public class CreateOperation implements IBoardObjectOperationType {
        public CreateOperation ();
        public BoardObjectOperationType getOperationType ();
    }
    
    /** The Deletion Operation - It has no parameters */
    public class DeleteOperation implements IBoardObjectOperationType  {
        public DeleteOperation ();
        public BoardObjectOperationType getOperationType ();
    }
    
    /** The Rotation Operation - It has the angle parameter */
    public class RotateOperation implements IBoardObjectOperationType  {
        private Angle angleCCW;
        public RotateOperation (Angle angleCCW);
        public BoardObjectOperationType getOperationType ();
        public Angle getAngle();
    }
    
    /** The Color Change Operation - It has the intensity parameter (i.e. color) */
    public class ColorChangeOperation implements IBoardObjectOperationType  {
        private Intensity intensity;
        public ColorChangeOperation (Intensity intensity);
        public BoardObjectOperationType getOperationType ();
        public Intensity getIntensity();
    }
    
    /**
     * The `BoardObject` class represents a shape by storing an ArrayList of pixels
     * which represent that shape. An object of this class is what is sent over the
     * network when it is created or an operation is performed on it
     */
    public class BoardObject implements Serializable {
        /** List of Pixels Representing the object */
        private ArrayList <Pixel> pixels;
        
        /** The operation performed on this object */
        private IBoardObjectOperation boardOp; 
	
        /** The object ID */
        private ObjectId objectId;
	
        /** Time of creation of this object */
        private Timestamp timestamp;
	
        /** User ID of the user who owns this object */
        private UserId userId;
	
        /** previous intensities (color) of this object, it is required by undo */
        private ArrayList <Pixel> prevPixelIntensities;
	
        /** 
         * Boolean Variable Indicating whether object is a reset object or not 
         * 
         * By reset object we mean an object which is built by an erase or clear
         * screen operation. This object then, cannot be rotated or color changed
         */
         private boolean isReset;
        
        /**
         * Constructs a Board Object using the list of pixels, object ID, timestamp,
         * user ID and information about whether the object is a reset object or not
         */
        public BoardObject (
            ArrayList <Pixel> pixels,
            ObjectId objectId,
            Timestamp timestamp,
            UserId userId,
            boolean isReset
        );
        
        /** Gets the operation corresponding to this shape */
        public IBoardObjectOperation getOperation();
	  
        /** Sets the operation to be performed on this board object */
        public void setOperation(IBoardObjectOperation boardOp);
        
        /** Gets the board object's list of pixels */
        public ArrayList <Pixel> getPixels();
        
        /** Sets the pixels using the given array-list of pixels */
        public void setPixels(ArrayList <Pixel> pixels);
        
        /** Gets the board object's list of positions */
        public ArrayList <Position> getPositions();
        
        /** Sets the pixels using the given array-list of pixels */
        public void setPixels (ArrayList <Pixel> pixels);
        
        /** Gets the user's User ID who owns this board object */
        public UserId getUserId();
        
        /** Sets user ID of this board object */
        public void setUserId(UserId userId);
        
        /** Gets object ID */
        public ObjectId getObjectId();
        
        /** Gets timestamp of creation of object */
        public Timestamp getTimestamp();
        
        /** Returns true if this a reset object, else false */
        public boolean isResetObject();
        
        /** Sets previous intensities of pixels */
        public void setPrevIntensity(ArrayList <Pixel> prevPixelIntensities) ;
        
        /** Gets previous intensities of pixels */
        public ArrayList <Pixel> getPrevIntensity();
    }
```

```java

    /**
     * This class provides functions for storing board state string at
     * in the filesystem, and retrieving it back from the filesystem
     */
    public class PersistanceSupport {
    
        /** Stores the Board State String as a file in the filesystem */
        public static void storeStateString (
            String boardStateString,
            BoardId boardId
        );
        
        /** Loads the Board State String from the file containing the String */
        public static String loadStateString (
            BoardId boardId
        );
    }
```

```java
    /** Class Representing an Angle */
    public class Angle {
        /** Angle as a double value */
        public double angle;
        
        /** Angle Constructor */
        public Angle(double angle);
    }
    
    /** Class Representing a Board ID */
    public class BoardId {
        /** Board ID String */
        private String boardId;
        
        /** BoardId Constructor */
        public BoardId(String boardId);
        
        /** Converts to String */
        public String toString();
    }
    
    /** Class Representing a Brush Radius */
    public class BrushRadius {
        /** The Brush Radius */
        public double brushRadius; 
        
        /** BrushRadius Constructor */
        public BrushRadius(double brushRadius);
    }
     
    /** Class Representing the Dimension */
    public class Dimension {
        /** Number of rows and Number of columns */
        public int numRows, numCols;
        
        /** Dimension Constructor */
        public Dimension(int numRows, int numCols);
    }
    
    /** Class Representing a Filepath */
    public class Filepath {
        /** Filepath String */
        private String filepath;
        
        /** Filepath Constructor */
        public Filepath(String filepath);
        
        /** Converts to String */
        public String toString();
    }
    
    /** Class Representing Pixel Intensity */
    public class Intensity {
        /** red, green and blue respectively */
        public int r, g, b;
        
        /** Intensity Constructor */
        public Intensity(int red, int green, int blue);
    }
    
    /** Class Representing the Board Object ID */
    public class ObjectId {
        /** Object ID as a String */
        private String objectId;
        
        /** Builds Object ID using User ID and Timestamp */
        public ObjectId(UserId userId, Timestamp timestamp);
        
        /** Converts to String */
        public String toString();
    }
    
    /** Class Representing a Pixel */
    public class Pixel {
        /** Position of this pixel on the Board */
        public Position position;
        
        /** Intensity of this pixel */
        public Intensity intensity;
        
        /** Constructor for Pixel class */
        public Pixel(Position position, Intensity intensity);
    }
    
    /** Class Representing a Port */
    public class Port {
        /** Port Number as an integer value */
        public int port;
        
        /** Port Constructor */
        public Port(int port);
    }
    
    /** Class Representing a Position on the Board */
    public class Position {
        /** Row and Column respectively */
        public int r, c;
        
        /** Position Constructor */
        public Position(int row, int column);
    }
    
    /** Class Representing a Radius value */
    public class Radius {
        /** The radius as a double value */
        public double radius;
        
        /** Radius Constructor */
        public Radius(double radius);
    }
    
    /** Class Representing a Timestamp */
    public class Timestamp {
        /** Timestamp is internally stored as a Date */
        private Date date;
        
        /** Timestamp Constructor */
        public Timestamp(Date date);
        
        /** Converts to String */
        public String toString();
        
        /** Converts to Date */
        public Date toDate();
    }
    
    /** Class Representing a user's User ID */
    public class UserId {
        /** User ID is stored as a String */
        private String userId;
        
        /**
         * User ID Constructor
         *
         * Construct the User ID using the user's machine's IP Address and user's Username
         */
        public UserId(IpAddress ipAddress, Username username)
        
        /** Converts to String */
        public String toString();
        
        /** Gets the Username present in the User ID */
        public Username getUsername();
    }
    
    /** Class Representing a user's Username */
    public class Username {
        /** Username is stored as a String */
        private String username;
        
        /** Username Constructor */
        public Username(String username);
        
        /** Converts to String */
        public String toString();
    }
    
```

# Class diagram for Board Object and Related Classes

![](https://i.imgur.com/Hs8tzEB.png)

# Design Analysis

* All those classes mentioned above which have only static members will
  not be instantiated for objects. They are only used to group together
  similar functions.
* We thought about using JSON for storing the class type within the serialized
  String, but we found out that it is not needed since Java provides a way
  to get the dynamic type of the object using the `getClass` method (a method 
  inherited from the `Object` class).
* The `Serializable` interface does not require any methods to be implemented.
* The algorithms for computing the pixels from the parameters of different shapes
  would use slight approximations for digits behind the decimal point for the
  computed pixels since the screen is a 2D array of pixels (smallest entity
  that can be manipulted is a pixel). 
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
  within the `BoardObject` in the form of an object of `IBoardObjectOperation`
  interface named `boardOp`. Here, if all operations were non-parameteric (i.e. did
  not have parameters like angle or color), then we could have just used an enum
  type, but since that is not the case, we ended up building a single interface
  and multiple operation classes - one for each operation.
* We would be providing utility classes which are inherently simple types.
  The reason for doing this is that we can identify the object by the object name
  as well as its type. For example, we know from this `UserId userId` that
  `userId` represents a user's ID, this provides extended readibility (extension
  to what the object name provides, i.e. `userId`).

# Summary and Conclusion

* The `serialize` and `deSerialize` methods can work on any object that is `Serializable`
  i.e. not just on board objects. This would be useful for other serializable 
  objects like the board state that have to be transferred using the network.
* The various methods for drawing different standard shapes would be present
  as static functions within the `BoardObjectBuilder` class.
* The `BoardObject` class is the class whose objects would be operated upon by
  various different operations like rotate, delete, undo, redo, color change.
  Objects of this class would be sent over the network to inform the server
  and other clients about an operation performed on the object by a client.
* The store and load functions for storing and loading the board state string in
  the server's file system would be provided for use by the server which has
  to maintain persistence of the board state.