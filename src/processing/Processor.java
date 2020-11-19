package processing;

import java.util.*;

import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.ModuleID;
import processing.boardobject.*;
import processing.utility.*;
import processing.threading.*;

/**
 * This file implements all the function and uses the static functions defined by some other classes.
 * UI will get an object of this class after they call the getProcessor function of ProcessingFactory class.
 *
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public class Processor implements IDrawErase, IDrawShapes, IOperation, IUndoRedo, IUser {
	
	protected Processor() {};
	
	Thread selectThread;
	
	@Override
	public void drawCurve (ArrayList <Pixel> pixels) {
		
		if(pixels == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Null object given as argument for drawCurve from UI"
			);
			return;
		}
		
		IBoardObjectOperation boardOp = new CreateOperation();
		Timestamp time = Timestamp.getCurrentTime();
		ObjectId objectId = new ObjectId(ClientBoardState.userId, time);
		
		DrawCurve runnable = new DrawCurve(
				pixels, boardOp, objectId, time, 
				ClientBoardState.userId, 
				new ArrayList<Pixel>(), false
		);
		
		Thread drawCurveThread = new Thread(runnable);
		drawCurveThread.start();
	}
	
	@Override
	public void erase (ArrayList <Position> position) {
		
		if(position == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Null object given as argument for erase from UI"
			);
			return;
		}
		
		IBoardObjectOperation boardOp = new CreateOperation();
		Timestamp time = Timestamp.getCurrentTime();
		ObjectId objectId = new ObjectId(ClientBoardState.userId, time);
		
		Erase runnable = new Erase(position, boardOp, objectId, time, 
				ClientBoardState.userId, true
		);
		
		Thread eraseThread = new Thread(runnable);
		eraseThread.start();
	}
	
	@Override
	public void drawCircle(Pixel center, float radius) {
		
		if(center == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "center object given as argument is null for drawCircle from UI"
			);
			return;
		}	
		
		DrawCircle runnable = new DrawCircle(center.position, 
				new Radius(radius), center.intensity
		);
		
		Thread drawCircleThread = new Thread(runnable);
		drawCircleThread.start();
	}
	
	@Override
	public void drawSquare(Pixel start, float length) {
		
		if (start == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "start object given as argument is null for drawSquare from UI"
			);
			return;
		}
		
		DrawSquare runnable = new DrawSquare(start, length);
		Thread drawSquareThread = new Thread(runnable);
		drawSquareThread.start();
		
	}
	
	@Override
	public void drawRectangle(Pixel start, Pixel end) {
		
		if (start == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "start object given as argument is null for drawRectangle from UI"
			);
			return;
		}
		
		if (end == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "end object given as argument is null for drawRectangle from UI"
			);
			return;
		}
		
		DrawRectangle runnable = new DrawRectangle(start.position, end.position, start.intensity);
		Thread drawRectangle = new Thread(runnable);
		drawRectangle.start();
	}
	
	@Override
	public void drawLine(Pixel start, Pixel end) {
		
		if (start == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "start object given as argument is null for drawLine from UI"
			);
			return;
		}
		
		if (end == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "end object given as argument is null for drawLine from UI"
			);
			return;
		}
		
		DrawLine runnable = new DrawLine(start.position, end.position, start.intensity);
		Thread drawLine = new Thread(runnable);
		drawLine.start();
	}
	
	@Override
	public void drawTriangle(Pixel vertA, Pixel vertB, Pixel vertC) {
		
		if(vertA == null || vertB == null || vertC == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "one of the vertices given for drawing triangle is null from UI"
			);
			return;
		}
		
		DrawTriangle runnable = new DrawTriangle(
				vertA.position, 
				vertB.position, 
				vertC.position, 
				vertA.intensity
		);
		
		Thread drawTriangle = new Thread(runnable);
		drawTriangle.start();
	}
	
	@Override
	public ArrayList<Pixel> select (ArrayList <Position> positions) {
		
		if (positions == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "positions given as null argument for select function"
			);
			
			return new ArrayList<Pixel>();
		}
		
		Select runnable = new Select(positions);
		selectThread = new Thread(runnable);
		selectThread.start();
		
		/**
		 * waiting for the select thread to complete as we need to return the
		 * selected pixels back to the UI.
		 */
		try {
			selectThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Select Thread is interrupted while join select"
			);
		}
		return runnable.getSelectedObjectPixels();
	}
	
	@Override
	public void delete() {
		
		try {
			selectThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Select Thread is interrupted while join in Delete"
			);
		}
		
		BoardObject objectToDelete = ClientBoardState
				.maps
				.getBoardObjectFromId(
						ClientBoardState.getSelectedObject().objectId
				);
		
		Delete runnable = new Delete(objectToDelete, ClientBoardState.userId);
		Thread deleteThread = new Thread(runnable);
		deleteThread.start();
	}
	
	@Override
	public void colorChange (Intensity intensity) {
		
		if (intensity == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "intensity argument is given as null in colorChange"
			);
			return;
		}
		
		try {
			selectThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Select Thread is interrupted while join in Color Change"
			);
		}
		
		BoardObject selectedObject = ClientBoardState
				.maps
				.getBoardObjectFromId(
						ClientBoardState.getSelectedObject().objectId
				);
		
		ColorChange runnable = new ColorChange(
				selectedObject, 
				ClientBoardState.userId, 
				intensity
		);
		
		Thread colorChangeThread = new Thread(runnable);
		colorChangeThread.start();
	}
	
	@Override
	public void rotate (Angle angleCCW) {
		
		if (angleCCW == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "angleCCW argument is given as null in rotate"
			);
			return;
		}
		
		try {
			selectThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "Select Thread is interrupted while join in rotate"
			);
		}
		
		BoardObject selectedObject = ClientBoardState
				.maps
				.getBoardObjectFromId(
						ClientBoardState.getSelectedObject().objectId
				);
		
		Rotate runnable = new Rotate(
				selectedObject, 
				ClientBoardState.userId, 
				angleCCW
		);
		
		Thread rotateThread = new Thread(runnable);
		rotateThread.start();
	}
	
	@Override
	public void reset () {
		
		processing.threading.Reset runnable = new processing.threading.Reset(ClientBoardState.userId, true);
		Thread resetThread = new Thread(runnable);
		resetThread.start();
	}
	
	@Override
	public void undo() {
		
		Undo runnable = new Undo();
		Thread undoThread = new Thread(runnable);
		undoThread.start();
	}
	
	@Override
	public void redo() {
		
		Redo runnable = new Redo();
		Thread redoThread = new Thread(runnable);
		redoThread.start();
	}
	
	@Override
	public String giveUserDetails(String userName, String ipAddress, String boardId) {
		
		String ipWithoutPort = ipAddress.split(":")[0];
		
		GiveUserDetails runnable = new GiveUserDetails(userName, ipWithoutPort, boardId);
		Thread giveDetailsThread = new Thread(runnable);
		giveDetailsThread.start();
		
		try {
			giveDetailsThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "giveDetails Thread is interrupted while join"
			);
		}
		
		return ClientBoardState.boardId.toString();
	}
	
	@Override
	public String getUser(ArrayList<Position> positions) {
		
		if (positions == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "positions given as argument in getUser function is null"
			);
		}
		
		GetUser runnable = new GetUser(positions);
		Thread getUserThread = new Thread(runnable);
		getUserThread.start();
		
		try {
			getUserThread.join();
		} catch (InterruptedException e) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "getUser Thread is interrupted while join"
			);
		};
		
		return runnable.getUsername();
	}
	
	@Override
	public void stopBoardSession() {
		
		StopBoardSession runnable = new StopBoardSession();
		Thread stopSessionThread = new Thread(runnable);
		stopSessionThread.start();
	}
	
	@Override
	public void subscribeForChanges(String identifier, IChanges handler) {
		
		if (handler == null) {
			
			ClientBoardState.logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"[#" + Thread.currentThread().getId() + "] "
					+ "handler given as argument while subscribing is null"
			);
		}
		
		SubscribeForChanges runnable = new SubscribeForChanges(identifier, handler);
		Thread subscribeThread = new Thread(runnable);
		subscribeThread.start();
	}
}
