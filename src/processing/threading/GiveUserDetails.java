package processing.threading;

import processing.UserUtil;
import infrastructure.validation.logger.*;

/**
 * Wrapper to initiate a user's connection to the board using user details from the UI
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class GiveUserDetails implements Runnable {
    private final String userName;
    private final String ipAddress;
    private final String boardId;

    /**
     * Initiaize the incoming parameters from UI for giving user details
     *
     * @param userName user name of the user
     * @param ipAddress user's IP address
     * @param boardId user's board id
     */
    public GiveUserDetails (String userName, String ipAddress, String boardId) {
        this.userName = userName;
        this.ipAddress = ipAddress;
        this.boardId = boardId;
    }

    /**
     * Initiates the user's connection to the board using user details from the UI
     */
    public void run() {
        ILogger logger = null;
        try {
            logger = LoggerFactory.getLoggerInstance();

            UserUtil.initiateUserConnection(
                this.userName,
                this.ipAddress,
                this.boardId
            );

            Helper.log(
                    logger,
                    LogLevel.SUCCESS,
                    "UserUtil.initiateUserConnection successful"
            );
        }
        catch (Exception e) {
            Helper.log(
                    logger,
                    LogLevel.ERROR,
                    "GiveUserDetails failed"
            );
        }
    }
}
