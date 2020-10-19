package processing.board_object;

/**
*
* @author Ahmed Zaheer Dadarkar
*/

// Supportive Datatype for BoardObjectOperation
// This datatype would be helpful to get the
// type of operation that should be performed
// on the object without giving information
// about the parameters of the operation
public enum BoardObjectOperationType {
    CREATE,        // Non-parametric operations
    DELETE,
    ROTATE,        // Parametric operations
    COLOR_CHANGE
}

// Note that CREATE and DELETE are non-parametric
// operations in the sense that they do not require
// parameters, while ROTATE and COLOR_CHANGE are
// parametric operations because they require
// the angle and intensity respectively