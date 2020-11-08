package processing.utility;

/**
 * Class Representing a Board ID
 *
 * @author Ahmed Zaheer Dadarkar
 * @reviewer Himanshu Jain
 */

public class BoardId {
	/**
	 * Board ID String 
	 */
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
}
