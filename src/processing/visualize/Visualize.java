package processing.visualize;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import processing.utility.Dimension;
import processing.utility.Pixel;

/**
 * Class for Visualizing ArrayList of Pixels
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class Visualize {

	/**
	 * Display the pixels provided as an ArrayList
	 * 
	 * @param pixels ArrayList of pixels which should be displayed
	 * @param dimension Dimension of the board
	 */
	public static void visualize(
		ArrayList<Pixel> pixels,
		Dimension dimension
	) {
		// Build the Buffered Image
		BufferedImage bufferedImage = buildImage(pixels, dimension);
		
		// Display the Buffered Image
		displayImage(bufferedImage);
	}
	
	/**
	 * Builds the Buffered Image using the pixels provided
	 * 
	 * @param pixels ArrayList of pixels which should be displayed
	 * @param dimension Dimension of the board
	 * @return image containing the required pixels and a white background
	 */
	private static BufferedImage buildImage(
		ArrayList<Pixel> pixels,
		Dimension dimension
	) {
		// Construct the Buffered Image
		BufferedImage bufferedImage = new BufferedImage(
			dimension.numCols,
			dimension.numRows,
			BufferedImage.TYPE_INT_RGB
		);
		
		// Initialize it with white (255, 255, 255)
		for(int r = 0;r < dimension.numRows;r++) {
			for(int c = 0;c < dimension.numCols;c++) {
				bufferedImage.setRGB(c, r, Color.WHITE.getRGB());
			}
		}
		
		// Add all the pixels in the ArrayList into the image
		for(Pixel pixel : pixels) {
			Color color = new Color(
				pixel.intensity.r,
				pixel.intensity.g,
				pixel.intensity.b
			);
			
			int x = pixel.position.c;
			int y = pixel.position.r;
			
			bufferedImage.setRGB(x, y, color.getRGB());
		}
		
		// Return the computed Buffered Image
		return bufferedImage;
	}
	
	/**
	 * Displays the provided Buffered Image
	 * 
	 * @param bufferedImage Buffered Image to be displayed
	 */
	private static void displayImage(BufferedImage bufferedImage) {
		// Construct frame in which to display
		JFrame frame = new JFrame("Visualize");
		
		// Construct Image Icon using Buffered Image
		ImageIcon imageIcon = new ImageIcon(bufferedImage);
		
		// Close Application on Closing the Frame
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Construct Label using Icon
		JLabel label = new JLabel();
		label.setIcon(imageIcon);
		
		// Place the label on the frame
		frame.getContentPane().add(label, BorderLayout.CENTER);
		
		// Make the Image Visible
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
