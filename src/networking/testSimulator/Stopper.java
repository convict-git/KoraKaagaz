package networking.testSimulator;

/**
 * This Class is used for indicating the status of communication
 * @author sravan
 */
public class Stopper {
	
	private int stop;

	
	public Stopper() {
		this.stop = 0;
	}
	
	/**
	 * Method used to increment the stop variable
	 */
	public synchronized void incrementStop() {
		stop++;
	}
	/**
	 * This is method is used to get the value of stop
	 * @return int , returns the value of stop variable 
	 */
	public synchronized int getStop() {
		return stop;
	}
	
	/**
	 * This method used to get the status of stopper 
	 * @return boolean ,
	 */
	public synchronized  boolean getStatus() {
		if(getStop()>=4)return false;
		return true;
	}
}
