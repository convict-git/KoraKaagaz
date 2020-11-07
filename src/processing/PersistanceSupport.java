package processing;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import processing.utility.BoardId;
import processing.utility.Filepath;

/**
 * PersistanceSupport Class which contains static methods for
 * saving and retrieving board state from the filesystem
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar, Himanshu Jain
 */

public class PersistanceSupport {
	
	/**
	 * Stores the board state string of the board corresponding to the
	 * provided Board ID in the file system after serializing it.
	 * The file containing board state of board given by boardId is named
	 * "${boardId}.kk"
	 * 
	 * @param boardStateString The board state String
	 * @param boardId Board ID of the board whose state is to be stored
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void storeStateString (
        String boardStateString,
        BoardId boardId
    ) throws IOException, UnsupportedEncodingException {
		
		// Construct path from the provided Board ID
		Filepath boardStatePath = 
			new Filepath(boardId.toString() + ".kk");
		
		// Create a file corresponding to the path provided
		// if it does not already exist
		File boardFile = new File(boardStatePath.toString());
		if(!boardFile.exists())
			boardFile.createNewFile();
		
		// Write the serialized string to the board state
		// after converting to an array of bytes using the
		// ISO-8859-1 encoding
		Files.write(
			boardFile.toPath(), 
			boardStateString.getBytes("ISO-8859-1")
		);
	}
	
	/**
	 * Loads the Board State String from the file containing the Serialized String
	 * corresponding to the board state.
	 * The file containing board state of board given by boardId is named
	 * "${boardId}.kk"
	 * 
	 * @param boardId Board ID of the board whose state is to be retrieveds
	 * @return the Board State String
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ClassNotFoundException
	 */
    public static String loadStateString (
        BoardId boardId
    ) throws IOException, UnsupportedEncodingException, ClassNotFoundException {
    	
    	// Construct path from the provided Board ID
		Filepath boardStatePath = 
			new Filepath(boardId.toString() + ".kk");
    	
    	// Get the file corresponding to the path
    	File boardFile = new File(boardStatePath.toString());
    	
    	// Read all the bytes from the file and construct the serialized string
    	// using the ISO-8859-1 encoding
    	byte[] boardStateBytes = Files.readAllBytes(boardFile.toPath());
    	String boardStateString = new String(boardStateBytes, "ISO-8859-1");
    	
    	// Return the String
    	return boardStateString;
    }
}
