package fr.jamailun.ultimatespellsystem.dsl.validators;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Validate the DSL tree.
 */
public final class DslValidator {
    private DslValidator() {}

    public static void validateDsl(@NotNull List<StatementNode> nodes) {
        validateType(nodes);
        //XXX more validations
    }

    public static void validateType(@NotNull List<StatementNode> nodes) {
        TypesContext context = new TypesContext();
        for (StatementNode node : nodes) {
            node.validateTypes(context);
        }
    }
}
