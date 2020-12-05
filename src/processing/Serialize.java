package processing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Class containing static methods for serializing and
 * deserializing Serializable objects and Strings representing
 * them respectively
 * 
 * The Serialize class provides the serialize and deserialize
 * functions for Serializable objects. The 
 * {@link Serialize#deSerialize} function when applied on a
 * string serialized by the {@link Serialize#serialize} function
 * outputs the exact same object and its dynamic type
 * 
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class Serialize {

	/**
	 * Takes a Serializable object as input and outputs
	 * the corresponding serialized String
	 * 
	 * @param serialObj A Serializable Object
	 * @return the Serializable object as a String
	 * @throws IOException
	 */
	public static String serialize (
		Serializable serialObj
	) throws IOException, UnsupportedEncodingException {
		// Create Stream for outputting object as Bytes
    	ByteArrayOutputStream byteOutputStream = 
    		new ByteArrayOutputStream();
	  
	    GZIPOutputStream gzipOutputStream = 
	    	new GZIPOutputStream(byteOutputStream);
	    	
	    // Create Stream for passing serializable object to
		// ByteArrayOutputStream
		ObjectOutputStream objOutStream = 
			new ObjectOutputStream(gzipOutputStream);
		
		// Write the object and flush it (so that
		// it is written immediately)
		objOutStream.writeObject(serialObj);
		objOutStream.flush();
		gzipOutputStream.finish();
		
		// Get the serialized String from stream
		// The ISO-8859-1 preserves 1-to-1 mapping between bytes and characters,
		// Read this: https://stackoverflow.com/questions/9098022/
		// problems-converting-byte-array-to-string-and-back-to-byte-array
		String serialString = byteOutputStream.toString("ISO-8859-1");
		
		// Close the streams
		objOutStream.close();
		byteOutputStream.close();
		
		return serialString;
	}
	
	/**
	 * Takes a String representing a Serialized Object, and returns the object
	 * 
	 * @param serialString A String representing a Serializable object
	 * @return the Serializable object from the String
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Serializable deSerialize (
		String serialString
	) throws IOException, ClassNotFoundException, UnsupportedEncodingException {
		
		// Create Stream for reading the bytes of the object present in the string
		// The ISO-8859-1 preserves 1-to-1 mapping between bytes and characters,
		ByteArrayInputStream byteInputStream = 
			new ByteArrayInputStream(serialString.getBytes("ISO-8859-1"));
		
		GZIPInputStream gzipInputStream =
			new GZIPInputStream(byteInputStream);
		
		// Create Stream for reading the object from the
		// ByteArrayInputStream stream
		ObjectInputStream objInputStream = 
			new ObjectInputStream(gzipInputStream);
		
		// Read the serialized object from the object input stream
		Serializable serialObj = 
			(Serializable)objInputStream.readObject();
		
		// Close the streams
		objInputStream.close();
		byteInputStream.close();
		
		return serialObj;
	}
}
