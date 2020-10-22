package processing.server.board;

import java.util.*;
import processing.utility.*;

/***
 * This class implements interface IClientDetails
 * 
 * @author Himanshu Jain
 *
 */

public class ClientDetails implements IClientDetails {
	
	@Override
	public Map<Username, IpAddress> getClientDetails() {
		Map<Username, IpAddress> userToIp = new HashMap<Username, IpAddress>();
		return userToIp;
	}
	
}
