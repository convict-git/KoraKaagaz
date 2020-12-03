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
    
    public static boolean diff(String path1, String path2) throws IOException {
        return 
        Files.readString(Paths.get(path1)) 
        == 
        Files.readString(Paths.get(path2));
    }
}