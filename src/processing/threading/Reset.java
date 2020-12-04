package processing.threading;

import processing.boardobject.BoardObject;
import processing.utility.UserId;
import infrastructure.validation.logger.*;

/**
 * Wrapper class to perform the reset operation on the board
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class Reset implements Runnable {
    private final UserId userId;
    private final boolean reset;

    /**
     * Initializes parameters needed to reset the screen by a client
     *
     * @param userId userid of the user performing the reset operation
     * @param reset true only if the object to be created is a reset object
     */
    public Reset (UserId userId, boolean reset) {
        this.userId = userId;
        this.reset = reset;
    }

    /**
     * Resets the screen
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject resetObject = processing.Reset.screenReset(userId, reset);

            Helper.log(
                    logger,
                    LogLevel.SUCCESS,
                    "Reset.screenReset successful"

            );

            /*
             * Sending the reset operation object to the board server
             */
            if (resetObject != null) {
                Helper.sendToBoardServer(
                        logger,
                        resetObject,
                        "Reset"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                    logger,
                    LogLevel.ERROR,
                    "Reset failed"
            );
        }
    }
}
