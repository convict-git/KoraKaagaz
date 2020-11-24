package processing.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import processing.utility.*;

/**
 * Methods for drawing Triangles
 *
 * @author Ahmed Zaheer Dadarkar, Rakesh Kumar
 * @reviewer Rakesh Kumar, Ahmed Zaheer Dadarkar
 */

public class TriangleDrawer {
	/**
	 * Draws a triangle given the three vertices of the triangles
	 * 
	 * @param vertA First vertex of the triangle
	 * @param vertB Second vertex of the triangle
	 * @param vertC Third vertex of the triangle
	 * @param intensity Intensity of each point on the triangle
	 * @return arraylist of pixel values of the border points of the
	 * triangle
	 */
	public static ArrayList<Pixel> drawTriangle (
		Position vertA,
        Position vertB,
        Position vertC,
        Intensity intensity
	) {
		// Initialize pixel values
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		// Draw line between A and B
		pixels.addAll(LineDrawer.drawSegment(vertA, vertB, intensity));
		
		// Draw line between B and C
		pixels.addAll(LineDrawer.drawSegment(vertB, vertC, intensity));
		
		// Draw line between C and A
		pixels.addAll(LineDrawer.drawSegment(vertC, vertA, intensity));
		
		// Return the constructed pixels
		return pixels;
	}
	
	/**
	 * Area = [x1(y2 - y3) + x2(y3 - y1) + x3(y1 - y2)] / 2
	 * This method computes the twice the area of the triangle using
	 * the above formula
	 * 
	 * @author Rakesh Kumar
	 * 
	 * @param vertA (x1, y1)
	 * @param vertB (x2, y2)
	 * @param vertC (x3, y3)
	 * @return
	 */
	private static int areaTriangle (
			Position vertA,
	        Position vertB,
	        Position vertC
	) {
		int area = vertA.c * (vertB.r - vertC.r);
		area += vertB.c * (vertC.r - vertA.r);
		area += vertC.c * (vertA.r - vertB.r);
		
		return Math.abs(area);
	}
	
	/**
	 * Finds if pos is inside the triangle
	 * formed by the vertices vertA, vertB and vertC
	 * 
	 * @author Rakesh Kumar
	 * 
	 * @param vertA First vertex of the triangle
	 * @param vertB Second vertex of the triangle
	 * @param vertC Third vertex of the triangle
	 * @param pos position
	 * @return
	 */
	private static Boolean isInside(
			Position vertA,
	        Position vertB,
	        Position vertC,
	        Position pos
	) {
		int areaPAB = areaTriangle(pos, vertA, vertB);
		int areaPBC = areaTriangle(pos, vertB, vertC);
		int areaPCA = areaTriangle(pos, vertC, vertA);
		int areaABC = areaTriangle(vertA, vertB, vertC);
		
		// The area is same only when the pos lies inside the triangle
		return (areaPAB + areaPBC + areaPCA == areaABC);
	}
	
	/**
	 * Draws a filled triangle given by three vertices
	 * Uses BFS to fill the triangle
	 * 
	 * @author Rakesh Kumar
	 * 
	 * @param vertA First vertex of the triangle
	 * @param vertB Second vertex of the triangle
	 * @param vertC Third vertex of the triangle
	 * @param intensity Intensity of each point on the triangle
	 * @return filled triangle with the given intensity
	 */
	public static ArrayList <Pixel> drawTriangleFill (
			Position vertA,
	        Position vertB,
	        Position vertC,
	        Intensity intensity
	) {
		// BFS queue
		Queue <Position> queuePos = new LinkedList <Position> ();
		ArrayList <Pixel> pixels = new ArrayList <Pixel> ();
		
		// set for keeping the visited positions
		Set <Position> visitPos = new HashSet <Position> ();
		
		int centR = (vertA.r + vertB.r + vertC.r) / 3;
		int centC = (vertA.c + vertB.c + vertC.c) / 3;
		Position centroid = new Position(centR, centC);
		
		// BFS will start with centroid as the source position
		queuePos.add(centroid);
		visitPos.add(centroid);
		
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
				if (visitPos.contains(pos))
					continue;
				if (isInside(vertA, vertB, vertC, pos)) {
					visitPos.add(pos);
					queuePos.add(pos);
				}
						
			}
		}
		
		// filled triangle pixels
		for (Position pos : visitPos)
			pixels.add(new Pixel(pos, new Intensity (intensity)));

		return pixels;
	}
}
