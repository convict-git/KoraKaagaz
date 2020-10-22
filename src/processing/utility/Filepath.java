package processing.utility;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the filepath
public class Filepath {
	private String filepath; // Filepath String
	
	// Construct using String
	public Filepath(String filepath) {
		this.filepath = filepath;
	}
	
	// Copy Constructor for Filepath Class
	public Filepath(Filepath filepathObject) {
		filepath = filepathObject.filepath;
	}
	
	// Convert to String
	public String toString() {
		return filepath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Filepath)
			return filepath == ((Filepath)obj).filepath;
		else
			return false;
	}
}
