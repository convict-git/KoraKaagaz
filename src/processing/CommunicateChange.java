package processing;

import java.util.*;
import processing.IChanges;
import processing.utility.*;

public class CommunicateChange {
	
	// identifier to IChanges handler map
    // private Map <Identifier, IChanges> identifierTohandler;
    
	/*
     * Computes the set of pixels which will have effect
     * on the screen and notify the UI using the getChanges
     * method of IChanges.
     */
	
	// oldPixels -> previous pixels of the object
	// newPixels -> new pixels of the object
	public static void provideChanges(
			Identifier identifier, 
			ArrayList<Pixel> oldPixels, 
			ArrayList<Pixel> newPixels) {
		
		// stores the set of positions which would have been modified
		Set <Position> modifiedPos = new HashSet <Position> ();
		
		// stores the modified pixels
		ArrayList <Pixel> modifiedPixels = new ArrayList <Pixel> ();
		
		if (oldPixels != null) {
			// inserts the old position into the set
			for (Pixel pixel : oldPixels)
				modifiedPos.add(pixel.position);
		}
		
		if (newPixels != null) {
			// inserts the new position into the set
			for (Pixel pixel : newPixels)
				modifiedPos.add(pixel.position);
		}
		
		// finds the intensity value at each position and push into the array
		for (Position pos : modifiedPos) {
			Position curPos = new Position(pos);
			Intensity intensity;
			
			// gets the objectId at pos
			ObjectId objectId = ClientBoardState
					.maps
					.getMostProbableObjectId(
							new ArrayList <Position> (Arrays.asList(pos))
							);
			
			if (objectId == null) {
				// no object at the position, so default value is white
				intensity = new Intensity(255, 255, 255);
			}
			else {
				ArrayList <Pixel> pixels = ClientBoardState
						.maps
						.getBoardObjectFromObjectId(objectId)
						.getPixels();
				
				if (pixels == null || pixels.size() <= 0) {
					// Ideally, it should not happen
					// error should be logged
					intensity = new Intensity(255, 255, 255);
				}
				else {
					// An Object has same color on all of its pixels
					// gets the intensity of the object from the first pixel
					intensity = new Intensity(pixels.get(0).intensity);
				}
					
			}
			modifiedPixels.add(new Pixel(curPos, intensity));
		}
		
		// gets the handler for the subscriber
		IChanges handler = ClientBoardState.identifierTohandler.get(identifier);
		// sends the subscriber the modified pixels
		handler.getChanges(modifiedPixels);
	}
	
	// UI will subscribe for any changes 
	public static void subscribeForChanges(Identifier identifier, IChanges handler) {
		ClientBoardState.identifierTohandler.put(identifier, handler);
	}
}
