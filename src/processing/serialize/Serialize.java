package processing.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
    ) throws IOException {
    	// Create Stream for outputting object as Bytes
    	ByteArrayOutputStream byteOutputStream = 
    		new ByteArrayOutputStream();
  
    	// Create Stream for passing serializable object to
    	// ByteArrayOutputStream
    	ObjectOutputStream objOutStream = 
    		new ObjectOutputStream(byteOutputStream);
    	
    	// Write the object and flush it (so that
    	// it is written immediately)
    	objOutStream.writeObject(serialObj);
    	objOutStream.flush();
    	
    	// Get the serialized String from stream
    	String serialString = byteOutputStream.toString();
    	
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
    ) throws IOException, ClassNotFoundException {
    	// Create Stream for reading the bytes of the object
    	// present in the string
    	ByteArrayInputStream byteInputStream = 
    		new ByteArrayInputStream(serialString.getBytes());
    	
    	// Create Stream for reading the object from the
    	// ByteArrayInputStream stream
    	ObjectInputStream objInputStream = 
    		new ObjectInputStream(byteInputStream);
    	
    	// Read the serialized object from the object input stream
    	Serializable serialObj = 
    		(Serializable)objInputStream.readObject();
    	
    	// Close the streams
    	objInputStream.close();
    	byteInputStream.close();
    	
    	return serialObj;
    }
}
