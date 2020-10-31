package processing;

import java.util.*;
import processing.utility.*;

/**
 * Class for Communicating Changes to the UI
 * 
 * @author Rakesh Kumar
 * @reviewer Devansh Singh Rathore
 */

public class CommunicateChange {
	
	/** identifier to IChanges handler map */
	private static Map <String, IChanges> identifierToHandler;
	/** UI identifier */
	private static String identifierUI;

	/**
     * Computes the set of pixels which will have effect
     * on the screen and notify the UI using the getChanges
     * method of IChanges handler.
     *
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
		
		/** stores the set of positions which would have been modified */
		Set <Position> modifiedPos = new HashSet <Position> ();
		
		/** stores the modified pixels */
		ArrayList <Pixel> modifiedPixels = new ArrayList <Pixel> ();
		
		if (oldPixels != null) {
			/** inserts the old position into the set */
			for (Pixel pixel : oldPixels)
				modifiedPos.add(pixel.position);
		}
		
		if (newPixels != null) {
			/** inserts the new position into the set */
			for (Pixel pixel : newPixels)
				modifiedPos.add(pixel.position);
		}
		
		/** finds the intensity value at each position and push into the array */
		for (Position pos : modifiedPos) {
			Position curPos = new Position(pos);
			Intensity intensity;
			
			/** gets the objectId at pos */
			ObjectId objectId = ClientBoardState
					.maps
					.getMostProbableObjectId(
							new ArrayList <Position> (Arrays.asList(pos))
					);
			
			if (objectId == null) {
				/** no object at the position, so default value is white */
				intensity = new Intensity(255, 255, 255);
			}
			else {
				ArrayList <Pixel> pixels = ClientBoardState
						.maps
						.getBoardObjectFromObjectId(objectId)
						.getPixels();
				
				if (pixels == null || pixels.size() <= 0) {
					 /** Ideally, it should not happen and error should be logged */
					intensity = new Intensity(255, 255, 255);
				}
				else {
					/**
					 * An Object has same color on all of its pixels
					 * gets the intensity of the object from the first pixel
					 */
					intensity = new Intensity(pixels.get(0).intensity);
				}
					
			}
			modifiedPixels.add(new Pixel(curPos, intensity));
		}
		
		
		/** gets the handler for the subscriber */
		IChanges handler = identifierToHandler.get(identifier);
		
		/** sends the modified pixels to the handler*/
		handler.getChanges(modifiedPixels);
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
		/** Default UI identifier is stored */
		identifierUI = identifier;
	}
}
