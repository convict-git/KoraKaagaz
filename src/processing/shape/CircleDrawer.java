package processing.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import processing.utility.*;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;

/**
 * Static Circle Drawing Methods
 *
 * @author Ahmed Zaheer Dadarkar, Rakesh Kumar
 * @reviewer Rakesh Kumar, Ahmed Zaheer Dadarkar
 */

public class CircleDrawer {
	
	/** Algorithm for Circle Boundary */
	public enum AlgorithmCircle {MID_POINT};
	
	/** Algorithm for Circle Fill */
	public enum AlgorithmFill {MID_POINT_BASED, DEVANSH, BFS_FILL};
	
	/** Variable storing the algorithm for Circle Boundary */
	private static AlgorithmCircle circleAlgorithm =
		AlgorithmCircle.MID_POINT;
	
	/** Variable storing the algorithm for Circle Fill */
	private static AlgorithmFill fillAlgorithm =
		AlgorithmFill.MID_POINT_BASED;
	
	/** Sets the algorithm for Circle Boundary to be used */
	public static void setAlgorithmCircle(AlgorithmCircle algorithm) {
		circleAlgorithm = algorithm;
	}
	
	/** Sets the algorithm for Circle Boundary to be used */
	public static void setAlgorithmFill(AlgorithmFill algorithm) {
		fillAlgorithm = algorithm;
	}
	
	/**
	 * Draws a Circle based on the center and radius provided using
	 * the selected algorithm
	 * 
	 * @param center Center of the circle
	 * @param radius Radius of the circle
	 * @param intensity Intensity of each pixel of the circle
	 * @return the arraylist of pixels of the circle
	 */
	public static ArrayList<Pixel> drawCircle(
		Position center,
        Radius radius,
        Intensity intensity
    ) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = null;
		
		// Select Algorithm based on choice
		switch(circleAlgorithm) {
		
			/*
			 *  If Mid Point method was selected, use mid point circle
			 *  drawing algorithm (fall through case)
			 */
			case MID_POINT:
				
			// By default, use mid point based circle filling algorithm
			default:
				LoggerFactory.getLoggerInstance().log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using Mid Point Circle Drawing Algorithm"
				);
				pixels = midPointCircleDraw(center, radius, intensity);
		}
		
		// Return the computed pixel arraylist
		return pixels;
	}
	
	/**
	 * Draws a Filled Circle based on the center and radius provided
	 * using the selected algorithm
	 * 
	 * @param center Center of the filled circle
	 * @param radius Radius of the filled circle
	 * @param intensity Intensity of each pixel of the filled circle
	 * @return the arraylist of pixels of the filled circle
	 */
	public static ArrayList<Pixel> drawCircleFill(
		Position center,
		Radius radius,
		Intensity intensity
	) {
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = null;
		
		// Select Algorithm based on choice
		switch(fillAlgorithm) {
		
			/* 
			 * If Devansh method was selected, use Devansh Circle
			 * Filling Algorithm
			 */
			case DEVANSH:
				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using Devansh Circle Filling Algorithm"
				);
				pixels = devanshCircleFill(center, radius, intensity);
				break;
				
			// If BFS method was selected, use BFS fill Circle algorithm
			case BFS_FILL:
				pixels = bfsCircleFill(center, radius, intensity);
				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using BFS based Circle Filling Algorithm"
					);
				break;
			
			/*
			 *  If Mid Point Based method was selected, use Mid Point
			 *  Based Filling Algorithm (fall through case)
			 */
			case MID_POINT_BASED:
				
			// By Default, use Mid Point Based Filling Algorithm
			default:
				logger.log(
					ModuleID.PROCESSING, 
					LogLevel.INFO, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Using Mid Point Based Circle Filling Algorithm"
				);
				pixels = midPointBasedCircleFill(center, radius, intensity);
		}

		// Return the computed pixel arraylist
		return pixels;
	}
	
	/**
	 * Mid Point Circle Drawing Algorithm
	 * 
	 * @param center Center of the filled circle 
	 * @param radius Radius of the filled circle
	 * @param intensity Intensity of each pixel of the filled circle
	 * @return the arraylist of pixels of the filled circle
	 */
	private static ArrayList<Pixel> midPointCircleDraw(
		Position center,
        Radius radius,
        Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Get radius after rounding to nearest integer
		int rad = (int) Math.round(radius.radius);
		
		// Initial Position
		Position pos = new Position(0, rad);
		
		// Initial Decision Parameter
		int decisionParam = 1 - rad;
		
		// Add all points based on symmetry
		addSymmetricPoints(pixels, pos, intensity, center);

		// Run until we complete one octant
		while(pos.c > pos.r) {
			
			// If decision parameter is less than zero, then select East (E)
			if(decisionParam < 0)
				decisionParam += 2 * pos.r + 3;
			
			else { 	// If decision parameter is greater than or equal
					// to zero, then select South East (SE)
				
				decisionParam += 2 * pos.r - 2 * pos.c + 5;
				pos.c --;
			}
			
			// Increment row coordinate
			pos.r ++;
			
			// Add all points based on symmetry
			addSymmetricPoints(pixels, pos, intensity, center);
		}
		
		// Return the computed pixel arraylists
		return pixels;
	}
	
	/**
	 * Add all positions symmetric to the position provided
	 * 
	 * @param pixels ArrayList of pixels to which we add the points
	 * @param pos Position whose symmetric positions are to be added
	 * @param intensity Intensity of each symmetric position
	 * @param center Center of the circle
	 */
	private static void addSymmetricPoints(
		ArrayList<Pixel> pixels,
		Position pos,
		Intensity intensity,
		Position center
	) {
		// All symmetric points with respect to the eight octants 
		Position[] allSymmetricPos = new Position[] {
			new Position(pos.r, pos.c),
			new Position(pos.c, pos.r),
			new Position(pos.c, -pos.r),
			new Position(pos.r, -pos.c),
			new Position(-pos.r, -pos.c),
			new Position(-pos.c, -pos.r),
			new Position(-pos.c, pos.r),
			new Position(-pos.r, pos.c)
		};
		
		/*
		 *  Add all the symmetric positions after translating by
		 *  the center coordinates
		 */
		for(Position symmetricPos : allSymmetricPos) {
			pixels.add(new Pixel(
				new Position(
					symmetricPos.r + center.r, 
					symmetricPos.c + center.c
				),
				new Intensity(intensity)
			));
		}
	}
	
	private static ArrayList<Pixel> midPointBasedCircleFill(
		Position center,
        Radius radius,
        Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Get radius after rounding to nearest integer
		int rad = (int) Math.round(radius.radius);
		
		// Initial Position
		Position pos = new Position(0, rad);
		
		// Initial Decision Parameter
		int decisionParam = 1 - rad;
		
		// Add all cols based on symmetry
		fillSymmetric(pixels, pos, intensity, center);

		while(pos.c > pos.r) {
			
			// If decision parameter is less than zero, then select East (E)
			if(decisionParam < 0)
				decisionParam += 2 * pos.r + 3;
			
			else {	// If decision parameter is greater than or equal
					// to zero, then select South East (SE)
				
				decisionParam += 2 * pos.r - 2 * pos.c + 5;
				pos.c --;
			}
			
			// Increment row coordinate
			pos.r ++;
			
			// Add all cols based on symmetry
			fillSymmetric(pixels, pos, intensity, center);
		}
		
		// Return the computed pixel arraylists
		return pixels;
	}
	
	private static void fillSymmetric(
		ArrayList<Pixel> pixels,
		Position pos,
		Intensity intensity,
		Position center
	) {
		// All symmetric points at the right of the vertical axiss
		Position[] allFillSymmetricPos = new Position[] {
			new Position(pos.r, pos.c),
			new Position(pos.c, pos.r),
			new Position(pos.c, -pos.r),
			new Position(pos.r, -pos.c)
		};
		
		/*
		 *  For each point in allFillSymmetricPos construct a column
		 *  from the negative of the row val till the positive
		 */
		for(Position symmetricFillPos : allFillSymmetricPos) {
			for(int r = -symmetricFillPos.r;r <= symmetricFillPos.r;r++) {
				pixels.add(new Pixel(
					new Position(
						r + center.r,
						symmetricFillPos.c + center.c
					),
					new Intensity(intensity)
				));
			}
		}
	}
	
	/**
	 * Devansh Circle Filling Algorithm constructs a filled circle
	 * given the center and radius of the circle
	 * 
	 * @param center Center of the filled circle
	 * @param radius Radius of the filled circle
	 * @param intensity Intensity of each point in the filled circle
	 * @return arraylist of pixels of the filled circle
	 */
	private static ArrayList<Pixel> devanshCircleFill(
		Position center,
        Radius radius,
        Intensity intensity
	) {
		// Initialize arraylist of pixels
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Convert radius to integer by rounding
		int rad = (int) Math.round(radius.radius);
		
		// Compute square of radius
		int radSquare = rad * rad;
		
		/* 
		 * Go over every pixel of the smallest square that encompasses
		 * the circle (i.e. a square of side equal to the diameter of
		 * the circle) and then if the point lies on or inside the circle
		 * place it in our arraylist of pixels
		 */
		for(int r = center.r - rad;r <= center.r + rad;r++) {
			for(int c = center.c - rad;c <= center.c + rad;c++) {
				
				/*
				 *  If the point lies on or inside the circle, then place it
				 *  in our arraylist of pixels
				 */
				if(square(r - center.r) + square(c - center.c) <= radSquare) {
					pixels.add(new Pixel(
						new Position(r, c),
						new Intensity(intensity)
					));
				}
			}
		}
		
		// Return the computed pixel arraylist
		return pixels;
	}
	
	/** Function to square its input */
	private static int square(int x) {
		return x * x;
	}
	
	/**
	 * Draws a Filled Circle based on the center and radius provided
	 * using the BFS algorithm
	 * 
	 * @author Rakesh Kumar
	 * 
	 * @param center Center of the filled circle
	 * @param radius Radius of the filled circle
	 * @param intensity Intensity of each pixel of the filled circle
	 * @return the arraylist of pixels of the filled circle
	 */
	private static ArrayList <Pixel> bfsCircleFill(
			Position center,
	        Radius radius,
	        Intensity intensity
		) {
		
		int rad = (int) Math.round(radius.radius);
		// Compute square of radius
		int radSquare = rad * rad;
		
		// BFS queue
		Queue <Position> queuePos = new LinkedList <Position> ();
		// stores the filled circle pixels
		ArrayList <Pixel> pixels = new ArrayList <Pixel> ();
		
		/**
		 *  2D boolean array for marking the visited positions
		 *  Size of the array is the size of the smallest square covering the circle 
		 *  The circle is shifted such that the positive axes are its tangents
		 */
		boolean[][] visitPos = new boolean[(rad << 1) + 1][(rad << 1) + 1];
		
		
		// BFS will start with center as the source position
		queuePos.add(center);
		// shifted circle center
		visitPos[rad][rad] = true;
		pixels.add(new Pixel(new Position(center), new Intensity(intensity)));
		
		/**
		 * dr, dc are changes in rows and cols respectively.
		 * These are used to find the adjacent positions
		 */
		ArrayList <Integer> dr = new ArrayList <Integer> (
				Arrays.asList(-1, -1, -1, 0, 0, 1, 1, 1)
				); 
		ArrayList <Integer> dc = new ArrayList <Integer>(
				Arrays.asList(-1, 0, 1, -1, 1, -1, 0, 1)
				);

		while (queuePos.size() > 0) {
			Position front = queuePos.remove();
			
			// pushes the adjacent valid unvisited positions into the queue
			for (Integer d = 0; d < dr.size(); d++) {
				int r = dr.get(d) + front.r;
				int c = dc.get(d) + front.c;
				Position pos = new Position(r, c);
				// Checks whether (r, c) is inside the circle or not
				if (square(r - center.r) + square(c - center.c) > radSquare)
					continue;
				if (visitPos[r - center.r + rad][c - center.c + rad] == false) {
					// if the position is not visited
					visitPos[r - center.r + rad][c - center.c + rad] = true;
					queuePos.add(pos);
					pixels.add(new Pixel(pos, new Intensity(intensity)));
				}
						
			}
		}
		
		// filled circle pixels
		return pixels;
	}
}
