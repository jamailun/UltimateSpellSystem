package fr.jamailun.ultimatespellsystem.dsl.metadata;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.MetadataStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import org.jetbrains.annotations.NotNull;

/**
 * A metadata rule uses the metadata statements to change the behaviour of the spell compilation.
 */
public interface MetadataRule {

    /**
     * Get the name this rule will apply to.
     * @return a non-null string.
     */
    @NotNull String getName();

    /**
     * Apply this rule to a context.
     * @param context the context.
     * @param metadata the statement to apply the rule to.
     */
    void apply(@NotNull TypesContext context, @NotNull MetadataStatement metadata);

}
