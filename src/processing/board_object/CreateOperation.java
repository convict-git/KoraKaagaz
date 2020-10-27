package processing.board_object;

import java.io.Serializable;

/**
 * The Create Operation
 * 
 * It does not have any parameter
 *
 * @author Ahmed Zaheer Dadarkar
 */

public class CreateOperation implements BoardObjectOperation, Serializable {
	
	/** UID of this serializable class */
	private static final long serialVersionUID = 8647835000907198741L;

	/** Constructor for the Create Operation */
	public CreateOperation() {}
	
	/** Returns the type of operation */
	public BoardObjectOperationType getOperationType() {
		return BoardObjectOperationType.CREATE;
	}
}
