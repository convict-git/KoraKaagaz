package processing.shape;

import java.util.ArrayList;
import processing.utility.*;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;

/**
 * Static Line Segment Drawing Methods
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class LineDrawer {
	
	/** Algorithm Choice Enum */
	public enum Algorithm {DDA, BRESENHAM};
	
	/** Variable storing the algorithm which must be used */
	private static Algorithm lineAlgorithm =
		Algorithm.BRESENHAM; 
	
	/** Sets the algorithm to be used */
	public static void setAlgorithm(Algorithm algorithm) {
		lineAlgorithm = algorithm;
	}
	
	/**
	 * Draws a Line Segment from pointA to pointB based on the
	 * selected algorithm
	 * 
	 * @param pointA First end point of the line segment
	 * @param pointB Second end point of the line segment
	 * @param intensity Intensity of each point on the segment
	 * @return The arraylist of pixels of the line segment 
	 */
	public static ArrayList<Pixel> drawSegment(
		Position pointA,
	    Position pointB,
	    Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = null;
		
		switch(lineAlgorithm) {
			// If DDA algorithm is to be used, then use it
			case DDA:
				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using DDA Line Drawing Algorithm"
				);
				pixels = digitalDifferentialAnalyser(
					pointA,
					pointB,
					intensity
				);
				break;
			
			// If Bresenham algorithm is to be used, then use it (fall through case)
			case BRESENHAM:
				
			// By Default, use Bresenham Algorithm
			default:
				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using Bresenham Line Drawing Algorithm"
				);
				pixels = bresenhamLineDraw(
					pointA,
					pointB,
					intensity
				);
		}
		
		// Return the computed pixel arraylists
		return pixels;
	}
	
	/**
	 * Digital Differential Analser Algorithm
	 * 
	 * This algorithm constructs a line segment between two given
	 * points. It approximates the continuous line segment on the
	 * discrete surface (bitmap).
	 * 
	 * Algorithm Link:
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/
	 * Digital_differential_analyzer_(graphics_algorithm)">
	 * 
	 * @param pointA First end point of the line segment
	 * @param pointB Second end point of the line segment
	 * @param intensity Intensity of each point on the segment
	 * @return The arraylist of pixels of the line segment 
	 */
	private static ArrayList<Pixel> digitalDifferentialAnalyser(
		Position pointA,
	    Position pointB,
	    Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Compute difference in row and column coordinates
		int dr = pointB.r - pointA.r;
		int dc = pointB.c - pointA.c;
		
		// Compute number of steps to be taken
		int steps = Math.max(Math.abs(dr), Math.abs(dc));
		
		double rInc = dr / (double) steps; // Increment in row direction
		double cInc = dc / (double) steps; // Increment in col direction
		
		// Initial Coordinates
		double r = (double) pointA.r, c = (double) pointA.c;
		
		/*
		 *  We iterate one more than the number of steps to take
		 *  since we must also cover the initial coordinate
		 */
		for(int i = 0; i <= steps; i++, r += rInc, c += cInc) {
			// Round the double position to integer
			Position pos = new Position(
				(int) Math.round(r), 
				(int) Math.round(c)
			);
			
			// Add the computed pixel
			pixels.add(new Pixel(
				pos,
				new Intensity(intensity)
			));
		}
		
		// Return the computed pixel arraylist
		return pixels;
	}
	
	/**
	 * Bresenham's Line Drawing Algorithm
	 * 
	 * This algorithm constructs a line segment between two given
	 * points. It approximates the continuous line segment on the
	 * discrete surface (bitmap). It does not perform any floating
	 * point calculations, and optimally selects points which are
	 * close to the actual line.
	 * 
	 * Algorithm Link:
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm"</a>
	 * 
	 * @param pointA First end point of the line segment
	 * @param pointB Second end point of the line segment
	 * @param intensity Intensity of each point on the segment
	 * @return The arraylist of pixels of the line segment 
	 */
	private static ArrayList<Pixel> bresenhamLineDraw(
		Position pointA,
	    Position pointB,
	    Intensity intensity
	) {
		// Create new position objects since we may be swapping them
		pointA = new Position(pointA);
		pointB = new Position(pointB);
		
		int drAbs = Math.abs(pointB.r - pointA.r); // Difference along row axis
		int dcAbs = Math.abs(pointB.c - pointA.c); // Difference along col axis
		
		// Pixel arraylist which must be returned
		ArrayList<Pixel> pixels = null;
		
		/*
		 *  If difference along col axis is more, we can sample more points
		 *  along col axis (in increments of 1) compared to row axis
		 */
		if(dcAbs >= drAbs) {
			if(pointB.c < pointA.c)
				swapPosition(pointA, pointB);
			
			/*
			 *  Use drawLineLow to draw a "low" line, i.e. line with slope
			 *  having absolute value between 0 and 1
			 */
			pixels = drawLineLow(pointA, pointB, intensity);
		}
		else {
			/*
			 *  If difference along row axis is more, we can sample more
			 *  points along row axis compared to col axis
			 */
			if(pointB.r < pointA.r)
				swapPosition(pointA, pointB);
			
			/*
			 *  Use drawLineHigh to draw a "high" line, i.e. line with slope
			 *  having absolute value greater than 1
			 */
			pixels = drawLineHigh(pointA, pointB, intensity);
		}
		
		// Return the computed pixel arraylist
		return pixels;
	}
	
	/** Swap member values in a {@link Position} object */
	private static void swapPosition(
		Position a,
		Position b
	) {
		int temp;
		
		// Swap row values
		temp = a.r;
		a.r = b.r;
		b.r = temp;
		
		// swap col values
		temp = a.c;
		a.c = b.c;
		b.c = temp;
	}
	
	/**
	 * Draws Line which have the absolute value of slope less than
	 * or equal to 1, i.e. absolute change in column is more than 
	 * or equal to the absolute change in row. Also, the start
	 * point must have a lower or equal col than end point
	 * 
	 * @param start Start point of the line
	 * @param end End point of the line
	 * @param intensity Intensity of each point in the line
	 * @return The arraylist of pixels of the line segment 
	 */
	private static ArrayList<Pixel> drawLineLow(
		Position start,
		Position end,
		Intensity intensity
	) {
		// Pixel arraylist which must be returned
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Add start point
		pixels.add(new Pixel(
			new Position(start), 
			new Intensity(intensity)
		));

		int dr = end.r - start.r; // Difference in row
		int dc = end.c - start.c; // Difference in col
		
		/* 
		 * Decision parameter is used to decide whether to increment
		 * (or decrement) the row for the next iteration
		 */
		int decisionParam;
		
		/* Value to be added to row on an iteration when decision
		 * parameter indicates it to be added
		 */
		int rInc = 1;
		
		/*
		 *  If change in row is less than 0, then our line is heading
		 *  from a higher row value to a lower row value, so our increment
		 *  in row is actually negative
		 */
		if(dr < 0) {
			dr = -dr;
			rInc = -1;
		}
		
		// Compute initial decision parameter
		decisionParam = 2 * dr - dc;
		int prevRow = start.r; // Starting point's row value

		/* Start from the 1st point (0-based) since we have
		 * already taken the 0th point
		 */
		for(int c = start.c + 1;c <= end.c;c++) {
			
			// Compute position assuming row remains the same
			Position pos = new Position(prevRow, c);

			/* 
			 * If decision parameter is greater than zero, we increment
			 * row value and update decision parameter else we only
			 * update the decision parameter
			 */
			if(decisionParam > 0) {
				pos.r += rInc;
				decisionParam += 2 * (dr - dc);
			}
			else
				decisionParam += 2 * dr;

			// Add the pixel to our arraylist
			pixels.add(new Pixel(
				pos,
				new Intensity(intensity)
			));
			
			prevRow = pos.r;
		}

		// Return the computed pixel arraylist
		return pixels;
	}
	
	/**
	 * Draws Line which have the absolute value of slope greater than
	 * 1, i.e. absolute change in row is more than the absolute change in col.
	 * Also, the start point must have a lower or equal row than end point
	 * 
	 * @param start Start point of the line
	 * @param end End point of the line
	 * @param intensity Intensity of each point in the line
	 * @return The arraylist of pixels of the line segment 
	 */
	private static ArrayList<Pixel> drawLineHigh(
		Position start,
		Position end,
		Intensity intensity
	) {
		// Pixel arraylist which must be returned
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Add start point
		pixels.add(new Pixel(
			new Position(start), 
			new Intensity(intensity)
		));

		int dr = end.r - start.r; // Difference in row
		int dc = end.c - start.c; // Difference in col
		
		/*
		 *  Decision parameter is used to decide whether to increment
		 *  (or decrement) the col for the next iteration
		 */
		int decisionParam;

		/*
		 *  Value to be added to col on an iteration when decision
		 *  parameter indicates it to be added
		 */
		int cInc = 1;
		
		/*
		 *  If change in col is less than 0, then our line is heading
		 *  from a higher col value to a lower col value, so our increments
		 *  in col is actually negatives
		 */
		if(dc < 0) {
			dc = -dc;
			cInc = -1;
		}
		
		// Compute initial decision parameters
		decisionParam = 2 * dc - dr;
		int prevCol = start.c; // Starting point's col value

		/*
		 *  Start from the 1st point (0-based) since we have
		 *  already taken the 0th point
		 */
		for(int r = start.r + 1;r <= end.r;r++) {
			
			// Compute position assuming col remains the sames
			Position pos = new Position(r, prevCol);

			/*
			 *  If decision parameter is greater than zero, we increment
			 *  col value and update decision parameter else we only
			 *  update the decision parameter
			 */
			if(decisionParam > 0) {
				pos.c += cInc;
				decisionParam += 2 * (dc - dr);
			}
			else
				decisionParam += 2 * dc;

			// Add the pixel to our arraylist
			pixels.add(new Pixel(
				pos,
				new Intensity(intensity)
			));
			
			prevCol = pos.c;
		}

		// Return the computed pixel arraylist
		return pixels;
	}
}
