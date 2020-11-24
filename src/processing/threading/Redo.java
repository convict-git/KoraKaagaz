package processing.threading;

import processing.UndoRedo;
import processing.boardobject.BoardObject;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;

/**
 * Wrapper class implementing Runnable interface for threading of the redo operation
 *
 * @author Shruti Umat
 */

public class Redo implements Runnable {

    /**
     * Performs the redo operation on the client and sends the object
     * with the appropriate operation and parameter, if applicable,
     * which when performed on other clients,
     * results in redo operation done by this client
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();
            BoardObject objectToBeSent = UndoRedo.redo();

            Helper.log(
                logger,
                LogLevel.INFO,
                "UndoRedo.redo Successful"
            );

            /*
             * Sending the redo operation object to the board server
             */
            if (objectToBeSent != null) {
                Helper.sendToBoardServer(
                    logger,
                    objectToBeSent,
                    "Redo"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Redo failed"
            );
        }
    }
}
