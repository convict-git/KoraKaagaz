package processing.utility;

import java.util.Date;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Timestamp
public class Timestamp implements Comparable<Timestamp> {
	private Date date; // Date

	// Constructor for Timestamp
	public Timestamp(Date date) {
		this.date = date;
	}
	
	// Copy Constructor for Timestamp
	public Timestamp(Timestamp timestampObj) {
		date = new Date(timestampObj.date.getTime());
	}
	
	// Convert timestamp to String
	public String toString() {
		return date.toString();
	}
	
	// Convert timestamp to date
	public Date toDate() {
		return date;
	}
	
	// Equals method
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp)obj;
			return date.equals(timestamp.date);
		}
		else
			return false;
	}

	// Compare method
	@Override
	public int compareTo(Timestamp timestamp) {
		return date.compareTo(timestamp.date);
	}
}