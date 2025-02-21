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

    /**
     * Validate an AST.
     * @param nodes the AST to validate.
     */
    public static void validateDsl(@NotNull List<StatementNode> nodes) {
        validateType(nodes);
        validateTree(nodes);
    }

    /**
     * Validate and propagate types.
     * @param nodes the nodes to handle.
     */
    public static void validateType(@NotNull List<StatementNode> nodes) {
        TypesContext context = new TypesContext();
        for (StatementNode node : nodes) {
            node.validateTypes(context);
        }
    }

    /**
     * Validate the tree structure.
     * @param nodes the nodes to handle.
     */
    public static void validateTree(@NotNull List<StatementNode> nodes) {
        StructureValidationVisitor visitor = new StructureValidationVisitor();
        for(StatementNode node : nodes) {
            node.visit(visitor);
        }
    }
}
