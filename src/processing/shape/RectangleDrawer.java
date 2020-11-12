package processing.shape;

import java.util.ArrayList;
import processing.utility.*;

/**
 *
 * Axis Aligned Rectangle Border Drawing and Filling Methods
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class RectangleDrawer {
	/**
	 * Draws an axis aligned rectangle given the top left and
	 * bottom right coordinates
	 * 
	 * @param topLeft Top left coordinate
	 * @param bottomRight Bottom right coordinate
	 * @param intensity Intensity of each pixel
	 * @return the pixels of the border of the axis aligned
	 * rectangle to be constructed 
	 */
	public static ArrayList<Pixel> drawRectangle (
        Position topLeft,
        Position bottomRight,
        Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Draw the left and right vertical lines of the rectangle
		for(int r = topLeft.r;r <= bottomRight.r;r++) {
			pixels.add(new Pixel(
				new Position(r, topLeft.c),
				new Intensity(intensity)
			));
			
			pixels.add(new Pixel(
				new Position(r, bottomRight.c),
				new Intensity(intensity)
			));
		}
		
		/* 
		 * Draw the top and bottom vertical lines of the rectangle
		 * The top left, top right, bottom left and bottom right
		 * pixels should not be added since they have already been
		 * added before (hence the for loop initialization and
		 * condition are not the end values)
		 */
		for(int c = topLeft.c + 1;c <= bottomRight.c - 1;c++) {
			pixels.add(new Pixel(
				new Position(topLeft.r, c),
				new Intensity(intensity)
			));
			
			pixels.add(new Pixel(
				new Position(bottomRight.r, c),
				new Intensity(intensity)
			));
		}
		
		// Return the arraylist of pixels
		return pixels;
	}
	
	/**
	 * Draws a filled axis aligned rectangle given the top left
	 * and bottom right coordinates
	 * 
	 * @param topLeft Top left coordinate
	 * @param bottomRight Bottom right coordinate
	 * @param intensity Intensity of each pixel
	 * @return the pixels of the border of the filled axis
	 * aligned rectangle to be constructed 
	 */
	public static ArrayList<Pixel> drawRectangleFill (
        Position topLeft,
        Position bottomRight,
        Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		/*
		 *  Pick every point lying on or within the rectangle
		 *  to fill it completely
		 */
		for(int r = topLeft.r;r <= bottomRight.r;r++) {
			for(int c = topLeft.c;c <= bottomRight.c;c++) {
				pixels.add(new Pixel(
					new Position(r, c),
					new Intensity(intensity)
				));
			}
		}
		
		// Return the arraylist of pixelss
		return pixels;
	}
}
