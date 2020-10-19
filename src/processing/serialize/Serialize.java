package processing.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// The Serialize class provides the serialize and deserialize
// functions for Serializable objects. The `deserialize`
// function when applied on a string serialized by the
// `serialize` function outputs the exact same object
// and its dynamic type.
public class Serialize {

    // The `serialize` function takes as input an object
    // `serialObj` which implements the Serializable interface
    // and outputs the object as a serialized String
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
    
    // The `deserialize` function takes as input a
    // String `serialString` which is the serialized form
    // of a Serializable object, and returns the object
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
