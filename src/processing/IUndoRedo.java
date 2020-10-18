package processing;

/**
*
* @author Himanshu Jain
*/

public interface IUndoRedo {
    // redo the work done by a particular user
    void redo();
    
    // undo the work done by a particular user
    void undo();
}