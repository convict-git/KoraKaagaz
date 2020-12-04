/**
 * Author: Anish Jain
 * This handles the brush and eraser mouse events for canavs.fxml
 */

package UI;
import java.io.*;

import processing.utility.*;
import processing.*;
import processing.shape.LineDrawer;
import infrastructure.validation.logger.*;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

/**
 * This deals with the brush class that has both brush and eraser.
 * It has functions that are called by the canvas controller.
 * These functions draw and erase in mouse drag and release conditions
 */
public class Brush{

	static boolean brushSelected = false;

	static boolean erasorSelected = false;

	static boolean sizeSelected =false;

	private static IDrawErase drawerase = ProcessingFactory.getProcessor();

	private static ArrayList<Pixel> pixels=new ArrayList<Pixel>();
	
	private static ArrayList<Pixel> tempPixels=new ArrayList<Pixel>();

	private static ArrayList<Position> position =new ArrayList<Position>();

	/**
	 * To set them to default boolean value as false
	 */
	public static void defaultSelected() {
		brushSelected = false;
		erasorSelected = false;
	}

	/**
	 * This is called on mouse release for brush to send all the pixels as an arraylist to the processing module
	 * @param color   any color selected
	 * @param g   context
	 * @param startx start pt(X)
	 * @param starty start pt(Y)
	 * @param endx end(dragged) pt(X)
	 * @param endy end(dragged) pt(Y)
	 * @returns nothing
	 */
	public static void drawBrush(
		Color color,
		GraphicsContext g,
		double startx,
		double starty,
		double endx,
		double endy
	) {
		g.setStroke(color);
		g.stroke();
		g.closePath();

		drawerase.drawCurve(pixels);
		pixels = new ArrayList<Pixel>();
	}

	/**
	 * This is called on mouse release for eraser to send all positions as an arraylist to the processing module
	 * @param color   any color selected
	 * @param g   context
	 * @param startx start pt(X)
	 * @param starty start pt(Y)
	 * @param endx end(dragged) pt(X)
	 * @param endy end(dragged) pt(Y)
	 * @returns nothing
	 */
	public static void drawEraser(
		Color color,
		GraphicsContext g,
		double startx,
		double starty,
		double endx,
		double endy
	) {
		g.closePath();

		drawerase.erase(position);
		position = new ArrayList<Position>();
	}

	/**
	 * It is to show the brush effect while mouse drag and collect data to send to processing team
	 * @param canvas  canvas to do on
	 * @param color   any color selected
	 * @param g   context
	 * @param startx start pt(X)
	 * @param starty start pt(Y)
	 * @param endx end(dragged) pt(X)
	 * @param endy end(dragged) pt(Y)
	 * @param size brushsize
	 * @returns nothing
	 */
	public static void drawBrushEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double startx, double starty, double endx, double endy,double size
	) {
		double x = 0, y = 0, m = 0, flag = 0 , x1 = 0 , y1 = 0;

		/**
		 * Check if slope is 0 or infinity.
		 * If either is there flag to a certain value
		 * m = slope
		 */
		if(startx == endx) {
			flag = 1;
		}
		else if(endy == starty) {
			flag = 2;
		}
		else {
			flag = 0;
			m = (endy - starty) / (endx - startx);
		}

		/**
		 * Intensity in terms of RGB format
		 */
		Intensity i = new Intensity((int) Math.round(color.getRed()*255),(int) Math.round(color.getGreen()*255),(int) Math.round(color.getBlue()*255));
		
		/**
		 * Using the drawsegment to get pixels of the line with the size of brush
		 */
		for (int k = (int)(-1 * size/2); k <= (int)(size/2); k+=2) 
		{
			if(flag == 0) {
				x = startx + m * k * Math.sqrt(1/(m * m + 1));
				y = starty + k * Math.sqrt(1/(m * m + 1));
				x1 = endx + m * k * Math.sqrt(1/(m * m + 1));
				y1 = endy + k * Math.sqrt(1/(m * m + 1));
				
			}
			if(flag == 1) {
				x = startx + k;
				y = starty ;
				x1 = endx + k;
				y1 = endy ;
			}
			if(flag == 2) {
				x = startx;
				y = starty + k;
				x1 = endx;
				y1 = endy + k ;
			}
			Position  start = new Position((int) (x),(int) (y));
			Position  end = new Position((int) (x1),(int) (y1));
			tempPixels = LineDrawer.drawSegment(start,end,i);
			pixels.addAll(tempPixels);
			
			for(Pixel pix:tempPixels) {
				Position pos = pix.position;
				g.getPixelWriter().setColor(pos.r,pos.c,color);
			}
		}
	}

	/**
	 * It is to show the erase effect while mouse drag and collect data to send to processing team
	 * @param canvas  canvas to do on
	 * @param color   any color selected
	 * @param g   context
	 * @param startx start pt(X)
	 * @param starty start pt(Y)
	 * @param endx end(dragged) pt(X)
	 * @param endy end(dragged) pt(Y)
	 * @param size brushsize
	 * @returns nothing
	 */
	public static void drawEraserEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double startx, double starty, double endx, double endy,double size
	) {
		/**
		 * Check if slope is 0 or infinity.
		 * If either is there flag to a certain value
		 * m = slope
		 */
		double x = 0, y = 0, m = 0, flag = 0 , x1 = 0 , y1 = 0;
		if (startx == endx) {
			flag = 1;
		}
		else if (endy == starty) {
			flag = 2;
		}
		else {
			flag = 0;
			m = (endy - starty) / (endx - startx);
		}

		Intensity i = new Intensity(1,1,1);
		/**
		 * Using the drawsegment to get pixels of the line with the size of eraser
		 */
		for (int k = (int)(-1 * size/2); k <= (int)(size/2); k+=2) 
		{
			if(flag == 0) {
				x = startx + m * k * Math.sqrt(1/(m * m + 1));
				y = starty + k * Math.sqrt(1/(m * m + 1));
				x1 = endx + m * k * Math.sqrt(1/(m * m + 1));
				y1 = endy + k * Math.sqrt(1/(m * m + 1));
				
			}
			if(flag == 1) {
				x = startx + k;
				y = starty ;
				x1 = endx + k;
				y1 = endy ;
			}
			if(flag == 2) {
				x = startx;
				y = starty + k;
				x1 = endx;
				y1 = endy + k ;
			}
			Position  start = new Position((int) (x),(int) (y));
			Position  end = new Position((int) (x1),(int) (y1));
			tempPixels = LineDrawer.drawSegment(start,end,i);
			pixels.addAll(tempPixels);
			
			for(Pixel pix:tempPixels) {
				Position pos = pix.position;
				g.getPixelWriter().setColor(pos.r,pos.c,Color.WHITE);
			}
		}
		
		for(int j=0; j<pixels.size() ;j++) {
			position.add(pixels.get(j).position);
		}
	}
}
