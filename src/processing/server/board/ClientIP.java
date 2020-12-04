package processing.server.board;

import java.util.*;

import processing.ClientBoardState;
import processing.utility.*;

/**
 * This class implements interface IClientIP to send the
 * map containing all the users connected to this board server
 * along with their addresses.
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ClientIP implements IClientIP {
	
	/**
	 * making the constructor protected so that this class can not 
	 * be instantiated by program in some other package so as to 
	 * implement factory pattern.
	 */
	protected ClientIP() {};
	
	@Override
	public Map<Username, IpAddress> getClientIP() {
		Map<Username, IpAddress> userToIp = new HashMap<Username, IpAddress>();
		
		for(Map.Entry<UserId, IpAddress> user : ClientBoardState.users.entrySet()) {
			userToIp.put(user.getKey().getUsername(), user.getValue());
		}
		
		return userToIp;
	}
	
}