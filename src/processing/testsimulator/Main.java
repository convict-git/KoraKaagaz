package processing.testsimulator;

import processing.tests.*;

/**
 * Main file to run the modular test
 * Runs all the test in tests package
 * 
 * @author Sakshi Rathore
 *
 */

public class Main {

	public static void main(String[] args) {
		
		DrawCurveTest draw = new DrawCurveTest();
		System.out.println("Draw Curve: " + draw.run());
		
		EraseTest erase = new EraseTest();
		System.out.println("Erase: " + erase.run());
		
		DrawCircleTest circle = new DrawCircleTest();
		System.out.println("Draw Circle: " + circle.run());

		DrawSquareTest square = new DrawSquareTest();
		System.out.println("Draw Square: " + square.run());
		
		DrawRectangleTest rectangle = new DrawRectangleTest();
		System.out.println("Draw Rectangle: " + rectangle.run());
		
		DrawLineTest line = new DrawLineTest();
		System.out.println("Draw Line: " + line.run());
		
		DrawTriangleTest triangle = new DrawTriangleTest();
		System.out.println("Draw Triangle: " + triangle.run());
		
		SelectTest select = new SelectTest();
		System.out.println("Select: " + select.run());
		
		ResetTest reset = new ResetTest();
		System.out.println("Reset: " + reset.run());

		DeleteTest delete = new DeleteTest();
		System.out.println("Delete: " + delete.run());
		
		ColorChangeTest color = new ColorChangeTest();
		System.out.println("ColorChange: " + color.run());
		
		RotateTest rotate = new RotateTest();
		System.out.println("Rotate: " + rotate.run());
		
		UndoTest undo = new UndoTest();
		System.out.println("Undo: " + undo.run());
		
		RedoTest redo = new RedoTest();
		System.out.println("Redo: " + redo.run());
		
		GetUserTest username = new GetUserTest();
		System.out.println("UserName: " + username.run());
		
		MainServerTest mainServer = new MainServerTest();
		System.out.println("Server: " + mainServer.run());
	}
}
