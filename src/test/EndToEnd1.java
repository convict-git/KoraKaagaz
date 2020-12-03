package test;

import java.io.IOException;
import java.util.ArrayList;
import processing.IChanges;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.Pixel;

/**
* This file does an End to End test of the KoraKaagaz application. The test
* is achieved by two clients connecting to the same board where one client 
* acts as a sender and the other as a receiver. The sender client sends some 
* pixels and the receiver client tries to catch the pixels. The sent pixels 
* and the received pixels must match for the test to pass. 
* @author Muhammed Yaseen
*
*/

public class EndToEnd1 implements IChanges {
    public static void main(String[] args) throws InterruptedException, IOException {
        
        ProcessBuilder processBuilder1 = new ProcessBuilder();
        ProcessBuilder processBuilder2 = new ProcessBuilder();
        
        // Create Main Server
        processBuilder1.command(
            "java", 
            "-jar", 
            "MainServer.jar"
        );
        Process process1 = processBuilder1.start();
        // Wait for main server to start
        Thread.sleep(3000);
        
        // Create Board
        Processor processor = ProcessingFactory.getProcessor();
        // IP of main server verified by running MainServer.jar separately
        String ip = "192.168.1.6";
        String boardId = processor.giveUserDetails("User1", ip, null);
        processor.subscribeForChanges("E2E", new EndToEnd1());
        
        // Files for storing the sent and received pixels
        String sendFilePath = "sendFile";
        String recvFilePath = "recvFile";
        
        // Create Sender client and connect to the same board
        processBuilder2.command(
            "java", 
            "-jar", 
            "ClientSender.jar",
            ip,
            boardId,
            sendFilePath
        );
        Process process2 = processBuilder2.start();
        // wait for the sender to run
        Thread.sleep(10000);	
        // Check File Equivalence for evaluation of the test
        if(FileHelp.diff(sendFilePath, recvFilePath)) {
        	System.out.println("E2E test passed");
        }
        else {
        	System.out.println("E2E test failed");
        }
        
        // stop the session and kill the forked processes
        processor.stopBoardSession();
        process1.destroy();
        process2.destroy();
        System.exit(0);
    }

    /** 
	 * getChanges will take all the changes as the input and passed to the UI.
	 * We are tapping it one layer above the UI for the test purpose.
	 * 
	 * @param pixels List of all the pixels where there is a change.
	 */
	@Override
	public void getChanges(ArrayList<Pixel> pixels) {}

	/**
	 * giveSelectedPixels will pass the list of pixels belonging to the
	 * selected object, if the selected object got changed in between
	 * by any other client.
	 * 
	 * @param pixels List of pixels belonging to the selected object
	 */
	@Override
	public void giveSelectedPixels(ArrayList<Pixel> pixels) {}
}