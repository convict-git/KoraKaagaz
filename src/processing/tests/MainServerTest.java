package processing.tests;

import java.util.ArrayList;

import infrastructure.validation.logger.ILogger;
import infrastructure.validation.logger.LogLevel;
import infrastructure.validation.logger.LoggerFactory;
import infrastructure.validation.logger.ModuleID;
import infrastructure.validation.testing.TestCase;

import processing.*;
import processing.server.main.*;
import processing.utility.*;
import processing.testsimulator.*;
import processing.testsimulator.handlers.*;
import processing.testsimulator.ui.*;

/**
 * Test for Main Server of Processing Module. 
 * Test new and existing board Request. 
 * 
 * The server and a client are simulated. Client sends 
 * first the request for new board and then for existing 
 * board. As the folder does not contain the jar file needed
 * to start a new board, server will just not be able to 
 * start a new board process but send the boardId for new board. 
 * This should suffice test purpose. 
 * 
 * @author Sakshi Rathore
 *
 */

public class MainServerTest extends TestCase {

	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the Main Server.");
		setCategory("Processing");
		setPriority(2);
		
		/* Get an instance of logger */
		ILogger logger = LoggerFactory.getLoggerInstance();
		
		try {
			/* run the server */
			MainServer.main(null);
		} catch (Exception e) {
			/* return and set error in case of unsuccessful processing */
			this.setError(e.toString());
			logger.log(ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"MainServerTest: Failed to start main server.");
			return false;
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "MainServerTest: Initialise processor for test.");
		
		TestUtil.initialiseMainServerForTest();
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		
		// send request for new board to server
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			logger.log(ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"MainServerTest: New board request failed.");
			return false;
		}
		
		/* wait till UI receives the output */
		while (ClientBoardState.boardId == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		BoardId boardId = ClientBoardState.boardId;
		/* wait till UI receives the output */
		while (ClientBoardState.portNumber == null) {
			try{
				Thread.sleep(100);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		Port boardServerPort = ClientBoardState.portNumber;
		
		System.out.println(boardId);
		System.out.println(boardServerPort);
		
		// send request for an existing board to server 
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			logger.log(ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"MainServerTest: Existing board request failed.");
			return false;
		}

		/* wait till UI receives the output */
		while (ClientBoardState.boardId == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		/* wait till UI receives the output */
		while (ClientBoardState.portNumber == null) {
			try{
				Thread.sleep(50);
			 } catch (Exception e) {
				 // wait until output received
			 }
		}
		System.out.println("BoardId : " + ClientBoardState.boardId);
		System.out.println("port : " + ClientBoardState.portNumber);
		
		System.out.println("bno: " + ServerState.boardNumber);
		return true;
	}
	
}
