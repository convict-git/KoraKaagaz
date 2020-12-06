package processing;

import java.util.*;
import processing.boardobject.*;
import processing.utility.*;
import infrastructure.validation.logger.*;

/**
 * Class for implementing selected(non-erase, non-reset) object's Color-change and Rotate
 * operation.
 *
 * @author Devansh Singh Rathore
 * @reviewer Shruti Umat
 */


public class ParameterizedOperationsUtil {

	// initialise a reference to the logger singleton object
	static ILogger paraOpLogger = LoggerFactory.getLoggerInstance();

	/**
	 * Function to update undo-redo stack. Undo-Redo module cannot call this function inorder to
	 * avoid logical errors.
	 */
	private static void stackUtil(BoardObject newObj) {

		try {

			UndoRedo.pushIntoStack(newObj);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Undo-Redo stacks updated"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "Undo-Redo call failed!"
			);
		}

		return;
	}

	/**
	 * Function to calculate the centre of object's pixel by averaging the x and y coordinates of
	 * all the pixels in the given object.
	 */
	private static Position findCentre(ArrayList<Pixel>pixels) {

		Position centrePos = new Position(0, 0);
		int numOfPixels = pixels.size();
		for (int i = 0; i < numOfPixels; i++) {

			centrePos.r += pixels.get(i).position.r;
			centrePos.c += pixels.get(i).position.c;
		}

		if (numOfPixels == 0) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"Object is empty!"
			);
		}
		else {

			// averaging, followed by Narrowing Type Casting
			centrePos.r = (int)(centrePos.r / numOfPixels);
			centrePos.c = (int)(centrePos.c / numOfPixels);
		}

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
	 * To be called by ClientSideProcessing(via colorChange function) and UndoRedo sections.
	 */
	public static BoardObject colorChangeUtil(BoardObject obj, UserId id, Intensity newIntensity) {

		// storing initial intensities of pixels (to be used during undo-operation)
		ArrayList<Pixel> prevPixelIntensities = new ArrayList<Pixel>();

		// shallow copy
		// prevPixelIntensities = obj.getPixels();

		// deep copy
		for (Pixel p : obj.getPixels()) {

			prevPixelIntensities.add(new Pixel(p));
		}

		// changing intensities of selected object's pixels
		ArrayList<Pixel> newPixelSet = new ArrayList<Pixel>();

		// shallow copy
		// newPixelSet = obj.getPixels();

		// deep copy
		for (Pixel p : obj.getPixels()) {

			newPixelSet.add(new Pixel(p));
		}

		for (int i = 0; i < newPixelSet.size(); i++) {

			Pixel newPix = new Pixel(newPixelSet.get(i).position, newIntensity);
			newPixelSet.set(i, newPix);
		}

		// storing data members that remain same: objectId, and timestamp of object creation
		ObjectId newObjectId = obj.getObjectId();
		Timestamp newTime = obj.getTimestamp();

		// set (COLOR_CHANGE) as the operation which is applied on object.
		IBoardObjectOperation newBoardOp = new ColorChangeOperation(newIntensity);

		try {

			// remove previous object from maps.
			ClientBoardState.maps.removeObjectFromMaps(obj.getObjectId());

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChangeU: old object deleted"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChangeU: object deletion failed!"
			);
		}

		BoardObject newObj = null;
		try {

			// create a new object with same objectId, timestamp and other updated values.
			newObj = CurveBuilder.drawCurve(newPixelSet, newBoardOp, newObjectId, newTime,
			id, prevPixelIntensities, false);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChangeU: object recreated"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChangeU: object recreation failed!"
			);
		}

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

		// shallow copy
		// prevPixelIntensities = obj.getPixels();

		// deep copy
		for (Pixel p : obj.getPixels()) {

			prevPixelIntensities.add(new Pixel(p));
		}

		Position centre = null;
		try {

			// finding centre of rotation (i.e centre Position of previous pixels)
			centre = new Position(findCentre(prevPixelIntensities));

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: object centre found"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: centre couldn't be found!"
			);
		}

		double[][] rotMatrix = new double[2][2];
		try {

			// Calculating the required rotation matrix
			rotMatrix = computeRotationMatrix(angleOfRotation);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: rotation matrix created"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: rotation matrix couldn't be formed!"
			);
		}

		// generating new list of object's pixels
		ArrayList<Pixel> newPixelSet = new ArrayList<Pixel>();

		// shallow copy
		// newPixelSet = obj.getPixels();

		// deep copy
		for (Pixel p : obj.getPixels()) {

			newPixelSet.add(new Pixel(p));
		}

		for (int i = 0; i < newPixelSet.size(); i++) {

			// shifting origin
			Position posWithShiftedOrigin = new Position(
				newPixelSet.get(i).position.r - centre.r,
				newPixelSet.get(i).position.c - centre.c
			);

			// applying rotation matrix followed by Narrowing Type Casting
			Position rotatedPos = new Position(
				(int)(
					rotMatrix[0][0] * posWithShiftedOrigin.r
					+ rotMatrix[0][1] * posWithShiftedOrigin.c
				),
				(int)(
					rotMatrix[1][0] * posWithShiftedOrigin.r
					+ rotMatrix[1][1] * posWithShiftedOrigin.c
				)
			);

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

		try {

			// remove previous object from maps.
			ClientBoardState.maps.removeObjectFromMaps(obj.getObjectId());

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: old object deleted"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: object deletion failed!"
			);
		}

		BoardObject newObj = null;
		try {

			// create a new object with same objectId, timestamp and other updated values.
			newObj = CurveBuilder.drawCurve(newPixelSet, newBoardOp, newObjectId, newTime,
			id, prevPixelIntensities, false);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: object recreated"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotationU: object recreation failed!"
			);
		}

		return newObj;
	}

	/**
	 * Function for implementing color-change of select-able objects.
	 */
	public static BoardObject colorChange(BoardObject obj, UserId id, Intensity intensity) {

		BoardObject newObj = null;
		try {

			newObj = colorChangeUtil(obj, id, intensity);
			// stackUtil(newObj);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChange: operation completed"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChange: operation failed!"
			);
		}

		try {

			// To send all the pixel updates to UI
			CommunicateChange.provideChanges(newObj.getPrevIntensity(), newObj.getPixels());

			// To send selection updates to UI
			// CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
			//	.giveSelectedPixels(newObj.getPixels());

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChange: updates sent to UI"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "colorChange: UI updates failed!"
			);
		}

		// change `boardOp` of old object and return it
		IBoardObjectOperation newBoardOp = new ColorChangeOperation(intensity);
		//obj.setOperation(newBoardOp);
		//obj.setUserId(newObj.getUserId());
		BoardObject copyObj = UndoRedo.duplicateObject(obj, newBoardOp);
		copyObj.setUserId(newObj.getUserId());

		return copyObj;
	}

	/**
	 * Function for implementing rotation of select-able objects.
	 */
	public static BoardObject rotation(BoardObject obj, UserId id, Angle angleOfRotation) {

		BoardObject newObj = null;
		try {

			newObj = rotationUtil(obj, id, angleOfRotation);
			// stackUtil(newObj);

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotation: operation completed"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotation: operation failed!"
			);
		}

		try {

			// To send all the pixel updates to UI
			CommunicateChange.provideChanges(newObj.getPrevIntensity(), newObj.getPixels());

			// To send selection updates to UI
			// CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
			//	.giveSelectedPixels(newObj.getPixels());

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.SUCCESS,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotation: updates sent to UI"
			);
		} catch (Exception e) {

			paraOpLogger.log(
				ModuleID.PROCESSING,
				LogLevel.ERROR,
				"[#" + Thread.currentThread().getId() + "] "
				+ "rotation: UI updates failed!"
			);
		}

		// change `boardOp`, `UserId` of old object and return it
		IBoardObjectOperation newBoardOp = new RotateOperation(angleOfRotation);
		//obj.setOperation(newBoardOp);
		//obj.setUserId(newObj.getUserId());
		BoardObject copyObj = UndoRedo.duplicateObject(obj, newBoardOp);
		copyObj.setUserId(newObj.getUserId());

		return copyObj;
	}
}
