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
	 * This is called on mouse release for brush to send all the pixels as an arraylist to the processing module
	 * @param color   any color selected
	 * @param g   context
	 * @param x12 start pt(X)
	 * @param y12 start pt(Y)
	 * @param x22 end(dragged) pt(X)
	 * @param y22 end(dragged) pt(Y)
	 * @returns nothing
	 */
	public static void drawBrush(Color color,GraphicsContext g, double x12, double y12, double x22, double y22) {
		g.setStroke(color);
		g.stroke();
		g.closePath();

		drawerase.drawCurve(pixels);
		pixels=new ArrayList<Pixel>();
    	}

	/**
	 * This is called on mouse release for eraser to send all positions as an arraylist to the processing module
	 * @param color   any color selected
	 * @param g   context
	 * @param x12 start pt(X)
	 * @param y12 start pt(Y)
	 * @param x22 end(dragged) pt(X)
	 * @param y22 end(dragged) pt(Y)
	 * @returns nothing
	 */
	public static void drawEraser(Color color,GraphicsContext g, double x12, double y12, double x22, double y22) {
		g.stroke();
		g.closePath();

		drawerase.erase(position);
		position=new ArrayList<Position>();
    	}

	/**
	 * It is to show the brush effect while mouse drag and collect data to send to processing team
	 * @param canvas  canvas to do on
	 * @param color   any color selected
	 * @param g   context
	 * @param x12 start pt(X)
	 * @param y12 start pt(Y)
	 * @param x22 end(dragged) pt(X)
	 * @param y22 end(dragged) pt(Y)
	 * @param size brushsize
	 * @returns nothing
	 */
	public static void drawBrushEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double x12, double y12, double x22, double y22,double size
	) {
		double x = x22 - size / 2;
		double y = y22 - size / 2;

		g.setFill(color);
		g.fillRect(x, y, size, size);

		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
		for (int j = (int)y; j < (int)(y+size); j+=2)
		{
			for (int k = (int)x; k < (int)(x+size); k+=2)
		    	{
				Position  start = new Position((int) (j*100),(int) (k*100));
				Pixel p1 = new Pixel(start,i);
				pixels.add(p1);
		    	}
		}
	}

	/**
	 * It is to show the erase effect while mouse drag and collect data to send to processing team
	 * @param canvas  canvas to do on
	 * @param color   any color selected
	 * @param g   context
	 * @param x12 start pt(X)
	 * @param y12 start pt(Y)
	 * @param x22 end(dragged) pt(X)
	 * @param y22 end(dragged) pt(Y)
	 * @param size brushsize
	 * @returns nothing
	 */
	public static void drawEraserEffect(
		Canvas canvasB,
		Color color,
		GraphicsContext g,
		double x12, double y12, double x22, double y22,double size
	) {
		double x = x22 - size / 2;
		double y = y22 - size / 2;

		g.clearRect(x, y, size, size);

		for (int j = (int)y; j < (int)(y+size); j+=2)
		{
			for (int k = (int)x; k < (int)(x+size); k+=2)
		    {
				Position  start = new Position((int) (j*100),(int) (k*100));
				position.add(start);
		    }
		}
	}

}
