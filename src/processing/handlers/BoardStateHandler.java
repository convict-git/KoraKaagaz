package processing.handlers;

import java.io.IOException;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import processing.threading.*;
import processing.Serialize;
import networking.INotificationHandler;
import processing.BoardState;
import processing.ClientBoardState;

/**
 * This class handles Board State received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class BoardStateHandler implements INotificationHandler {
	
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	public static void handleBoardState(String message) {
		try {
			
			BoardState boardState = (BoardState)Serialize.deSerialize(message);
			ClientBoardState.maps = boardState;
			ClientBoardState.sendBoardState();
			
		} catch (ClassNotFoundException e) {
			
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] "+
					"BoardState Class not found while deserializing"
			);
			
		} catch (IOException e) {
			
			logger.log(ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] "+
					"Boardstate IO not found while deserializing"
			);
			
		}
	}
	
	public void onMessageReceived(String message) {
		
		HandleBoardState runnable = new HandleBoardState(message);
		Thread boardState = new Thread(runnable);
		boardState.start();
		
	}
}
