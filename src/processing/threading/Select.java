package processing.threading;

import processing.utility.*;
import java.util.ArrayList;
import processing.SelectDelete;
import infrastructure.validation.logger.*;

/**
 * Wrapper class implementing Runnable interface for threading of the select operation
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class Select implements Runnable {
    private final ArrayList<Position> inputPositions;
    private ArrayList<Pixel> selectedObjectPixels;

    /**
     * Returns all the positions of the object that got selected from
     * the top of input positions
     *
     * @return positions of the object that got selected
     */
    public ArrayList<Pixel> getSelectedObjectPixels() {
        return selectedObjectPixels;
    }

    /**
     * Initializes input positions needed by select as argument
     *
     * @param inputPositions positions clicked by the user
     */
    public Select (ArrayList<Position> inputPositions) {
        this.inputPositions = inputPositions;
    }

    /**
     * Computes the selected object and its positions from the input positions
     */
    public void run() {
        ILogger logger = null;

        try {
            logger = LoggerFactory.getLoggerInstance();
            this.selectedObjectPixels = SelectDelete.select(inputPositions);

            Helper.log(
                logger,
                LogLevel.INFO,
                "SelectDelete.select Successful"
            );
        }
        catch (Exception e) {
            this.selectedObjectPixels = null;
            Helper.log(
                logger,
                LogLevel.ERROR,
                "Select failed"
            );
        }
    }
}
