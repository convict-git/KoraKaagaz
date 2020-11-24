package processing.threading;

import processing.IChanges;
import processing.CommunicateChange;
import infrastructure.validation.logger.*;

/**
 * Wrapper class to spawn a thread for a module to subscribe to changes in the board's
 * pixels and selected pixels
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class SubscribeForChanges implements Runnable {
    private final String identifier;
    private final IChanges handler;

    /**
     * Initializes the parameters needed for a module to subscribe to the changes in the board
     *
     * @param identifier identifier of the handler
     * @param handler handler implementing IChanges interface whose functions
     *                will be invoked whenever the board's pixels change
     */
    public SubscribeForChanges(String identifier, IChanges handler) {
        this.identifier = identifier;
        this.handler = handler;
    }

    /**
     * Stores the handler and its identifier to be called whenever the board's pixels change
     */
    public void run() {
        ILogger logger = null;
        try {
            logger = LoggerFactory.getLoggerInstance();

            CommunicateChange.subscribeForChanges(this.identifier, this.handler);
            Helper.log(
                    logger,
                    LogLevel.SUCCESS,
                    "CommunicateChange.subscribeForChanges successful"
            );

        }
        catch (Exception e) {
            Helper.log(
                    logger,
                    LogLevel.ERROR,
                    "SubscribeForChanges failed"
            );
        }
    }
}
