package processing.utility;

import java.io.Serializable;
import java.util.Date;

/**
 * Class Representing a Timestamp
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class Timestamp implements Comparable<Timestamp>, Serializable {
	
	/** Serial UID */
	private static final long serialVersionUID = -1627727721294374145L;
	
	/** Timestamp is internally stored as a Date */
	private Date date;
	
	/**
	 * Timestamp Constructor
	 * 
	 * @param date Date to build the timestamp
	 */
	public Timestamp(Date date) {
		this.date = date;
	}
	
	/** Copy Constructor */
	public Timestamp(Timestamp timestampObj) {
		date = new Date(timestampObj.date.getTime());
	}
	
	/** Get Current Time */
	public static Timestamp getCurrentTime() {
		// Date() constructor when used, contains the time at which the
		// object of Date is built
		// read this:
		// https://docs.oracle.com/javase/7/docs/api/java/util/Date.html#Date()
		return new Timestamp(new Date());
	}
	
	/**
	 * Converts to String
	 * 
	 * @return Timestamp as a String
	 */
	public String toString() {
		return Long.toString(date.getTime());
	}
	
	/**
	 * Converts to Date
	 * 
	 * @return Timestamp as a Date
	 */
	public Date toDate() {
		return date;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp)obj;
			return date.equals(timestamp.date);
		}
		else
			return false;
	}

	/** Compare Method */
	@Override
	public int compareTo(Timestamp timestamp) {
		return date.compareTo(timestamp.date);
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return date.hashCode();
	}
}