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

public class Visualize {
	public static void visualize(
		ArrayList<Pixel> pixels,
		Dimension dimension
	) {
		BufferedImage bufferedImage = buildImage(pixels, dimension);
		JFrame frame = new JFrame("Visualize");
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		ImageIcon imageIcon = new ImageIcon(bufferedImage);
		JLabel label = new JLabel();
		label.setIcon(imageIcon);
		
		frame.getContentPane().add(label, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static BufferedImage buildImage(
		ArrayList<Pixel> pixels,
		Dimension dimension
	) {
		BufferedImage bufferedImage = new BufferedImage(
			dimension.numCols,
			dimension.numRows,
			BufferedImage.TYPE_INT_RGB
		);
		
		for(int r = 0;r < dimension.numRows;r++) {
			for(int c = 0;c < dimension.numCols;c++) {
				bufferedImage.setRGB(c, r, Color.WHITE.getRGB());
			}
		}
		
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
		
		return bufferedImage;
	}
}
