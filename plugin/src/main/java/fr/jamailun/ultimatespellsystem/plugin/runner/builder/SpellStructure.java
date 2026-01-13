package fr.jamailun.ultimatespellsystem.plugin.runner.builder;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.functions.GlobalFunction;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.RuntimeFunctionDeclaration;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Content of a spell.
 * @param statements statements of the spell.
 * @param functions functions registered inside the spell.
 */
public record SpellStructure(
        @NotNull List<RuntimeStatement> statements,
        @NotNull Collection<GlobalFunction> functions
) {
}
