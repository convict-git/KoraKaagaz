package processing.board_object;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// The Delete Operation
public class DeleteOperation implements BoardObjectOperation {
	
	// Empty Constructor
	public DeleteOperation() {}
	
	// Return the type of operation
	public BoardObjectOperationType getOperationType() {
		return BoardObjectOperationType.DELETE;
	}
}
