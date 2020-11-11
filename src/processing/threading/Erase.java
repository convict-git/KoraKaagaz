package processing.threading;

import java.util.ArrayList;
import processing.utility.*;
import processing.CurveBuilder;
import processing.boardobject.BoardObject;
import infrastructure.validation.logger.*;
import processing.boardobject.IBoardObjectOperation;

/**
 * Wrapper class implementing Runnable interface for threading of the erase operation
 *
 * @author Shruti Umat
 */

public class Erase implements Runnable {
    private final ArrayList<Position> position;
    private final IBoardObjectOperation newBoardOp;
    private final ObjectId newObjectId;
    private final Timestamp newTimestamp;
    private final UserId newUserId;
    private final Boolean reset;

    /**
     * Initializes parameters needed by the erase operation
     *
     * @param position list of positions to be erased
     * @param newBoardOp board object operation
     * @param newObjectId object id of the erase object
     * @param newTimestamp timestamp of the erase object
     * @param newUserId user id of the creator of the object
     * @param reset true only if the current object is a reset object
     */
    public Erase (
        ArrayList<Position> position,
        IBoardObjectOperation newBoardOp,
        ObjectId newObjectId,
        Timestamp newTimestamp,
        UserId newUserId,
        Boolean reset
    ) {
        this.position = position;
        this.newBoardOp = newBoardOp;
        this.newObjectId = newObjectId;
        this.newTimestamp = newTimestamp;
        this.newUserId = newUserId;
        this.reset = reset;
    }

    /**
     * Performs the erase operation and sends the object to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject eraseObject =
                CurveBuilder.eraseCurve(
                    position,
                    newBoardOp,
                    newObjectId,
                    newTimestamp,
                    newUserId,
                    reset
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.eraseCurve Successful"
            );

            /*
             * Sending the erase operation object to the board server
             */
            Helper.sendToBoardServer(
                logger,
                eraseObject,
                "Erase"
            );
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Erase failed"
            );
        }
    }
}
