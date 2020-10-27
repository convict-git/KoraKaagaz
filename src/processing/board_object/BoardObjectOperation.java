package processing.board_object;

/**
 * This interface would provide the operation that would
 * be performed on an object.
 *
 * @author Ahmed Zaheer Dadarkar
 */

public interface BoardObjectOperation {
	/**
	 * Get the enum type of the operation to be performed
	 * 
	 * This method would give the type, which would then help 
	 * us to (downcast) cast it into its original dynamic type for
	 * retrieving the parameters
	 * 
	 * @return the enum type of the operation to be performed
	 */
    public BoardObjectOperationType getOperationType();
}
