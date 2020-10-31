package processing;

import java.util.*;
import processing.utility.*;
import processing.boardobject.BoardObject;

public class SelectDelete {

    public static ArrayList<Position> select (ArrayList <Position> positions) {

        BoardState board = ClientBoardState.maps;
        ObjectId mostProbableObjectId = board.getMostProbableObjectId(positions);

        if (mostProbableObjectId == null) {
            board.setSelectedObject(new PriorityQueueObject(null, null));
            return new ArrayList <Position>();              // returning empty list
        }

        BoardObject selected = ClientBoardState.maps.getBoardObjectFromId(mostProbableObjectId);
        if (selected == null) {
            board.setSelectedObject(new PriorityQueueObject(null, null));
            return new ArrayList<Position>();              // returning empty list
        }

        board.setSelectedObject(new PriorityQueueObject(selected.getObjectId(), selected.getTimestamp()));
        return selected.getPositions();
    }

    public static void delete (BoardObject object) {
        /**
         * Delete the object from the maps
         * If deleted object ID = selected Obj, set selectedObj = null
         */
    }
}
