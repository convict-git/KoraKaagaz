package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.ParameterizedOperationsUtil;

/**
 * Wrapper class implementing Runnable interface for threading of the color change operation
 *
 * @author Shruti Umat
 */

public class ColorChange implements Runnable {
    private final BoardObject object;
    private final UserId userId;
    private final Intensity intensity;

    public ColorChange (BoardObject object, UserId userId, Intensity intensity) {
        this.object = object;
        this.userId = userId;
        this.intensity = intensity;
    }

    /**
     * Performs color change operation on the object which was initialized
     * Sends it to the Server Communicator
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();
            BoardObject colorChangeOperationObject =
                ParameterizedOperationsUtil.colorChange(
                    object,
                    userId,
                    intensity
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "ParameterizedOperationsUtil.colorChange Successful"
            );

            /*
             * Sending the color change operation object to the board server
             */
            if (colorChangeOperationObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    colorChangeOperationObject,
                    "ColorChange"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "ColorChange failed"
            );
        }
    }
}
