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
 * 
 * @author Sakshi Rathore
 *
 */

public class MainServerTest extends TestCase {

	@Override
	public boolean run() {
		
		/* Use methods in TestCase to set the variables for test */
		setDescription("Test the delete API in IOperation interface.");
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
			return false;
		}
		
		/* Initialize the variables in Processor Module */
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "DeleteTest: Initialise processor for test.");
		
		TestUtil.initialiseMainServerForTest();
		TestUtil.initialiseProcessorForTest();
		ClientBoardState.boardId = null;
		System.out.println("BoardId initial : " + ClientBoardState.boardId);

		/* get an instance of IOperation interface */
		IUser user = ProcessingFactory.getProcessor();
		
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			System.out.println(error.toString());
			return false;
		}
		try {
			ClientBoardState.start();
		} catch (Exception error) {
			this.setError(error.toString());
			System.out.println(error.toString());
			return false;
		}
		System.out.println("BoardId : " + ClientBoardState.boardId);
		System.out.println("bno: " + ServerState.boardNumber);
		return true;
	}
	
}
