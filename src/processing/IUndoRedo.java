package processing;

/**
 * IUndoRedo provides functions for undo and redo options
 * 
 * @author Himanshu Jain
 * @reviewer Ahmed Zaheer Dadarkar
 */

public interface IUndoRedo {
	/** redo will perform the redo operation */
	void redo();
	
	/** undo will perform the undo operation */
	void undo();
}