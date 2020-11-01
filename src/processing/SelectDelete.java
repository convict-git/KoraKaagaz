package processing;

import java.util.*;
import processing.utility.*;
import processing.boardobject.*;


public class SelectDelete {
    private static final BoardState board = ClientBoardState.maps;

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
        /** Set Delete Operation in Object */
        IBoardObjectOperation deleteOperation = new DeleteOperation();
        object.setOperation(deleteOperation);

        /** Delete the object from the maps */
        board.removeObjectFromMaps(object.getObjectId());

        /** Push in the undo stack */
        UndoRedo.pushIntoStack(object);

        /** Provide the changes in pixels to the UI */
        provideChanges(object.getPixels(), null);

        /** if deletedObjectId == selectedObjectId then make selectedObject null and send changes in selected pixels to UI */
        PriorityQueueObject selectedObject = board.getSelectedObject();
        if (selectedObject.objectId == object.getObjectId()) {
            board.setSelectedObject(null);
            // TODO add Himanshu's function to tell UI that the selected pixels have been modified (in this case, none at all)
        }
    }
}
