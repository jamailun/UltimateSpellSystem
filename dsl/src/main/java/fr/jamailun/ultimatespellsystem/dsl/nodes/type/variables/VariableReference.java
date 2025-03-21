package fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

public abstract class VariableReference {

    public abstract @NotNull Type getType(@NotNull TypesContext context);

    abstract @NotNull TypeException exception(@NotNull String message);

    @Getter
    @RequiredArgsConstructor
    public static class Constant extends VariableReference {
        private final Type type;
        private final TokenPosition position;
        public @NotNull Type getType(@NotNull TypesContext context) {
            return type;
        }
        @NotNull TypeException exception(@NotNull String message) {
            return new TypeException(position, message);
        }

        @Override
        public String toString() {
            return "type(" + type + ")";
        }
    }

    @RequiredArgsConstructor
    public static class Dynamic extends VariableReference {

        private final ExpressionNode node;

        private boolean computed = false;

        public @NotNull Type getType(@NotNull TypesContext context) {
            if(!computed) {
                computed = true;
                node.validateTypes(context);
            }
            return node.getExpressionType();
        }

        @NotNull TypeException exception(@NotNull String message) {
            return new TypeException(node, message);
        }

        @Override
        public String toString() {
            return "type_ref(" + (computed ? node.getExpressionType() : "->"+node) + ")";
        }
    }

}
