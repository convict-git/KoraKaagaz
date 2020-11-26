package processing.threading;

import processing.UserUtil;
import java.util.ArrayList;
import processing.utility.Position;
import infrastructure.validation.logger.*;

/**
 * Wrapper class to spawn a thread for getting the username of the user
 * who most recently modified/created the object on top of the input positions on the board
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class GetUser implements Runnable {
    private final ArrayList<Position> positions;
    private String username;

    /**
     * Initializes the parameters needed for getUser function call
     *
     * @param positions positions on the board
     */
    public GetUser (ArrayList<Position> positions) {
        this.positions = positions;
    }

    /**
     * Returns the user name computed from the run() method of this class
     *
     * @return user name of the user who created/last modified the object
     * on top of the initialized board positions
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Computes the username of the user who created/last modified
     * the object on top of the initialized positions on the board
     */
    public void run() {
        ILogger logger = null;
        try {
            logger = LoggerFactory.getLoggerInstance();

            this.username = UserUtil.getUser(this.positions);
            Helper.log(
                    logger,
                    LogLevel.SUCCESS,
                    "UserUtil.getUser successful"
            );
        }
        catch (Exception e) {
            Helper.log(
                    logger,
                    LogLevel.ERROR,
                    "GetUser failed"
            );
        }
    }
}
