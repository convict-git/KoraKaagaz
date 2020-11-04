package processing;

import java.util.*;
import processing.utility.*;
import processing.boardobject.*;

/**
 * This class contains static methods to implement selection and deletion
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class SelectDelete {
    private static final BoardState board = ClientBoardState.maps;

    /**
     * Computes all the positions of the object that gets selected
     * when the user clicks on some pixels on the board
     *
     * @param positions positions from the UI that the user clicked on
     * @return all positions of the object that got selected
     */
    public static ArrayList<Position> select (ArrayList <Position> positions) {

        PriorityQueueObject noObjectSelected = new PriorityQueueObject(null, null);
        ArrayList <Position> emptyPositionList = new ArrayList<>();

        /** Get Most Probable ObjectId from the maps */
        ObjectId mostProbableObjectId = board.getMostProbableObjectId(positions);

        if (mostProbableObjectId == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);
            // giveSelectedPixels(new ArrayList<Pixel>());     // returning empty list to UI
            return emptyPositionList;                       // returning empty list
        }

        /** Lookup BoardObject from ObjectId, if ObjectId exists */
        BoardObject selected = board.getBoardObjectFromId(mostProbableObjectId);

        if (selected == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);
            // giveSelectedPixels(new ArrayList<Pixel>());     // returning empty list to UI
            return emptyPositionList;                       // returning empty list
        }

        /** Pass selected positions to UI for display */
        // giveSelectedPixels(selected.getPositions());

        /** Updated selectedObject data member in ClientBoardState */
        PriorityQueueObject newSelectedObject = new PriorityQueueObject(selected.getObjectId(), selected.getTimestamp());
        ClientBoardState.setSelectedObject(newSelectedObject);
        return selected.getPositions();
    }

    /**
     * Deletes the input BoardObject from the board, updates the maps,
     * pushes the operation to the undo stack, provides changes in pixels
     * and selected positions to the UI
     *
     * @param object object to be deleted
     */
    public static void delete (BoardObject object) {

        /** Set Delete Operation in Object */
        IBoardObjectOperation deleteOperation = new DeleteOperation();
        object.setOperation(deleteOperation);

        /** Delete the object from the maps */
        board.removeObjectFromMaps(object.getObjectId());

        /** Push in the undo stack */
        UndoRedo.pushIntoStack(object);

        /** Provide the changes in pixels to the UI */
        CommunicateChange.provideChanges(object.getPixels(), null);

        /** if deletedObjectId == selectedObjectId then make selectedObject null and send changes in selected pixels to UI */
        PriorityQueueObject selectedObject = ClientBoardState.getSelectedObject();
        if (selectedObject.objectId == object.getObjectId()) {
            ClientBoardState.setSelectedObject(null);
            // giveSelectedPixels(new ArrayList<Pixel>());     // returning empty list
        }
    }

}
