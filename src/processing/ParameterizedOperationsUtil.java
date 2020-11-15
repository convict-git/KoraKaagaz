package processing;

import java.util.*;
import processing.boardobject.*;
import processing.utility.*;

/**
 *
 * @author Devansh Singh Rathore
 * @reviewer Shruti Umat
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
     * Function to calculate the centre of object's pixel by averaging the x and y coordinates of
     * all the pixels in the given object.
     */
    private static Position findCentre(ArrayList<Pixel>pixels) {

        Position centrePos = new Position(0, 0);
        int numOfPixels = pixels.size();
        for(int i = 0; i < numOfPixels; i++) {

            centrePos.r += pixels.get(i).position.r;
            centrePos.c += pixels.get(i).position.c;
        }

        // averaging, followed by Narrowing Type Casting
        centrePos.r = (int)(centrePos.r / numOfPixels);
        centrePos.c = (int)(centrePos.c / numOfPixels);

        return centrePos;
    }

    /**
     * Function calculates the rotation matrix.
     */
    private static double[][] computeRotationMatrix(Angle angleOfRotation) {

        // calculating angle, sin, cosine values
        double radians = Math.toRadians(angleOfRotation.angle);
        double cosTheta = Math.cos(radians), sinTheta = Math.sin(radians);

        // rotation matrix for counter-clockwise rotation
        double[][] rotMat = new double[2][2];
        rotMat[0][0] = cosTheta;
        rotMat[0][1] = -1.0 * sinTheta;
        rotMat[1][0] = sinTheta;
        rotMat[1][1] = cosTheta;

        return rotMat;
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

            Pixel newPix = new Pixel(newPixelSet.get(i).position, newIntensity);
            newPixelSet.set(i, newPix);
        }

        // storing data members that remain same: objectId, and timestamp of object creation
        ObjectId newObjectId = obj.getObjectId();
        Timestamp newTime = obj.getTimestamp();

        // set (COLOR_CHANGE) as the operation which is applied on object.
        IBoardObjectOperation newBoardOp = new ColorChangeOperation(newIntensity);

        // remove previous object from maps.
        BoardObject dummyObj = ClientBoardState.maps.removeObjectFromMaps(obj.getObjectId());

        // create a new object with same objectId, timestamp and other updated values.
        BoardObject newObj = CurveBuilder.drawCurve(newPixelSet, newBoardOp, newObjectId, newTime,
        id, prevPixelIntensities, false);

        return newObj;
    }

    /**
     * Function to rotate the object, store all required object's data members in local
     * variables, call for removing previous object from all utility maps, and finally create
     * new updated object.
     * To be called by ClientSideProcessing(via rotation function) and UndoRedo sections.
     */
    public static BoardObject rotationUtil(BoardObject obj, UserId id, Angle angleOfRotation) {

        // storing initial list of pixels (to be used in creation of new BoardObject)
        ArrayList<Pixel> prevPixelIntensities = new ArrayList<Pixel>();
        prevPixelIntensities = obj.getPixels();

        // finding centre of rotation (i.e centre Position of previous pixels)
        Position centre = new Position(findCentre(prevPixelIntensities));

        // Calculating the required rotation matrix
        double[][] rotMatrix = computeRotationMatrix(angleOfRotation);

        // generating new list of object's pixels
        ArrayList<Pixel> newPixelSet = new ArrayList<Pixel>();
        newPixelSet = obj.getPixels();
        for(int i = 0; i < newPixelSet.size(); i++) {

            // shifting origin
            Position posWithShiftedOrigin = new Position(newPixelSet.get(i).position.r - centre.r,
            newPixelSet.get(i).position.c - centre.c);

            // applying rotation matrix followed by Narrowing Type Casting
            Position rotatedPos = new Position((int)(rotMatrix[0][0] * posWithShiftedOrigin.r +
            rotMatrix[0][1] * posWithShiftedOrigin.c), (int)(rotMatrix[1][0] * posWithShiftedOrigin.r
             + rotMatrix[1][1] * posWithShiftedOrigin.c));

            // Re-align according to the calculated centre
            Position finalPos = new Position(rotatedPos.r + centre.r, rotatedPos.c + centre.c);

            // set final pixel, preserving the intensity
            Pixel newPix = new Pixel(finalPos, newPixelSet.get(i).intensity);
            newPixelSet.set(i, newPix);
        }

        // storing data members that remain same: objectId, and timestamp of object creation
        ObjectId newObjectId = obj.getObjectId();
        Timestamp newTime = obj.getTimestamp();

        // set (ROTATE) as the operation which is applied on object.
        IBoardObjectOperation newBoardOp = new RotateOperation(angleOfRotation);

        // remove previous object from maps.
        BoardObject dummyObj = ClientBoardState.maps.removeObjectFromMaps(obj.getObjectId());

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

        // To send all the pixel updates to UI
        CommunicateChange.provideChanges(obj.getPrevIntensity(), obj.getPixels());

        // To send selection updates to UI
        CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI).giveSelectedPixels(obj.getPixels());

        return obj.getObjectId();
    }

    /**
     * Function for implementing rotation of select-able objects.
     */
    public static ObjectId rotation(BoardObject obj, UserId id, Angle angleOfRotation) {

        obj = rotationUtil(obj, id, angleOfRotation);
        stackUtil(obj);

        // To send all the pixel updates to UI
        CommunicateChange.provideChanges(obj.getPrevIntensity(), obj.getPixels());

        // To send selection updates to UI
        CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI).giveSelectedPixels(obj.getPixels());

        return obj.getObjectId();
    }
}
