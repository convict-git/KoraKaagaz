package processing.utility;

import java.util.Date;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// This class corresponds to the Timestamp
public class Timestamp {
	private Date date; // Date

	public Timestamp(Date date) {
		this.date = date;
	}
	
	// Convert timestamp to String
	public String toString() {
		return date.toString();
	}
	
	// Convert timestamp to date
	public Date toDate() {
		return date;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp)obj;
			return date.equals(timestamp.date);
		}
		else
			return false;
	}
}
