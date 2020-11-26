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
     */
    public static void drawPerfectRect(Color color,GraphicsContext g, double x12, double y12, double d, double e) {
        double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw=Math.abs(x12-d);
        double ph=Math.abs(y12-e);
        g.setStroke(color);
        g.strokeRect(px, py, pw, ph);
        Position  start = new Position((int) (px*100),(int) (py*100));
        Position end = new Position((int) (px+pw)*100,(int) (py+ph)*100);
        Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
        Pixel p1 = new Pixel(start,i);
        Pixel p2 = new Pixel(end,i);
        drawshape.drawRectangle(p1,p2);
        logger.log(ModuleID.UI, LogLevel.INFO, "Rectangle drawn");
    }

    /**
     * This method will draw circle and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectCircle(Color color,GraphicsContext g, double x12, double y12, double d, double e) {
    	double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw = Math.abs(x12-d);
        double ph = Math.abs(y12-e);
        double r = Math.max(pw,ph);
        g.setStroke(color);
        g.strokeOval(px, py, r, r);
        Position  start = new Position((int) (px+(r/2))*100,(int) (py+(r/2))*100);
        Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
        Pixel p1 = new Pixel(start,i);
        drawshape.drawCircle(p1,(float) r/2);
        logger.log(ModuleID.UI, LogLevel.INFO, "Circle drawn");
	}

    /**
     * This method will draw line and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectLine(Color color,GraphicsContext g, double x12, double y12, double x22, double y22) {
    	g.setStroke(color);
        g.strokeLine(x12, y12, x22, y22);
        Position  start = new Position((int) x12*100,(int) y12*100);
        Position end = new Position((int) x22*100,(int) y22*100);
        Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
        Pixel p1 = new Pixel(start,i);
        Pixel p2 = new Pixel(end,i);
        drawshape.drawLine(p1, p2);
        logger.log(ModuleID.UI, LogLevel.INFO, "Line drawn");
	}

    /**
     * This method will draw square and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectSquare(Color color,GraphicsContext g, double x12, double y12, double d, double e) {
    	double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw = Math.abs(x12-d);
        double ph = Math.abs(y12-e);
        double r = Math.max(pw,ph);
        g.setStroke(color);
        g.strokeRect(px, py, r, r);
        Position  start = new Position((int) (px*100),(int) (py*100));
        Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
        Pixel p1 = new Pixel(start,i);
        drawshape.drawCircle(p1,(float) r);
        logger.log(ModuleID.UI, LogLevel.INFO, "Square drawn");
	}

    /**
     * This method will draw triangle and sends the data to processing module
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectTriangle(Color color,GraphicsContext g, double x12, double y12, double d, double e) {
		g.setStroke(color);
        double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        if (py==y12) {
        	double x3 = px + Math.abs(x12-d)/2;
        	double y3 = y12;
        	final double xs[] = new double[3];
        	final double ys[] = new double[3];
        	xs[0]=x3; xs[1]=x12; xs[2]=d;
        	ys[0]=y3; ys[1]=e; ys[2]=e;
        	g.strokePolygon(xs,ys,3);
        	Position a = new Position((int) xs[0]*100,(int) ys[0]*100);
            Position b = new Position((int) xs[1]*100,(int) ys[1]*100);
            Position c = new Position((int) xs[2]*100,(int) ys[2]*100);
            Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
            Pixel p1 = new Pixel(a,i);
            Pixel p2 = new Pixel(b,i);
            Pixel p3 = new Pixel(c,i);
            //drawTriangle method is not included in IDrawShapes interface
            //drawshape.drawTriangle(p1, p2, p3);
        }
        else {
        	double x3 = px + Math.abs(x12-d)/2;
        	double y3 = e;
        	final double xs[] = new double[3];
        	final double ys[] = new double[3];
        	xs[0]=x3; xs[1]=x12; xs[2]=d;
        	ys[0]=y3; ys[1]=y12; ys[2]=y12;
        	g.strokePolygon(xs,ys,3);
        	Position a = new Position((int) xs[0]*100,(int) ys[0]*100);
            Position b = new Position((int) xs[1]*100,(int) ys[1]*100);
            Position c = new Position((int) xs[2]*100,(int) ys[2]*100);
            Intensity i = new Intensity((int) color.getRed(),(int) color.getGreen(),(int) color.getBlue());
            Pixel p1 = new Pixel(a,i);
            Pixel p2 = new Pixel(b,i);
            Pixel p3 = new Pixel(c,i);
            //drawTriangle method is not included in IDrawShapes interface
            //drawshape.drawTriangle(p1, p2, p3);
        }
        logger.log(ModuleID.UI, LogLevel.SUCCESS, "Triangle drawn");
	}

    /**
     * This method will create the scaling effect for rectangle on back canvas while dragging the mouse
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
	public static void drawPerfectRectEffect(Canvas canvasB,Color color,GraphicsContext g, double x12, double y12, double d, double e) {
        double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw=Math.abs(x12-d);
        double ph=Math.abs(y12-e);
        g.setStroke(color);
        g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        g.strokeRect(px, py, pw, ph);
    }

	/**
     * This method will create the scaling effect for circle on back canvas while dragging the mouse
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
	public static void drawPerfectCircleEffect(Canvas canvasB,Color color,GraphicsContext g, double x12, double y12, double d, double e) {
    	double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw = Math.abs(x12-d);
        double ph = Math.abs(y12-e);
        double r = Math.max(pw,ph);
        g.setStroke(color);
        g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        g.strokeOval(px, py, r, r);
	}

	/**
     * This method will create the scaling effect for line on back canvas while dragging the mouse
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectLineEffect(Canvas canvasB,Color color,GraphicsContext g, double x12, double y12, double x22, double y22) {
    	g.setStroke(color);
    	g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        g.strokeLine(x12, y12, x22, y22);
	}

    /**
     * This method will create the scaling effect for square on back canvas while dragging the mouse
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectSquareEffect(Canvas canvasB,Color color,GraphicsContext g, double x12, double y12, double d, double e) {
    	double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        double pw = Math.abs(x12-d);
        double ph = Math.abs(y12-e);
        double r = Math.max(pw,ph);
        g.setStroke(color);
        g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        g.strokeRect(px, py, r, r);
	}

    /**
     * This method will create the scaling effect for triangle on back canvas while dragging the mouse
     * @param canvasB: Rear canvas
     * @param color: Color selected from color picker
     * @param g: object of graphicscontext
     */
    public static void drawPerfectTriangleEffect(Canvas canvasB,Color color,GraphicsContext g, double x12, double y12, double d, double e) {
		g.setStroke(color);
        double px = Math.min(x12,d);
        double py = Math.min(y12,e);
        if (py==y12) {
        	double x3 = px + Math.abs(x12-d)/2;
        	double y3 = y12;
        	final double xs[] = new double[3];
        	final double ys[] = new double[3];
        	xs[0]=x3; xs[1]=x12; xs[2]=d;
        	ys[0]=y3; ys[1]=e; ys[2]=e;
        	g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        	g.strokePolygon(xs,ys,3);
        }
        else {
        	double x3 = px + Math.abs(x12-d)/2;
        	double y3 = e;
        	final double xs[] = new double[3];
        	final double ys[] = new double[3];
        	xs[0]=x3; xs[1]=x12; xs[2]=d;
        	ys[0]=y3; ys[1]=y12; ys[2]=y12;
        	g.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
        	g.strokePolygon(xs,ys,3);
        }
	}
}
