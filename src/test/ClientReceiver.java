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

    // captures the pixels from the board and saves them to the file
    @Override
    public void getChanges(ArrayList<Pixel> pixels) {
        try {
            FileHelp.writePixels(pixels, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void giveSelectedPixels(ArrayList<Pixel> pixels) {
        try {
            FileHelp.writePixels(pixels, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}