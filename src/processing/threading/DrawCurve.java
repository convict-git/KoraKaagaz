package processing.threading;

import java.util.ArrayList;
import processing.utility.*;
import processing.CurveBuilder;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.boardobject.IBoardObjectOperation;

/**
 * Wrapper class implementing Runnable interface for threading of the draw curve operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class DrawCurve implements Runnable {
    private final ArrayList<Pixel> pixels;
    private final IBoardObjectOperation newBoardOp;
    private final ObjectId newObjectId;
    private final Timestamp newTimestamp;
    private final UserId newUserId;
    private final ArrayList<Pixel> prevPixelIntensity;
    private final Boolean reset;

    /**
     * Initializes the parameters needed for the draw curve operation
     *
     * @param pixels list of pixels belonging to the curve to be drawn
     * @param newBoardOp board object operation
     * @param newObjectId board object's object id
     * @param newTimestamp timestamp of the object
     * @param newUserId user id of the creator of the object
     * @param prevPixelIntensity previous pixel intensity
     * @param reset true only if this is a reset object
     */
    public DrawCurve (
        ArrayList<Pixel> pixels,
        IBoardObjectOperation newBoardOp,
        ObjectId newObjectId,
        Timestamp newTimestamp,
        UserId newUserId,
        ArrayList<Pixel> prevPixelIntensity,
        Boolean reset
    ) {
        this.pixels = pixels;
        this.newBoardOp = newBoardOp;
        this.newObjectId = newObjectId;
        this.newTimestamp = newTimestamp;
        this.newUserId = newUserId;
        this.prevPixelIntensity = prevPixelIntensity;
        this.reset = reset;
    }

    /**
     * Performs the draw curve operation and sends the object to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject objectCreated =
                CurveBuilder.drawCurve(
                    pixels,
                    newBoardOp,
                    newObjectId,
                    newTimestamp,
                    newUserId,
                    prevPixelIntensity,
                    reset
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.drawCurve Successful"
            );

            /*
             * Sending the create operation object to the board server
             */
            if (objectCreated != null) {
                Helper.sendToBoardServer(
                    logger,
                    objectCreated,
                    "DrawCurve"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "DrawCurve failed"
            );
        }
    }
}
