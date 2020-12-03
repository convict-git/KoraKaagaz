package networking.testSimulator;

public class Stopper {
	private int stop;
	private boolean status;
	public Stopper() {
		this.stop = 0;
		this.status =true;
	}
	public synchronized void incrementStop() {
		stop++;
	}
	public synchronized int getStop() {
		return stop;
	}
	public synchronized  boolean getStatus() {
		if(getStop()>=4)return false;
		return true;
	}
}
