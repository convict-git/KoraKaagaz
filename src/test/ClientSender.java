package test;

/**
* This file connects a client to a board with the boardId
* specifies in the argument and sends some pixels to the board.
* The pixels are also saved to the file for evaluation.
* It also forks a Receiver client before sending the pixels.
* @author Muhammed Yaseen
*/

import java.io.IOException;
import java.util.ArrayList;
import processing.IChanges;
import processing.ProcessingFactory;
import processing.Processor;
import processing.utility.Intensity;
import processing.utility.Pixel;
import processing.utility.Position;

public class ClientSender implements IChanges {

    public static void main(String[] args) throws InterruptedException, IOException {
        String serverIp = args[0];
        String boardId = args[1];
        String filepath = args[2];
        
        Processor processor = ProcessingFactory.getProcessor();
        // Sender client connects to the board with the name Sender
        processor.giveUserDetails("Sender", serverIp, boardId);
        processor.subscribeForChanges("Listener", new ClientSender());
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Pixels for test purpose
        ArrayList<Pixel> pixels = new ArrayList<Pixel>();
        pixels.add(new Pixel(new Position(1, 2), new Intensity(1, 2, 3)));
        pixels.add(new Pixel(new Position(2, 3), new Intensity(1, 2, 3)));
        pixels.add(new Pixel(new Position(3, 4), new Intensity(1, 2, 3)));
        pixels.add(new Pixel(new Position(4, 5), new Intensity(1, 2, 3)));
        
        // Write the pixels to the file for evaluation purpose
        FileHelp.writePixels(pixels, filepath);
        
        // Create Receiver Client and connects to the same board
        String recvFilePath = "recvFile";
        processBuilder.command(
            "java", 
            "-jar", 
            "ClientReceiver.jar",
            serverIp,
            boardId,
            recvFilePath
        );
        Process process = processBuilder.start();
        // wait for receiver to run
        Thread.sleep(3000);
        
        // mimics the UI to draw the curve
        processor.drawCurve(pixels);
        Thread.sleep(10000);
        
        // stop the session
        processor.stopBoardSession();
        process.destroy();
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