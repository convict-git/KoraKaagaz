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
 * start a new board process. It will send new boardId and 
 * Port number of board server.
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
		
		/* Initialize the variables in Processor Module */
		logger.log(
				ModuleID.PROCESSING,
				LogLevel.INFO,
				"MainServerTest: Initialise server and processor for test."
		);
		
		/* Initialise the main server */
		TestUtil.initialiseMainServerForTest();
		
		TestUtil.initialiseProcessorForTest(new ClientObjectHandler());
		
		// send request for new board to server
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"MainServerTest: New board request failed."
			);
			
			return false;
		}
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// wait for 2 sec to receive the output
		}
		
		if (ClientBoardState.boardId == null || ClientBoardState.portNumber == null) {
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"MainServerTest: Failed to receive boardId and portNumber!."
			);
			
			setError("New Board: Failed to receive boardId and portNumber.");
			return false;
		}

		// send request for an existing board to server 
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.WARNING, 
					"MainServerTest: Existing board request failed."
			);
			
			return false;
		}
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// wait for 2 sec to receive the output
		}
		
		if (ClientBoardState.boardId == null || ClientBoardState.portNumber == null) {
			logger.log(
					ModuleID.PROCESSING, 
					LogLevel.ERROR, 
					"MainServerTest: Failed!."
			);
			
			setError("Existing Board: Failed to receive boardId and portNumber.");
			return false;
		} else {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.SUCCESS,
					"MainServerTest: Successfull."
			);
			return true;
		}
	}
}
