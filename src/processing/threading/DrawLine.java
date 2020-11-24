package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.shape.BoardObjectBuilder;

/**
 * Wrapper class implementing Runnable interface for threading of the draw line operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class DrawLine implements Runnable {
    private final Position pointA;
    private final Position pointB;
    private final Intensity intensity;

    /**
     * Initializes the parameters needed to construct a line, namely
     * two positions and the line's intensity
     *
     * @param pointA position of one of the points making the line
     * @param pointB position of the other point making the line
     * @param intensity intensity of the line
     */
    public DrawLine (
        Position pointA,
        Position pointB,
        Intensity intensity
    ) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.intensity = intensity;
    }

    /**
     * Builds the line as a BoardObject from the initialized parameters
     * and sends to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject lineObject =
                BoardObjectBuilder.drawSegment(
                    pointA,
                    pointB,
                    intensity
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.drawSegment Successful"
            );

            /*
             * Sending the create operation object to the board server
             */
            if (lineObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    lineObject,
                    "DrawLine"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "DrawLine failed"
            );
        }
    }
}
