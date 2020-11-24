package processing.boardobject;

import java.io.Serializable;

/**
 * Enum type representing Board Object Operation
 * 
 * This is a Supportive Datatype for BoardObjectOperation,
 * This datatype would be helpful to get the type of operation
 * that would be performed on the object without giving information
 * about the parameters of the operation
 * 
 * Note that CREATE and DELETE are non-parametric operations, in the 
 * sense that they do not require parameters, while ROTATE and
 * COLOR_CHANGE are parametric operations because they require the
 * angle and intensity parameter respectively
 * 
 * Enum Objects are automatically serializable, hence UID is not
 * required to be specified,
 * @see <a href=https://docs.oracle.com/javase/1.5.0/docs/guide/
 * serialization/spec/serial-arch.html#enum>Enum Serialize</a>
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Rakesh Kumar
 */

public enum BoardObjectOperationType implements Serializable {
    CREATE,        /** Non-parametric operations begin */
    DELETE,
    ROTATE,        /** Parametric operations begin */
    COLOR_CHANGE
}
