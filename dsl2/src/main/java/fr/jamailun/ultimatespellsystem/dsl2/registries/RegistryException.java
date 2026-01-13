package fr.jamailun.ultimatespellsystem.dsl2.registries;

import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
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
