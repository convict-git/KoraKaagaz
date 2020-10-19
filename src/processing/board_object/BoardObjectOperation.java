package processing.board_object;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

//This interface would provide the operation that would
// be performed on the object.
public interface BoardObjectOperation {
    // This method would give the type, which
    // would then help us to (downcast) cast
    // it into its original dynamic type for
    // retrieving the parameters
    public BoardObjectOperationType getOperationType();
}
