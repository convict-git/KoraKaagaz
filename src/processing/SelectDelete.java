package processing;

import java.util.*;
import processing.utility.*;
import processing.boardobject.*;
import infrastructure.validation.logger.*;

import static processing.ClientBoardState.logger;

/**
 * This class contains static methods to implement selection and deletion
 *
 * @author Shruti Umat
 * @reviewer Satchit Desai
 */

public class SelectDelete {

    /**
     * Computes all the positions of the object that gets selected
     * when the user clicks on some pixels on the board
     *
     * @param positions positions from the UI that the user clicked on
     * @return all positions of the object that got selected
     */
    public static ArrayList<Pixel> select (ArrayList <Position> positions) {
        boolean success = true;
        PriorityQueueObject noObjectSelected = new PriorityQueueObject(null, null);

        /* Get Most Probable ObjectId from the maps */
        ObjectId mostProbableObjectId = ClientBoardState
            .maps
            .getMostProbableObjectId(positions);

        if (mostProbableObjectId == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);

            try {
                CommunicateChange.identifierToHandler
                        .get(CommunicateChange.identifierUI)
                        .giveSelectedPixels(new ArrayList<Pixel>());

                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.INFO,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                                + "updated UI with empty pixel selection"
                );
            }
            catch (Exception e) {
                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.ERROR,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                                + "could not update UI with empty pixel selection"
                );
            }

            // returning empty Position list
            return new ArrayList<Pixel>();
        }

        /* Lookup BoardObject from ObjectId, if ObjectId exists */
        BoardObject selected = ClientBoardState
            .maps
            .getBoardObjectFromId(mostProbableObjectId);

        if (selected == null) {
            ClientBoardState.setSelectedObject(noObjectSelected);
            try {
                CommunicateChange.identifierToHandler
                        .get(CommunicateChange.identifierUI)
                        .giveSelectedPixels(new ArrayList<Pixel>());

                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.INFO,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                                + "mostProbableObject null : updated UI with empty pixel selection"
                );
            }
            catch (Exception e) {
                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.ERROR,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                                + "mostProbableObject null: couldn't update UI's selected pixels"
                );
            }

            // returning empty Position list
            return new ArrayList<Pixel>();
        }

        /* Pass selected positions to UI for display */
        try {
            CommunicateChange.identifierToHandler
                    .get(CommunicateChange.identifierUI)
                    .giveSelectedPixels(selected.getPixels());

            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.INFO,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                            + "updated selected pixels in UI using giveSelectedPixels"
            );
        }
        catch (Exception e) {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.ERROR,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                            + "could not pass selected pixels to UI using giveSelectedPixels"
            );
            success = false;
        }

        /* Updated selectedObject data member in ClientBoardState */
        PriorityQueueObject newSelectedObject =
                new PriorityQueueObject(selected.getObjectId(), selected.getTimestamp());

        ClientBoardState.setSelectedObject(newSelectedObject);

        if (success) {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.SUCCESS,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                            + "selected object updated in processing module internally "
                            + "and updated the UI"
            );
        }
        else {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.WARNING,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.select: "
                            + "selected object updated in processing module internally "
                            + "BUT could not update the UI"
            );
        }
        return selected.getPixels();
    }

    /**
     * Deletes the input BoardObject from the board, updates the maps,
     * pushes the operation to the undo stack, provides changes in pixels
     * and selected positions to the UI
     *
     * @param object object to be deleted
     * @param userId userId of the user who performed the deletion
     */
    public static BoardObject delete (BoardObject object, UserId userId) {
        boolean success = true;

        /* Set Delete Operation in Object */
        IBoardObjectOperation deleteOperation = new DeleteOperation();
        object.setOperation(deleteOperation);

        /* Set UserId of the user who performed the operation in Object */
        object.setUserId(userId);

        /* Delete the object from the maps */
        ClientBoardState.maps.removeObjectFromMaps(object.getObjectId());

        logger.log(
                ModuleID.PROCESSING,
                LogLevel.INFO,
                "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                        + "board object removed from maps"
        );

        try {
            /* Push in the undo stack */
            UndoRedo.pushIntoStack(object);
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.INFO,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "board object pushed into the undo stack"
            );
        }
        catch (Exception e) {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.ERROR,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "could not push the board object into the undo stack"
            );
            success = false;
        }

        try {
            /* Provide the changes in pixels to the UI */
            CommunicateChange.provideChanges(object.getPixels(), null);
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.INFO,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "provided pixel changes due to deletion to the UI"
            );
        }
        catch (Exception e) {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.ERROR,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "could not provideChanges to the UI"
            );
            success = false;
        }

        /*
         * if deletedObjectId == selectedObjectId
         * make selectedObject null and send empty list as selected pixels to UI
         */
        PriorityQueueObject selectedObject = ClientBoardState.getSelectedObject();
        if ((selectedObject != null) && (selectedObject.objectId == object.getObjectId())) {
            ClientBoardState.setSelectedObject(null);
            try {
                CommunicateChange.identifierToHandler
                        .get(CommunicateChange.identifierUI)
                        .giveSelectedPixels(new ArrayList<Pixel>());

                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.INFO,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                                + "selected object deleted - giveSelectedPixels called"
                );
            }
            catch (Exception e) {
                logger.log(
                        ModuleID.PROCESSING,
                        LogLevel.ERROR,
                        "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                                + "could not execute giveSelectedPixels"
                                + " to update UI that the selected object has been deleted"
                );
                success = false;
            }
        }

        if (success) {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.SUCCESS,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "deleted the object successfully"
            );
        }
        else {
            logger.log(
                    ModuleID.PROCESSING,
                    LogLevel.WARNING,
                    "[#" + Thread.currentThread().getId() + "] SelectDelete.delete: "
                            + "deleted the object from the maps BUT could not "
                            + "update the UI OR the undo stack"
            );
        }
        /* Return the original object with correctly set operation (Delete) and user id
         * to be performed at other clients
         * when receiving the serialized object over the network */
        return object;
    }

}
