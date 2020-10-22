package processing.server.board;

import processing.utility.*;
import java.util.*;

/***
 * This interface will be used by the content module to get the ip addresses of
 * all the clients connected to the particular board.
 * 
 * 
 * @author Himanshu Jain
 *
 */

public interface IClientIP {
	
	public Map<Username, IpAddress> getClientDetails(); 
	
}