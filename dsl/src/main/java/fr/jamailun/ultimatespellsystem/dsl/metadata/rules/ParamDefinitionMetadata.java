package fr.jamailun.ultimatespellsystem.dsl.metadata.rules;

import fr.jamailun.ultimatespellsystem.dsl.errors.MetadataRuleFailureException;
import fr.jamailun.ultimatespellsystem.dsl.metadata.MetadataRule;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.MetadataStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import org.jetbrains.annotations.NotNull;

/**
 * Apply for {@code @param(NAME, TYPE)}
 */
public class ParamDefinitionMetadata implements MetadataRule {

    @Override
    public void apply(@NotNull TypesContext context, @NotNull MetadataStatement metadata) {
        // Read objets
        if(metadata.getParams().size() < 2)
            throw new MetadataRuleFailureException(metadata.getPosition(), "Invalid number of parameters. Expected at least 2, got " + metadata.getParams().size() + ".");
        if(!(metadata.getParams().getFirst() instanceof String name))
            throw new MetadataRuleFailureException(metadata.getPosition(), "For @param: invalid type for 'name'. Expected String, got " + metadata.getParams().getFirst());
        if(!(metadata.getParams().get(1) instanceof String type))
            throw new MetadataRuleFailureException(metadata.getPosition(), "For @param: invalid type for 'type'. Expected String, got " + metadata.getParams().get(1));
        boolean collection = false;
        if(metadata.getParams().size() > 2) {
            if(!(metadata.getParams().get(2) instanceof Boolean isColl))
                throw new MetadataRuleFailureException(metadata.getPosition(), "For @param: invalid type for 'type'. Expected String, got " + metadata.getParams().get(1));
            collection = isColl;
        }
        // Guarantee variable
        TypePrimitive primitive = TypePrimitive.parsePrimitive(type);
        if(primitive == null)
            throw new MetadataRuleFailureException(metadata.getPosition(), "For @param, unknown primitive value '" + type + "'.");
        context.promiseVariable(name, primitive.asType(collection));
    }

    @Override
    public @NotNull String getName() {
        return "param";
    }
}
