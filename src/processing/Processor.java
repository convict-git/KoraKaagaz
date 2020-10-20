package processing;

import java.util.*;
import processing.utility.*;

/**
* This file implements all the function and uses the static functions defined by some other classes.
* UI will get an object of this class after they call the getProcessor function of ProcessingFactory class.
*
* @author Himanshu Jain
*/

public class Processor implements IDrawErase, IDrawShapes, IOperation, IUndoRedo, IUser {
	
	@Override
	public void drawCurve (ArrayList <Pixel> pixels) {
		return;
	}
	
	@Override
	public void erase (ArrayList <Position> position) {
		return;
	}
	
	@Override
	public void draw_circle(Pixel center, float radius) {
		return;
	}
	
	@Override
	public void draw_square(Pixel start, float length) {
		return;
	}
	
	@Override
	public void draw_rectangle(Pixel start, Pixel end) {
		return;
	}
	
	@Override
	public void draw_line(Pixel start, Pixel end) {
		return;
	}
	
	@Override
	public ArrayList<Position> select (ArrayList <Position> positions) {
		ArrayList<Position> selectedPixels = new ArrayList<Position>();
		return selectedPixels;
	}
	
	@Override
	public void delete() {
		return;
	}
	
	@Override
	public void colorChange (Intensity intensity) {
		return;
	}
	
	@Override
	public void rotate (Angle angleCCW) {
		return;
	}
	
	@Override
	public void reset () {
		return;
	}
	
	@Override
	public void undo() {
		return;
	}
	
	@Override
	public void redo() {
		return;
	}
	
	@Override
	public String getUserDetails(String userName, String ipAddress, String boardId) {
		String userId = new String();
		return userId;
	}
	
	@Override
	public String getUser(ArrayList<Position> positions) {
		String userId = new String();
		return userId;
	}
	
	@Override
	public void stopBoardSession() {
		return;
	}
	
	@Override
	public void subscribeForChanges(String identifier, IChanges handler) {
		return;
	}
}
