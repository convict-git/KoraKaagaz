package processing.board_object;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// The Create Operation
public class CreateOperation implements BoardObjectOperation {
	
	// Empty Constructor
	public CreateOperation() {}
	
	// Return the type of operation
	public BoardObjectOperationType getOperationType() {
		return BoardObjectOperationType.CREATE;
	}
}
