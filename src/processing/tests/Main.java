package processing.tests;

import infrastructure.validation.testing.TestCase;
import processing.utility.*;
public class Main {

	public static void main(String[] args) {
//		
//		DrawCurveTest test = new DrawCurveTest();
//		boolean result = test.run();
//		System.out.println(result);
		
		EraseTest test = new EraseTest();
		boolean result = test.run();
		System.out.println(result);
		
	}
}
