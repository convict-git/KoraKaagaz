package processing.boardobject;

import java.io.Serializable;
import java.util.ArrayList;

import processing.utility.*;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;

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
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO,
			"[#" + Thread.currentThread().getId() + "] "
			+ "Constructing Board Object"
		);
		
		this.pixels = pixels;
		this.boardOp = new CreateOperation();
		this.objectId = objectId;
		this.timestamp = timestamp;
		this.userId = userId;
		this.isReset = isReset;
		this.prevPixelIntensities = null;
	}
	
	/** Gets the operation corresponding to this shape */
	public IBoardObjectOperation getOperation() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Operation from the Board Object"
		);
		return boardOp;
	}
	
	/**
	 * Sets the operations to be performed on this board object
	 * 
	 * @param boardOp The operation to be performed on this board object
	 */
	public void setOperation (IBoardObjectOperation boardOp) {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Set Operation of the Board Object"
		);
		this.boardOp = boardOp;    		
	}
	
	/** Gets the board object's list of pixels */
	public ArrayList <Pixel> getPixels () {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Pixels from the Board Object"
		);
		return pixels;
	}
	
	/** Gets the board object's list of positions */
	public ArrayList <Position> getPositions () {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Positions from the Board Object"
		);
		
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
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO,
			"[#" + Thread.currentThread().getId() + "] "
			+ "Set Pixels of the Board Object"
		);
		this.pixels = pixels;
	}
	
	/** Gets the user's User ID who owns this board object */
	public UserId getUserId() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO,
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get User ID from the Board Object"
		);
		return userId;
	}
	
	/** Sets user ID of this board object */
	public void setUserId(UserId userId) {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Set User ID of the Board Object"
		);
		this.userId = userId;
	}
	
	/** Gets object ID */
	public ObjectId getObjectId() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Object ID from the Board Object"
		);
		return objectId;
	}
	
	/** Gets timestamp of creation of object */
	public Timestamp getTimestamp() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO,
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Timestamp from the Board Object"
		);
		return timestamp;
	}
	
	/** Returns true if this a reset object, else false */
	public boolean isResetObject() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO,
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Reset Info from the Board Object"
		);
		return isReset;
	}
	
	/** Sets previous intensities of pixels */
	public void setPrevIntensity(
		ArrayList <Pixel> prevPixelIntensities
	) {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Set Previous Pixels of the Board Object"
		);
		this.prevPixelIntensities = prevPixelIntensities;
	}
	
	/** Gets previous intensities of pixels */
	public ArrayList <Pixel> getPrevIntensity() {
		LoggerFactory.getLoggerInstance().log(
			ModuleID.PROCESSING, 
			LogLevel.INFO, 
			"[#" + Thread.currentThread().getId() + "] "
			+ "Get Previous Pixels from the Board Object"
		);
		return prevPixelIntensities;
	}
}
