package processing;

import java.util.*;
import processing.utility.*;
import infrastructure.validation.logger.*;

/**
 * Class for Communicating Changes to the UI
 * 
 * @author Rakesh Kumar
 * @reviewer Devansh Singh Rathore
 */

public class CommunicateChange {
	
    // gets the logger instance
    private static ILogger logger = LoggerFactory.getLoggerInstance();
	
    // identifier to IChanges handler map
    public static Map <String, IChanges> identifierToHandler = new HashMap <String, IChanges> ();
    // UI identifier
    public static String identifierUI;
	
    /**
     * Computes the set of pixels which will have effect
     * on the screen and notify the UI using the getChanges
     * method of IChanges handler.
     * 
     * @param identifier identifier of the handler
     * @param oldPixels previous pixels of the object
     * @param newPixels new pixels of the object
     */
	
    public static void provideChanges(
                                      String identifier, 
                                      ArrayList<Pixel> oldPixels, 
                                      ArrayList<Pixel> newPixels
                                      ) {
		
        // stores the set of positions which would have been modified
        Set <Position> modifiedPos = new HashSet <Position> ();
		
        if (oldPixels != null) {
            // inserts the old positions into the set
            for (Pixel pixel : oldPixels)
                modifiedPos.add(pixel.position);
        }
		
        if (newPixels != null) {
            // inserts the new positions into the set
            for (Pixel pixel : newPixels)
                modifiedPos.add(pixel.position);
        }
		
        // stores the positions from the set into the array
        ArrayList <Position> setToArray = new ArrayList <Position> ();
        setToArray.addAll(modifiedPos);
		
        // gets the top pixels at those positions
        ArrayList <Pixel> modifiedPixels = ClientBoardState.maps.getPixelsAtTop(setToArray);
		
        // gets the handler for the subscriber
        IChanges handler = identifierToHandler.get(identifier);
		
        if (handler == null) {
            logger.log(
                       ModuleID.PROCESSING, 
                       LogLevel.ERROR, 
                       "[#" + Thread.currentThread().getId() + "] "
                       + "Couldn't find the handler for the identifier"
                       );
            return;
        }
		
        // sends the modified pixels to the handler
        handler.getChanges(modifiedPixels);
		
        logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS, 
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Sent the modified pixels to the UI"
                   );
    }
	
    /**
     * Used to implement default parameter method
     * Since, only UI subscribes for the changes so
     * default UI identifier is used and calls the above
     * method
     * 
     * @param oldPixels previous pixels of the object
     * @param newPixels new pixels of the object
     */
    public static void provideChanges(
                                      ArrayList<Pixel> oldPixels, 
                                      ArrayList<Pixel> newPixels
                                      ) {
        provideChanges(identifierUI, oldPixels, newPixels);
    }
	
    /**
     *  A module can subscribe for any changes
     *  In our design, only UI need subscription so
     *  @param identifier can be removed but it is kept
     *  for now. 
     *   
     *  @param identifier identifier used for subscription
     *  @param handler UI handler to handle changes
     */
    public static void subscribeForChanges(String identifier, IChanges handler) {
        identifierToHandler.put(identifier, handler);
        /**
         *  Default UI identifier is stored
         *  Needs to modified when more modules can subscribe
         */
        identifierUI = identifier;
        logger.log(
                   ModuleID.PROCESSING, 
                   LogLevel.SUCCESS, 
                   "[#" + Thread.currentThread().getId() + "] "
                   + "Successfully subscribed for the changes"
                   );
    }
}
