package processing.server.board;

import java.util.*;
import processing.utility.*;

/**
 * This class implements interface IClientIP
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 *
 */

public class ClientIP implements IClientIP {
	
	protected ClientIP() {};
	
	@Override
	public Map<Username, IpAddress> getClientIP() {
		Map<Username, IpAddress> userToIp = new HashMap<Username, IpAddress>();
		return userToIp;
	}
	
}