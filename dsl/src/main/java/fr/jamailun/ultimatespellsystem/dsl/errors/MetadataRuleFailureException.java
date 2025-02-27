package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

/**
 * Exception for invalid metadata.
 */
public class MetadataRuleFailureException extends UssException {
    public MetadataRuleFailureException(TokenPosition pos, String message) {
        super(pos, message);
    }
}
