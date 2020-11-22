package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.shape.BoardObjectBuilder;

/**
 * Wrapper class implementing Runnable interface for threading of the draw square operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class DrawSquare implements Runnable {
    private final Pixel topLeft;
    private final int sideLength;

    /**
     * Initializes the parameters of the square, namely
     * top left pixel containing the position and intensity, and the side length
     *
     * @param topLeft top left pixel
     * @param sideLength the square's side length
     */
    public DrawSquare (Pixel topLeft, float sideLength) {
        this.topLeft = topLeft;
        this.sideLength = Math.round(sideLength);
    }

    /**
     * Builds the square as a BoardObject from the initialized parameters
     * and sends to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            // calculating the bottom right Position from the top right position
            Position topLeftPos = topLeft.position;
            Position bottomRightPos =
                new Position(topLeftPos.r + sideLength, topLeftPos.c + sideLength);

            Intensity intensity = topLeft.intensity;

            BoardObject squareObject =
                BoardObjectBuilder.drawRectangle(
                    topLeftPos,
                    bottomRightPos,
                    intensity
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.drawSquare Successful"
            );

            /*
             * Sending the create operation object to the board server
             */
            if (squareObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    squareObject,
                    "DrawSquare"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "DrawSquare failed"
            );
        }
    }
}
