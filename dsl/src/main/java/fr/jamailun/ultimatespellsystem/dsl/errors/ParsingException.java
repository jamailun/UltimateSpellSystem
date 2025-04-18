package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Exception throw in the parsing phase.
 */
public class ParsingException extends UssException {

    /**
     * New exception instance with an unexpected character.
     * @param pos token position.
     * @param c unexpected character.
     * @param message additional message.
     */
    public ParsingException(@NotNull TokenPosition pos, char c, @NotNull String message) {
        super(pos, "Unexpected character : '" + c + "'. " + message);
    }

    /**
     * New parsing exception.
     * @param pos token position.
     * @param message message to display.
     */
    public ParsingException(@NotNull TokenPosition pos, @NotNull String message) {
        super(pos, message);
    }

}
