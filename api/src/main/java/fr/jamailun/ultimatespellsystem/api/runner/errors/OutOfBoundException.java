package fr.jamailun.ultimatespellsystem.api.runner.errors;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;

public class OutOfBoundException extends UssRuntimeException {

    public OutOfBoundException(int negativeIndex) {
        super("OBE. Index is negative : " + negativeIndex);
    }

    public OutOfBoundException(RuntimeExpression collection, int index, int size) {
        super("OBE. Collection = " + collection + ", index = " + index + ", size is " + size + ".");
    }
}
