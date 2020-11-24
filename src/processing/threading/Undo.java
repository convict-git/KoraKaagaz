package processing.threading;

import processing.UndoRedo;
import processing.boardobject.BoardObject;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;

/**
 * Wrapper class implementing Runnable interface for threading of the undo operation
 *
 * @author Shruti Umat
 */

public class Undo implements Runnable {

    /**
     * Performs the undo operation on the client and sends the object
     * with the appropriate operation and parameter, if applicable,
     * which when performed on other clients,
     * results in undo operation done by this client
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();
            BoardObject objectToBeSent = UndoRedo.undo();

            Helper.log(
                logger,
                LogLevel.INFO,
                "UndoRed.undo Successful"
            );

            /*
             * Sending the undo operation object to the board server
             */
            if (objectToBeSent != null) {
                Helper.sendToBoardServer(
                    logger,
                    objectToBeSent,
                    "Undo"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Undo failed"
            );
        }
    }
}
