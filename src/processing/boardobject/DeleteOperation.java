package processing.boardobject;

import java.io.Serializable;

/**
 * The Delete Operation
 * 
 * It does not have any parameter
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public class DeleteOperation implements IBoardObjectOperation, Serializable {
	
	/** UID of this serializable class */
	private static final long serialVersionUID = 1648404666296301004L;

	/** Returns the type of operation */
	public BoardObjectOperationType getOperationType() {
		return BoardObjectOperationType.DELETE;
	}
}
