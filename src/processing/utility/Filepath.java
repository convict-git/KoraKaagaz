package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Filepath
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Filepath implements Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = 1394822813812979917L;
	
	/** Filepath String */
	private String filepath;
	
	/**
	 * Filepath Constructor
	 * 
	 * @param filepath Filepath String
	 */
	public Filepath(String filepath) {
		this.filepath = filepath;
	}
	
	/** Copy Constructor */
	public Filepath(Filepath filepathObject) {
		filepath = filepathObject.filepath;
	}
	
	/**
	 * Converts to String
	 * 
	 * @return Filepath as a String
	 */
	public String toString() {
		return filepath;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Filepath)
			return filepath.equals(((Filepath)obj).filepath);
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return filepath.hashCode();
	}
}
