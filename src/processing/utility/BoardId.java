package processing.utility;

import java.io.Serializable;

/**
 * Class Representing a Board ID
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class BoardId implements Serializable  {
	
	/** Serial UID */
	private static final long serialVersionUID = -6661933124255421896L;

	/** Board ID String */
	private String boardId;
	
	/**
	 * BoardId Constructor
	 * 
	 * @param boardId Board ID String
	 */
	public BoardId(String boardId) {
		this.boardId = boardId;
	}
	
	/** Copy Constructor */
	public BoardId(BoardId boardIdObj) {
		boardId = boardIdObj.boardId;
	}
	
	/**
	 * Converts to String
	 * 
	 * @return Board ID as a String
	 */
	public String toString() {
		return boardId;
	}
	
	/** Equals Method */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BoardId)
			return boardId.equals(((BoardId)obj).boardId);
		else
			return false;
	}
	
	/** HashCode Method */
	@Override
	public int hashCode() {
		return boardId.hashCode();
	}
}
