package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

/**
 * An exception triggered when a specific label has not been registered.
 */
public class RegistryException extends UssException {

    public RegistryException(TokenPosition position, String label) {
        super(position, "Unknown label '" + label + "'. Did you properly register the provider ?");
    }

}
