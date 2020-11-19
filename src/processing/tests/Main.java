package processing.tests;
/**
 * Main file to run the processing client side test
 * 
 * @author Sakshi
 *
 */

public class Main {

	public static void main(String[] args) {
		
		DrawCurveTest draw = new DrawCurveTest();
		System.out.println(draw.run());
		
		EraseTest erase = new EraseTest();
		System.out.println(erase.run());
		
		DrawCircleTest circle = new DrawCircleTest();
		System.out.println(circle.run());

		DrawSquareTest square = new DrawSquareTest();
		System.out.println(square.run());
		
		DrawRectangleTest rectangle = new DrawRectangleTest();
		System.out.println(rectangle.run());
		
		DrawLineTest line = new DrawLineTest();
		System.out.println(line.run());
		
		DrawTriangleTest triangle = new DrawTriangleTest();
		System.out.println(triangle.run());
		
	}
}
