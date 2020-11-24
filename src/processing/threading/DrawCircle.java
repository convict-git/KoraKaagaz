package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.shape.BoardObjectBuilder;

/**
 * Wrapper class implementing Runnable interface for threading of the draw circle operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class DrawCircle implements Runnable {
    private final Position center;
    private final Radius radius;
    private final Intensity intensity;

    /**
     * Initialize the parameters needed to create a circle
     *
     * @param center center of the circle
     * @param radius radius of the circle
     * @param intensity intensity of each pixel of the circle
     */
    public DrawCircle (
        Position center,
        Radius radius,
        Intensity intensity
    ) {
        this.center = center;
        this.radius = radius;
        this.intensity = intensity;
    }

    /**
     * Builds the circle as a BoardObject from the initialized parameters
     * and sends to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject circleObject =
                BoardObjectBuilder.drawCircle(
                    center,
                    radius,
                    intensity
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.drawCircle Successful"
            );

            /*
             * Sending the create operation object to the board server
             */
            if (circleObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    circleObject,
                    "DrawCircle"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "DrawCircle failed"
            );
        }
    }
}
