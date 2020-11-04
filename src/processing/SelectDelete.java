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

        /* Get Most Probable ObjectId from the maps */
        ObjectId mostProbableObjectId = board.getMostProbableObjectId(positions);

        if (mostProbableObjectId == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);
            CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
                    .giveSelectedPixels(new ArrayList<Pixel>());
            return new ArrayList<Position>();                      // returning empty Position list
        }

        /* Lookup BoardObject from ObjectId, if ObjectId exists */
        BoardObject selected = board.getBoardObjectFromId(mostProbableObjectId);

        if (selected == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);
            CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
                    .giveSelectedPixels(new ArrayList<Pixel>());
            return new ArrayList<Position>();                      // returning empty Position list
        }

        /* Pass selected positions to UI for display */
        CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
                .giveSelectedPixels(selected.getPixels());

        /* Updated selectedObject data member in ClientBoardState */
        PriorityQueueObject newSelectedObject =
                new PriorityQueueObject(selected.getObjectId(), selected.getTimestamp());

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

        /* Set Delete Operation in Object */
        IBoardObjectOperation deleteOperation = new DeleteOperation();
        object.setOperation(deleteOperation);

        /* Delete the object from the maps */
        board.removeObjectFromMaps(object.getObjectId());

        /* Push in the undo stack */
        UndoRedo.pushIntoStack(object);

        /* Provide the changes in pixels to the UI */
        CommunicateChange.provideChanges(object.getPixels(), null);

        /*
         * if deletedObjectId == selectedObjectId
         * make selectedObject null and send empty list as selected pixels to UI
         */
        PriorityQueueObject selectedObject = ClientBoardState.getSelectedObject();
        if (selectedObject.objectId == object.getObjectId()) {
            ClientBoardState.setSelectedObject(null);
            CommunicateChange.identifierToHandler.get(CommunicateChange.identifierUI)
                    .giveSelectedPixels(new ArrayList<Pixel>());
        }
    }

}
