package processing.threading;

import processing.utility.*;
import infrastructure.validation.logger.*;
import processing.boardobject.BoardObject;
import processing.shape.BoardObjectBuilder;

/**
 * Wrapper class implementing Runnable interface for threading of the draw triangle operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class DrawTriangle implements Runnable {
    private final Position vertA;
    private final Position vertB;
    private final Position vertC;
    private final Intensity intensity;

    /**
     * Initializes the parameters to construct a triangle, namely
     * 3 vertices and the intensity of the triangle
     *
     * @param vertA a unique, named vertex of the triangle
     * @param vertB a unique, named vertex of the triangle
     * @param vertC a unique, named vertex of the triangle
     * @param intensity intensity of the triangle to be drawn
     */
    public DrawTriangle (
        Position vertA,
        Position vertB,
        Position vertC,
        Intensity intensity
    ) {
        this.vertA = vertA;
        this.vertB = vertB;
        this.vertC = vertC;
        this.intensity = intensity;
    }

    /**
     * Builds the triangle as a BoardObject from the initialized parameters
     * and sends to the board server
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();

            BoardObject triangleObject =
                BoardObjectBuilder.drawTriangle(
                    vertA,
                    vertB,
                    vertC,
                    intensity
                );

            Helper.log(
                logger,
                LogLevel.INFO,
                "BoardObjectBuilder.drawTriangle Successful"
            );

            /*
             * Sending the create operation object to the board server
             */
            if (triangleObject != null) {
                Helper.sendToBoardServer(
                    logger,
                    triangleObject,
                    "DrawTriangle"
                );
            }
        }
        catch (Exception e) {
            Helper.log(
                logger,
                LogLevel.ERROR,
                "DrawTriangle failed"
            );
        }
    }
}
