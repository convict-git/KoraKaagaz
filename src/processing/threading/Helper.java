package processing.threading;

import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.server.board.ServerCommunication;
import processing.server.board.IServerCommunication;

/**
 * Static helper class to send an object to the board server and log operations
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class Helper {

    /**
     * Sends the object to the Board Server and logs necessary operations
     *
     * @param logger logger instance
     * @param object non-null object to be sent to the board server
     * @param description threadID of caller of this function and
     *                   title of the function that created the above object
     */
    public static void sendToBoardServer (ILogger logger, BoardObject object, String description) {
        try {
            IServerCommunication communicator = new ServerCommunication();
            communicator.sendObject(object);
            Helper.log(
                logger,
                LogLevel.SUCCESS,
                description + ": Object Successfully sent to the Board Server"
            );
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                description + ": Sending to Board Server Failed"
            );
        }
    }

    /**
     * Exception-safe log wrapper function
     * Module Id is Processing module
     * If logger object is null, the function simply returns
     *
     * @param logger logger instance
     * @param level Success, Error, Info or Warning
     * @param description string to be logged
     */
    public static void log (ILogger logger, LogLevel level, String description) {
        try {
            String threadId = "[#" + Thread.currentThread().getId() + "] ";

            logger.log(
                ModuleID.PROCESSING,
                level,
                threadId + description
            );
        }
        catch (Exception e) {
            return;
        }
    }
}
