package fr.jamailun.ultimatespellsystem.dsl.validators;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;

import java.util.List;

public final class DslValidator {
    private DslValidator() {}

    public static void validateDsl(List<StatementNode> nodes) {
        validateType(nodes);
    }

    public static void validateType(List<StatementNode> nodes) {
        TypesContext context = new TypesContext();
        for (StatementNode node : nodes) {
            node.validateTypes(context);
        }
    }
}
