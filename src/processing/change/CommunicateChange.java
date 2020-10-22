package processing.change;

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
			Pixel pixel = new Pixel ();
			pixel.position.r = pos.r;
			pixel.position.c = pos.c;
			PriorityQueue <PriorityQueueObject> objQueue = 
					ClientBoardState.maps.getObject(pos);
			
			// no object at the position, so default value is white
			if (objQueue == null || objQueue.size() == 0) {
				pixel.intensity.r = 255;
				pixel.intensity.g = 255;
				pixel.intensity.b = 255;
			}
			else {
				UserId userId  = objQueue.peek();
				// find the intensity
				//pixel.intensity
			}
			modifiedPixels.add(pixel);
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
