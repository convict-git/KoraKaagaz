package processing.boardobject;

import processing.utility.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Board Object Class
 * 
 * It represents a collection of pixels on the board which
 * may be a curve or a shape. Objects of this class would
 * be sent across the network.
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class BoardObject implements Serializable {

	/**
	 * This UID is generated so that there won't be unexpected 
	 * InvalidClassExceptionsexceptions because of different 
	 * compiler implementations
	 * 
	 * @see <a href="https://docs.oracle.com/en/java/javase/11/
	 * docs/api/java.base/java/io/Serializable.html">Serializable</a>
	 */
	private static final long serialVersionUID = 1388864177359509930L;

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
	 * Constructor for the Board Object
	 * 
	 * @param pixels An array-list of pixels representing pixels of this board object
	 * @param objectId Object ID of this board object
	 * @param timestamp Time when the object was created 
	 * @param userId User ID of the user who owns this board object
	 * @param isReset Boolean representing whether object is a reset object or not
	 */
	public BoardObject (
	    ArrayList <Pixel> pixels,
	    ObjectId objectId,
	    Timestamp timestamp,
	    UserId userId,
	    boolean isReset
	) {
		this.pixels = pixels;
		this.boardOp = new CreateOperation();
		this.objectId = objectId;
		this.timestamp = timestamp;
		this.isReset = isReset;
		this.prevPixelIntensities = null;
	}
	
	/** Gets the operation corresponding to this shape */
	public IBoardObjectOperation getOperation() {
		return boardOp;
	}
	
	/**
	 * Sets the operations to be performed on this board object
	 * 
	 * @param boardOp The operation to be performed on this board object
	 */
	public void setOperation (IBoardObjectOperation boardOp) {
		this.boardOp = boardOp;    		
	}
	
	/** Gets the board object's list of pixels */
	public ArrayList <Pixel> getPixels () {
		return pixels;
	}
	
	/** Gets the board object's list of positions */
	public ArrayList <Position> getPositions () {
		// Construct position arraylist
		ArrayList <Position> positions = new ArrayList<Position> ();
		
		// Extract positions from pixels and add it to
		// the array list of positions
		for(Pixel p : pixels)
			positions.add(p.position);
		
		return positions;
	}
	
	/** Sets the pixels using the given array-list of pixels */
	public void setPixels (ArrayList <Pixel> pixels) {
		this.pixels = pixels;
	}
	
	/** Gets the user's User ID who owns this board object */
	public UserId getUserId() {
		return userId;
	}
	
	/** Sets user ID of this board object */
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	
	/** Gets object ID */
	public ObjectId getObjectId() {
		return objectId;
	}
	
	/** Gets timestamp of creation of object */
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	/** Returns true if this a reset object, else false */
	public boolean isResetObject() {
		return isReset;
	}
	
	/** Sets previous intensities of pixels */
	public void setPrevIntensity(
		ArrayList <Pixel> prevPixelIntensities
	) {
		this.prevPixelIntensities = prevPixelIntensities;
	}
	
	/** Gets previous intensities of pixels */
	public ArrayList <Pixel> getPrevIntensity() {
		return prevPixelIntensities;
	}
}
