package fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * A reference to a variable. <br/>
 * Used internally to have a pseudo-type inference for variables.
 */
public abstract class VariableReference {

    /**
     * Get the type of the variable.
     * @param context current context.
     * @return a non-null type. NULL if unset.
     */
    public abstract @NotNull Type getType(@NotNull TypesContext context);

    /**
     * Create a new specific exception.
     * @param message exception message.
     * @return a new instance of an exception.
     */
    abstract @NotNull TypeException exception(@NotNull String message);

    /**
     * Constant variable value.
     */
    @Getter
    @RequiredArgsConstructor
    public static class Constant extends VariableReference {
        private final Type type;
        private final TokenPosition position;
        @Override
        public @NotNull Type getType(@NotNull TypesContext context) {
            return type;
        }
        @Override
        @NotNull TypeException exception(@NotNull String message) {
            return new TypeException(position, message);
        }
        @Override
        public String toString() {
            return "type(" + type + ")";
        }
    }

    /**
     * Dynamic reference.
     */
    @RequiredArgsConstructor
    public static class Dynamic extends VariableReference {
        private final ExpressionNode node;
        private boolean computed = false;
        @Override
        public @NotNull Type getType(@NotNull TypesContext context) {
            if(!computed) {
                computed = true;
                node.validateTypes(context);
            }
            return node.getExpressionType();
        }
        @Override
        @NotNull TypeException exception(@NotNull String message) {
            return new TypeException(node, message);
        }
        @Override
        public String toString() {
            return "type_ref(" + (computed ? node.getExpressionType() : "->"+node) + ")";
        }
    }

}
