package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import org.jetbrains.annotations.NotNull;

/**
 * Position in a source file / string.
 * @param line the line of the element.
 * @param col the column of the element.
 */
public record TokenPosition(int line, int col) {
    @Override
    public @NotNull String toString() {
        if(line == -1 && col == -1)
            return "(?:?)";
        return "("+line+":"+col+")";
    }
}
