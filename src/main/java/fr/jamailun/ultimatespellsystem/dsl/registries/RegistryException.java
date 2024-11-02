package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class RegistryException extends UssException {

    public RegistryException(TokenPosition position, String label) {
        super(position, "Unknown label '" + label + "'. Did you properly register the provider ?");
    }

}
