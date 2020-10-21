package processing;

import java.util.*;
import processing.utility.*;

/**
*
* @author Devansh Singh Rathore
*/

/**
* Class for implementing selected(non-erase, non-reset) object's Color-change and Rotate
* operation.
*/
public class ParameterizedOperationsUtil {

    /**
    * Function to update undo-redo stack. Undo-Redo module cannot call this function inorder to
    * avoid logical errors.
    */
    private static void stackUtil(BoardObject newObj) {

        UndoRedo.pushIntoStack(newObj);
        return;
    }

    /**
    * Function to change colors of object, store all required object's data members in local
    * variables, call for removing previous object from all utility maps, and finally create
    * new updated object.
    * To be called by ClientSideProcessing(vis colorChange function) and UndoRedo sections.
    */
    public static BoardObject colorChangeUtil(BoardObject obj, UserId id, Intensity newIntensity) {

        // storing initial intensities of pixels (to be used during undo-operation)
        ArrayList<Pixel> prevPixelIntensities = new ArrayList<Pixel>();
        prevPixelIntensities = obj.getPixels();

        // changing intensities of selected object's pixels
        ArrayList<Pixel> newPixelSet = new ArrayList<Pixel>();
        newPixelSet = obj.getPixels();
        for(int i = 0; i < newPixelSet.size(); i++) {

            Pixel newPix;
            newPix.position = newPixelSet.get(i).position;
            newPix.intensity = newIntensity;

            newPixelSet.set(i, newPix);
        }

        // storing data members that remain same: objectId, and timestamp of object creation
        ObjectId newObjectId = obj.objectId;
        Timestamp newTime = obj.timestamp;

        // set (COLOR_CHANGE) as the operation which is applied on object.
        BoardObjectOperation newBoardOp = new ColorChangeOperation(newIntensity);

        // remove previous object from maps.
        BoardObject dummyObj = SelectDeleteUtil.removeObjectFromMaps(obj.objectId);

        // create a new object with same objectId, timestamp and other updated values.
        BoardObject newObj = CurveBuilder.drawCurve(newPixelSet, newBoardOp, newObjectId, newTime,
        id, prevPixelIntensities, false);

        return newObj;
    }

    /**
    * Function for implementing color-change of select-able objects.
    */
    public static ObjectId colorChange(BoardObject obj, UserId id, Intensity intensity) {

        obj = colorChangeUtil(obj, id, intensity);
        stackUtil(obj);

        return obj.getObjectId();
    }
}
