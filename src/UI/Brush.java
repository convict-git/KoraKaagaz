/**
 * Author: Anish Jain
 * This handles the brush and eraser mouse events for canavs.fxml
 */

package UI;
import java.io.*;

import processing.utility.*;
import processing.*;
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

	private static ArrayList<Position> position =new ArrayList<Position>();

	/**
	 * To set them to default boolean value as false
	 */
	public static void defaultSelected() {
		brushSelected = false;
		erasorSelected = false;
	}

	/**
	 *This is called on mouse release for brush to send all the pixels as an arraylist to the processing module
	 *@param color   any color selected
	 *@param g   context
	 *@param startx start pt(X)
	 *@param starty start pt(Y)
	 *@param endx end(dragged) pt(X)
	 *@param endy end(dragged) pt(Y)
	 *@returns nothing
	 */
	public static void drawBrush(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		g.setStroke(color);
		g.stroke();
		g.closePath();

		drawerase.drawCurve(pixels);
		pixels=new ArrayList<Pixel>();
    	}

	/**
	 *This is called on mouse release for eraser to send all positions as an arraylist to the processing module
	 *@param color   any color selected
	 *@param g   context
	 *@param startx start pt(X)
	 *@param starty start pt(Y)
	 *@param endx end(dragged) pt(X)
	 *@param endy end(dragged) pt(Y)
	 *@returns nothing
	 */
	public static void drawEraser(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		g.closePath();

		drawerase.erase(position);
		position=new ArrayList<Position>();
    	}

	/**
	 *It is to show the brush effect while mouse drag and collect data to send to processing team
	 *@param canvas  canvas to do on
	 *@param color   any color selected
	 *@param g   context
	 *@param startx start pt(X)
	 *@param starty start pt(Y)
	 *@param endx end(dragged) pt(X)
	 *@param endy end(dragged) pt(Y)
	 *@param size brushsize
	 *@returns nothing
	 */
	public static void drawBrushEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double startx, double starty, double endx, double endy,double size
	) {
		g.stroke();
		g.setLineCap(StrokeLineCap.ROUND);
		g.setLineWidth(size);
		g.setStroke(color);
		g.strokeLine(startx,starty,endx,endy);

		double x=0,y=0,m=0,flag=0;

		/**
		 * Check if slope is 0 or infinity.
		 * If either is there flag to a certain value
		 * m = slope
		 */
		if(startx==endx) {
			flag=1;
		}
		else if(endy==starty) {
			flag=2;
		}
		else {
			flag=0;
			m= (endy-starty)/(endx-startx);
		}

		/**
		 * Intensity in terms of RGB format
		 */
		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());

		/**
		 * The calculation of pixels
		 * Outer loop is for every 2 distance for line drawn from (startx , starty) to (endx , endy)
		 * Inner loop is half the size on both sides of line with distance 1.
		 * j and k are local loop variables.
		 */
		for (
			int j = 0;
			j <= (int)(Math.sqrt((endy - starty) * (endy - starty) + (endx - startx) * (endx - startx)));
			j+=2
		) {
			for (int k = (int)(-1 * size/2); k <= (int)(size/2); k+=1)
		    {
				if (flag==0) {
					x = startx + Math.sqrt((j*j)/(m * m + 1)) + ( Math.abs(m * k) * ( Math.sqrt(1/(1 + m * m)) ));
					y = starty + ( Math.abs(m) * Math.sqrt((j*j)/(m * m + 1))) - ( (1/m) * Math.abs(m * k) * Math.sqrt(1/(1 + m * m)));
				}
				else if (flag==1) {
					x = startx + k;
					y = starty + j;
				}
				else if (flag==1) {
					x = startx + j;
					y = starty + k;
				}

				/**
				 * start point for position
				 */
				Position  start = new Position((int) (x*100),(int) (y*100));
				Pixel p1 = new Pixel(start,i);
				pixels.add(p1);
			}
		}
	}

	/**
	 *It is to show the erase effect while mouse drag and collect data to send to processing team
	 *@param canvas  canvas to do on
	 *@param color   any color selected
	 *@param g   context
	 *@param startx start pt(X)
	 *@param starty start pt(Y)
	 *@param endx end(dragged) pt(X)
	 *@param endy end(dragged) pt(Y)
	 *@param size brushsize
	 *@returns nothing
	 */
	public static void drawEraserEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double startx, double starty, double endx, double endy,double size
	) {
		g.stroke();
		g.setLineCap(StrokeLineCap.ROUND);
		g.setLineWidth(size);
		g.setStroke(Color.WHITE);
		g.strokeLine(startx,starty,endx,endy);

		/**
		 * Check if slope is 0 or infinity.
		 * If either is there flag to a certain value
		 * m = slope
		 */
		double x=0,y=0,m=0,flag=0;
		if (startx==endx) {
			flag=1;
		}
		else if (endy==starty) {
			flag=2;
		}
		else {
			flag=0;
			m= (endy-starty)/(endx-startx);
		}

		/**
		 * The calculation of pixels
		 * Outer loop is for every 2 distance for line drawn from (startx , starty) to (endx , endy)
		 * Inner loop is half the size on both sides of line with distance 1.
		 * j and k are local loop variables.
		 */
		for (
			int j = 0;
			j <= (int)(Math.sqrt((endy - starty) * (endy - starty) + (endx - startx) * (endx - startx)));
			j+=2
		) {
			for (int k = (int)(-1 * size/2); k <= (int)(size/2); k+=1)
			{
				if (flag==0) {
					x = startx + Math.sqrt((j*j)/(m * m + 1)) + ( Math.abs(m * k) * ( Math.sqrt(1/(1 + m * m))));
					y = starty + ( Math.abs(m) * Math.sqrt((j*j)/(m * m + 1))) - ( (1/m) * Math.abs(m * k) * Math.sqrt(1/(1 + m * m)));
				}
				else if (flag==1) {
					x = startx + k;
					y = starty + j;
				}
				else if (flag==1) {
					x = startx + j;
					y = starty + k;
				}

				/**
				 * start point for position
				 */
				Position  start = new Position((int) (x*100),(int) (y*100));
				position.add(start);
			}
		}

	}
}
