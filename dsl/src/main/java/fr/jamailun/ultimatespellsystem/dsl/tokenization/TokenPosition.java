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

    private static final TokenPosition UNKNOWN = new TokenPosition(-1, -1);
    /**
     * Unknown position.
     * @return an unknown position.
     */
    public static @NotNull TokenPosition unknown() {
        return UNKNOWN;
    }
}
