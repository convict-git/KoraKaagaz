package processing.threading;

import processing.utility.*;
import processing.SelectDelete;
import processing.boardobject.BoardObject;
import infrastructure.validation.logger.*;

/**
 * Wrapper class implementing Runnable interface for threading of the delete operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class Delete implements Runnable {
    private final BoardObject object;
    private final UserId userId;

    /**
     * Initializes board object and user id needed by delete as arguments
     *
     * @param object object to be deleted
     * @param userId user id of the user deleting this object
     */
    public Delete (BoardObject object, UserId userId) {
        this.object = object;
        this.userId = userId;
    }

    /**
     * Deletes the object which was initialized
     * and sends it to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject deleteOperationObject =
                SelectDelete.delete(
                    this.object,
                    this.userId
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "SelectDelete.delete Successful"
            );

            /*
             * Sending the delete operation object to the board server
             */
            if (deleteOperationObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    deleteOperationObject,
                    "Delete"
                );
            }

        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Delete failed"
            );
        }
    }
}
