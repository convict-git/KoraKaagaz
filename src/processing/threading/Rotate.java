package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.ParameterizedOperationsUtil;

/**
 * Wrapper class implementing Runnable interface for threading of the rotate operation
 *
 * @author Shruti Umat
 */

public class Rotate implements Runnable {
    private final BoardObject object;
    private final UserId userId;
    private final Angle angleOfRotation;

    /**
     * Initializes the parameters needed for the rotate operation
     *
     * @param object object to be rotated
     * @param userId user id of the user rotating the object
     * @param angleOfRotation angle of rotation in counter clockwise sense
     */
    public Rotate (BoardObject object, UserId userId, Angle angleOfRotation) {
        this.object = object;
        this.userId = userId;
        this.angleOfRotation = angleOfRotation;
    }

    /**
     * Performs the rotation operation and sends
     * the object with rotation operation and angle parameter to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();
            BoardObject rotateOperationObject =
                ParameterizedOperationsUtil.rotation(
                    object,
                    userId,
                    angleOfRotation
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "ParameterizedOperationsUtil.rotation Successful"
            );

            /*
             * Sending the rotate operation object to the board server
             */
            if (rotateOperationObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    rotateOperationObject,
                    "Rotate"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Rotate failed"
            );
        }
    }
}
