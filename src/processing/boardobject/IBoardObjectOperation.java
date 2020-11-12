package processing.boardobject;

/**
 * This interface would provide the operation that would
 * be performed on an object.
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public interface IBoardObjectOperation {
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
