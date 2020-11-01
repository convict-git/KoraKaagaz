package processing;

import java.util.*;
import processing.utility.*;
import processing.boardobject.*;


public class SelectDelete {
    private static BoardState board = ClientBoardState.maps;

    public static ArrayList<Position> select (ArrayList <Position> positions) {

        ObjectId mostProbableObjectId = board.getMostProbableObjectId(positions);
        if (mostProbableObjectId == null) {
            board.setSelectedObject(new PriorityQueueObject(null, null));
            return new ArrayList <Position>();              // returning empty list
        }

        BoardObject selected = board.getBoardObjectFromId(mostProbableObjectId);
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
         * Provide the changes to the UI
         * If operation type is COLOR_CHANGE or ROTATE -
         *      do NOT push to stack for undo-redo
         * If deleted object ID = selected Obj, set selectedObj = null ONLY when NOT in COLOR_CHANGE, ROTATE operations
         */

        board.removeObjectFromMaps(object.getObjectId());
        // provide changes to the UI
        provideChanges(object.getPixels(), null);

        BoardObjectOperationType type = object.getOperation().getOperationType();

        if (type.equals( BoardObjectOperationType.ROTATE) || type.equals( BoardObjectOperationType.COLOR_CHANGE)) {
        }
        else {
            // push to undo stack
            UndoRedo.pushIntoStack(object);

            // if deletedObjectId == selectedObjectId then make selectedObject null and send changes to UI
            PriorityQueueObject selectedObject = board.getSelectedObject();
            if (selectedObject.objectId == object.getObjectId()) {
                board.setSelectedObject(null);
                // TODO add Himanshu's function to tell UI that the selected pixels have been modified (in this case, none at all)
            }
        }
    }
}
