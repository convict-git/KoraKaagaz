package test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import processing.utility.Pixel;

/**
* This file contains helper functions for file writing
* and file comparing purposes.
* @author Muhammed Yaseen
*
*/
public class FileHelp {
	/**
	 * writePixels writes a pixel object into a file in the disk
	 * @param pixels List of pixels belonging to the test object from the sender
	 * @param filepath Relative path of the file to which pixels are to be
	 * written w.r.t. the executable jar file location
	 */
    public static void writePixels(
        ArrayList<Pixel> pixels, 
        String filepath
    ) throws IOException {
        
        FileWriter fileWriter = new FileWriter(filepath);
        
        for(Pixel pixel : pixels) {
            fileWriter.write(
                pixel.position.r
                + " " + pixel.position.c
                + " " + pixel.intensity.r
                + " " + pixel.intensity.g
                + " " + pixel.intensity.b
                + "\n"
            );
        }
        
        fileWriter.flush();
        fileWriter.close();
    }
    /**
	 * diff Helper function to check for equality of two files in the disk
	 * 
	 * @param path1 Relative path of file1 w.r.t. the jar executable
	 * @param path1 Relative path of file2 w.r.t. the jar executable
	 */
    public static boolean diff(String path1, String path2) throws IOException {
        return 
        Files.readString(Paths.get(path1)).equals(Files.readString(Paths.get(path2)));
    }
}