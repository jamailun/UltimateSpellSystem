package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * An exception triggered when a specific label has not been registered.
 */
public class RegistryException extends UssException {

    /**
     * New instance about an unknown label.
     * @param position token position
     * @param label label cause of the exception.
     */
    public RegistryException(@NotNull TokenPosition position, @NotNull String label) {
        super(position, "Unknown label '" + label + "'. Did you properly register the provider ?");
    }

}
