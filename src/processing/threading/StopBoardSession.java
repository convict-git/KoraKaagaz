package processing.threading;

import processing.UserUtil;
import infrastructure.validation.logger.*;

/**
 * Wrapper class to spawn a thread for stopping the board session
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class StopBoardSession implements Runnable {

    /**
     * Stops the client's board session and logs success or failure
     */
    public void run() {
        ILogger logger = null;
        try {
            logger = LoggerFactory.getLoggerInstance();

            UserUtil.stopBoardSession();
            Helper.log(
                    logger,
                    LogLevel.SUCCESS,
                    "UserUtil.stopBoardSession successful"
            );
        }
        catch (Exception e) {
            Helper.log(
                    logger,
                    LogLevel.ERROR,
                    "StopBoardSession failed"
            );
        }
    }
}
