package fr.jamailun.ultimatespellsystem.api.runner.errors;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;

/**
 * Specific OBE.
 */
public class OutOfBoundException extends UssRuntimeException {

    /**
     * Create a new instance with a negative index.
     * @param negativeIndex the negative index used.
     */
    public OutOfBoundException(int negativeIndex) {
        super("OBE. Index is negative : " + negativeIndex);
    }

    /**
     * Create a new instance.
     * @param collection the runtime expression containing a collection.
     * @param index the required index.
     * @param size the real size of the collection.
     */
    public OutOfBoundException(RuntimeExpression collection, int index, int size) {
        super("OBE. Collection = " + collection + ", index = " + index + ", size is " + size + ".");
    }
}
