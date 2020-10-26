package processing.server.board;

import processing.utility.*;
import java.util.*;

/**
 * This interface will be used by the content module to get the IP addresses of
 * all the clients connected to the particular board.
 * 
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IClientIP {
	
	/**
	 * getClientIP will return a Map of all the clients to IP
	 * 
	 * @return Map from all the clients to IP
	 */
	public Map<Username, IpAddress> getClientIP(); 
	
}