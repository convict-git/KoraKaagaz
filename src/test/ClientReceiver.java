package test;

import java.io.IOException;
import java.util.ArrayList;
import processing.utility.Pixel;
import processing.IChanges;
import processing.ProcessingFactory;
import processing.Processor;
/**
* This file connects a client to a board with the boardId
* specifies in the argument. It receives the pixels from the board and stores
* them into a file.
* @author Muhammed Yaseen
*
*/

public class ClientReceiver implements IChanges {
    
    private static String serverIp, boardId, filepath;
    public static void main(String[] args) throws InterruptedException, IOException {
        serverIp = args[0];
        boardId = args[1];
        filepath = args[2];
        Processor processor = ProcessingFactory.getProcessor();
        processor.subscribeForChanges("ListenerRecv", new ClientReceiver());
        
        // Receiver connects to the same board with the name Receiver
        processor.giveUserDetails("Receiver", serverIp, boardId);
        
        // to receive the pixels
        Thread.sleep(13000);
        
        // stop the session
        processor.stopBoardSession(); 
    }

    /** 
	 * getChanges will take all the changes as the input and passed to the UI.
	 * We are tapping it one layer above the UI for the test purpose.
	 * 
	 * @param pixels List of all the pixels where there is a change.
	 */
    @Override
    public void getChanges(ArrayList<Pixel> pixels) {
        try {
            FileHelp.writePixels(pixels, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
	 * giveSelectedPixels will pass the list of pixels belonging to the
	 * selected object, if the selected object got changed in between
	 * by any other client.
	 * 
	 * @param pixels List of pixels belonging to the selected object
	 */
    @Override
    public void giveSelectedPixels(ArrayList<Pixel> pixels) {
        try {
            FileHelp.writePixels(pixels, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}