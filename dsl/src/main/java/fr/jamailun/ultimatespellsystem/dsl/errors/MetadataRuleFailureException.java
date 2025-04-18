package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Exception for invalid metadata.
 */
public class MetadataRuleFailureException extends UssException {
    /**
     * New exception instance.
     * @param pos token position.
     * @param message metadata error message.
     */
    public MetadataRuleFailureException(@NotNull TokenPosition pos, @NotNull String message) {
        super(pos, message);
    }
}
