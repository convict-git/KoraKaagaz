package processing;

import processing.utility.*;
import java.util.*;

/**
 * This is to compare two PriorityQueueObject objects, to store them in a sorted way according
 * to timestamp in the priority queue.
 *
 * @author Himanshu Jain
 */

public class ObjectComparator implements Comparator <PriorityQueueObject>{
	
	/** return 1 if the first object came before, 0 if they came on same time otherwise -1 */
	public int compare(PriorityQueueObject obj1, PriorityQueueObject obj2) {
		if(obj1.timestamp.compareTo(obj2.timestamp) < 0) {
			return 1;
		}
		else if(obj1.timestamp.compareTo(obj2.timestamp) > 0) {
			return -1;
		}
		else return 0;
	}
	
}
