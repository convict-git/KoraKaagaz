package processing.boardobject;

import java.io.Serializable;

/**
 * The Create Operation
 * 
 * It does not have any parameter
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class CreateOperation implements IBoardObjectOperation, Serializable {
	
	/** UID of this serializable class */
	private static final long serialVersionUID = 8647835000907198741L;
	
	/** Returns the type of operation */
	public BoardObjectOperationType getOperationType() {
		return BoardObjectOperationType.CREATE;
	}
}
