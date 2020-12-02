package UI;

/**
 * @author: Sajith Kumar Erasani
 * This controller handles all the action and mouse events of canvas for drawing and selecting Shapes
 */

import processing.utility.*;
import processing.*;
import infrastructure.validation.logger.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shapes {

	static boolean rectselected = false;
	static boolean circleselected = false;
	static boolean lineselected = false;
	static boolean triangleselected = false;
	static boolean squareselected = false;
	static boolean eraserselected = false;
	static IDrawShapes drawshape = ProcessingFactory.getProcessor();
	static ILogger logger = LoggerFactory.getLoggerInstance();

	/**
	 * This method will update the shape selection to be default
	 */
	public static void defaultSelected() {
		rectselected = false;
		circleselected = false;
		lineselected = false;
		triangleselected = false;
		squareselected = false;
		eraserselected = false;
	}

	/**
	 * This method will draw rectangle and sends the data to processing module
	 * @param color: Color selected from color picker
	 * @param g: object of graphicscontext
	 * @param startx,starty: start position of mouse drag
	 * @param endx,endy: end position of mouse drag
	 */
    public static void drawPerfectRect(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		/**
		 * To draw a rectangle, top left coordinates are required along with length and width of rectangle
		 * (topx,topy) are top left coordinates of rectangle
		 * x and y coordinates at top left corner of rectangle are minimum of all coordinates.
		 */
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length = Math.abs(startx-endx);
		double width = Math.abs(starty-endy);
		g.setStroke(color);
		g.strokeRect(topx, topy, length, width);
		Position start = new Position((int) (topx*100),(int) (topy*100));
		Position end = new Position((int) (topx+length)*100,(int) (topy+width)*100);
		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
		Pixel p1 = new Pixel(start,i);
		Pixel p2 = new Pixel(end,i);
		drawshape.drawRectangle(p1,p2);
		logger.log(
			ModuleID.UI,
			LogLevel.INFO,
			"Rectangle drawn from ("+topx+","+topy+") to ("+(topx+length)+","+(topy+width)+")"
		);
    }

    /**
     * This method takes start and end points of mouse drag draw circle and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectCircle(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		/**
		 * To draw a circle, top left coordinates of the boundary box are required along with diameter of circle
		 * Boundary Box is smallest rectangular region enclosing the shape
		 * Diameter of circle is nothing but the maximum of length, width of boundary box
		 */
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length = Math.abs(startx-endx);
		double width = Math.abs(starty-endy);
		double diameter = Math.max(length,width);
		g.setStroke(color);
		g.strokeOval(topx, topy, diameter, diameter);
		Position start = new Position((int) (topx+(diameter/2))*100,(int) (topy+(diameter/2))*100);
		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
		Pixel p1 = new Pixel(start,i);
		drawshape.drawCircle(p1,(float) diameter/2);
		logger.log(
			ModuleID.UI,
			LogLevel.INFO,
			"Circle drawn with center("+(topx+(diameter/2))+","+(topy+(diameter/2))+" radius:"+diameter/2
		);
	}

    /**
     * This method will draw line and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectLine(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		/**
		 * To draw a line start point and end point are required
		 */
		g.setStroke(color);
		g.strokeLine(startx, starty, endx, endy);
		Position start = new Position((int) startx*100,(int) starty*100);
		Position end = new Position((int) endx*100,(int) endy*100);
		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
		Pixel p1 = new Pixel(start,i);
		Pixel p2 = new Pixel(end,i);
		drawshape.drawLine(p1, p2);
		logger.log(
			ModuleID.UI,
			LogLevel.INFO,
			"Line drawn from ("+startx+","+starty+") to ("+endx+","+endy+")"
		);
	}

    /**
     * This method will draw square and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectSquare(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		/**
		 * We need to draw the rectangle with equal length and width to draw a square
		 * Here length of the square is maximum of length and width of rectangle
		 */
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length = Math.abs(startx-endx);
		double width = Math.abs(starty-endy);
		double len = Math.max(length,width);
		g.setStroke(color);
		g.strokeRect(topx, topy, len, len);
		Position  start = new Position((int) (topx*100),(int) (topy*100));
		Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
		Pixel p1 = new Pixel(start,i);
		drawshape.drawCircle(p1,(float) len);
		logger.log(
			ModuleID.UI,
			LogLevel.INFO,
			"Square drawn from ("+topx+","+topy+") with length:"+len
		);
	}

    /**
     * This method will draw triangle and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectTriangle(Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		/**
		 * To draw a triangle we need to calculate three points from start and end postions of mouse drag
		 * Here topx,topy are topleft coordinates of boundary box of triangle
		 * There are two cases here: (1) The end position of mouse drag is above start position
		 * 							 (2) The end position of mouse drag is below start position
		 * The coordinates of required triangle is based on the above two cases
		 */
    	g.setStroke(color);
        double topx = Math.min(startx,endx);
        double topy = Math.min(starty,endy);
        if (topy==starty) {
			/**
			 * xs[] stores all the x coordinates of triangle
			 * ys[] stores all respective the y coordinates of triangle
			 */
			double x3 = topx + Math.abs(startx-endx)/2;
			double y3 = starty;
			final double xs[] = new double[3];
			final double ys[] = new double[3];
			xs[0]=x3; xs[1]=startx; xs[2]=endx;
			ys[0]=y3; ys[1]=endy; ys[2]=endy;
			g.strokePolygon(xs,ys,3);
			Position a = new Position((int) xs[0]*100,(int) ys[0]*100);
			Position b = new Position((int) xs[1]*100,(int) ys[1]*100);
			Position c = new Position((int) xs[2]*100,(int) ys[2]*100);
			Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
			Pixel p1 = new Pixel(a,i);
			Pixel p2 = new Pixel(b,i);
			Pixel p3 = new Pixel(c,i);
			drawshape.drawTriangle(p1, p2, p3);
			logger.log(
				ModuleID.UI,
				LogLevel.INFO,
				"Triangle drawn with coordiates:("+xs[0]+","+ys[0]+"), ("+xs[1]+","+ys[1]+"), ("+xs[2]+","+ys[2]+")"
			);
    }
        else {
			/**
			 * xs[] stores all the x coordinates of triangle
			 * ys[] stores all respective the y coordinates of triangle
			 */
			double x3 = topx + Math.abs(startx-endx)/2;
			double y3 = endy;
			final double xs[] = new double[3];
			final double ys[] = new double[3];
			xs[0]=x3; xs[1]=startx; xs[2]=endx;
			ys[0]=y3; ys[1]=starty; ys[2]=starty;
			g.strokePolygon(xs,ys,3);
			Position a = new Position((int) xs[0]*100,(int) ys[0]*100);
			Position b = new Position((int) xs[1]*100,(int) ys[1]*100);
			Position c = new Position((int) xs[2]*100,(int) ys[2]*100);
			Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
			Pixel p1 = new Pixel(a,i);
			Pixel p2 = new Pixel(b,i);
			Pixel p3 = new Pixel(c,i);
			drawshape.drawTriangle(p1, p2, p3);
			logger.log(
				ModuleID.UI,
				LogLevel.INFO,
				"Triangle drawn with coordiates:("+xs[0]+","+ys[0]+"), ("+xs[1]+","+ys[1]+"), ("+xs[2]+","+ys[2]+")"
			);
        }
	}

    /**
     * This method will create the scaling effect for rectangle on back canvas while dragging the mouse
     * Logic: Draws and erases the shape for every drag postion in rear canvas(canvasB)
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
	public static void drawPerfectRectEffect(Canvas canvasB,Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length=Math.abs(startx-endx);
		double width=Math.abs(starty-endy);
		g.setStroke(color);
		g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
		g.strokeRect(topx, topy, length, width);
    }

	/**
     * This method will create the scaling effect for circle on back canvas while dragging the mouse
     * Logic: Draws and erases the shape for every drag postion in rear canvas(canvasB)
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
	public static void drawPerfectCircleEffect(Canvas canvasB,Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length = Math.abs(startx-endx);
		double width = Math.abs(starty-endy);
		double diameter = Math.max(length,width);
		g.setStroke(color);
		g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
		g.strokeOval(topx, topy, diameter, diameter);
	}

	/**
     * This method will create the scaling effect for line on back canvas while dragging the mouse
     * Logic: Draws and erases the shape for every drag postion in rear canvas(canvasB)
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectLineEffect(Canvas canvasB,Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		g.setStroke(color);
		g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
		g.strokeLine(startx, starty, endx, endy);
	}

    /**
     * This method will create the scaling effect for square on back canvas while dragging the mouse
     * Logic: Draws and erases the shape for every drag postion in rear canvas(canvasB)
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectSquareEffect(Canvas canvasB,Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
		double length = Math.abs(startx-endx);
		double width = Math.abs(starty-endy);
		double len = Math.max(length,width);
		g.setStroke(color);
		g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
		g.strokeRect(topx, topy, len, len);
	}

    /**
     * This method will create the scaling effect for triangle on back canvas while dragging the mouse
     * Logic: Draws and erases the shape for every drag postion in rear canvas(canvasB)
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     * @param startx,starty: start position of mouse drag
     * @param endx,endy: end position of mouse drag
     */
    public static void drawPerfectTriangleEffect(Canvas canvasB,Color color,GraphicsContext g, double startx, double starty, double endx, double endy) {
		g.setStroke(color);
		double topx = Math.min(startx,endx);
		double topy = Math.min(starty,endy);
        if (topy==starty) {
			double x3 = topx + Math.abs(startx-endx)/2;
			double y3 = starty;
			final double xs[] = new double[3];
			final double ys[] = new double[3];
			xs[0]=x3; xs[1]=startx; xs[2]=endx;
			ys[0]=y3; ys[1]=endy; ys[2]=endy;
			g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
			g.strokePolygon(xs,ys,3);
        }
        else {
			double x3 = topx + Math.abs(startx-endx)/2;
			double y3 = endy;
			final double xs[] = new double[3];
			final double ys[] = new double[3];
			xs[0]=x3; xs[1]=startx; xs[2]=endx;
			ys[0]=y3; ys[1]=starty; ys[2]=starty;
			g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
			g.strokePolygon(xs,ys,3);
        }
	}
}
