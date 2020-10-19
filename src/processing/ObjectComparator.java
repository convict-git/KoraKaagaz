package processing;

import processing.utility.*;
import java.util.*;

/**
*
* @author Himanshu Jain
*/

public class ObjectComparator implements Comparator <PriorityQueueObject>{
	
	public int compare(PriorityQueueObject obj1, PriorityQueueObject obj2) {
		if(obj1.timestamp.compareTo(obj2.timestamp) < 0) {
			return 1;
		}
		else if(obj1.timestamp.compareTo(obj2.timestamp) > 0) {
			return -1;
		}
		return 0;
	}
	
}
