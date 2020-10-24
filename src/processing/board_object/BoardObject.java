package processing.board_object;

import processing.utility.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

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
    // object ID, timestamp and user ID and boolean
    // telling if the object is a reset object or not
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

    // Get the operation corresponding to this shape
    public BoardObjectOperation getOperation() {
    	return boardOp;
    }

    // Set the operation corresponding to this shape
    // This operation has special functionality for color change
    // operation - it stores previous intensities into the
    // `prevPixelIntensities` variable - required by undo
    public void setOperation (BoardObjectOperation boardOp) {
    	this.boardOp = boardOp;
    	if(
    		boardOp.getOperationType()
    		==
    		BoardObjectOperationType.COLOR_CHANGE
    	)
    		this.prevPixelIntensities = this.pixels;
    }

    // Get the board object's list of pixels
    public ArrayList <Pixel> getPixels () {
    	return pixels;
    }

    // Set the pixels using the given list of pixels
    public void setPixels (ArrayList <Pixel> pixels) {
    	this.pixels = pixels;
    }

    // Get the user's User ID who owns this object
    public UserId getUserId() {
    	return userId;
    }

    // Set user ID of the object
    public void setUserId(UserId userId) {
    	this.userId = userId;
    }

    // Get object ID
    public ObjectId getObjectId() {
    	return objectId;
    }

    // Get timestamp of creation of object
    public Timestamp getTimestamp() {
    	return timestamp;
    }

    // Is this a reset object ?
    public boolean isResetObject() {
    	return isReset;
    }

    // Get previous intensities of pixels
    public ArrayList <Pixel> getPrevIntensity() {
    	return prevPixelIntensities;
    }
}
