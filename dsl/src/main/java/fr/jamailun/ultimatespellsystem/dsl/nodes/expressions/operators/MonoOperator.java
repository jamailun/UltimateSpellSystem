package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;

@Getter
public abstract class MonoOperator extends ExpressionNode {

    protected final ExpressionNode child;

    protected MonoOperator(TokenPosition position, ExpressionNode child) {
        super(position);
        this.child = child;
    }

    /**
     * Get this instance type of operation.
     * @return a non-null type of mono-operation.
     */
    public abstract @NotNull MonoOpeType getType();

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleMonoOperator(this);
    }

    @Override
    public final void validateTypes(@NotNull TypesContext context) {
        child.validateTypes(context);

       validateTypes(child.getExpressionType());
    }

    /**
     * Validate the static-type of the code usage.
     * @param childType the type to test.
     */
    public abstract void validateTypes(@NotNull Type childType);

    public enum MonoOpeType {
        NOT(x -> x),
        SIN(x -> Math.sin(x.doubleValue())),
        COS(x -> Math.cos(x.doubleValue())),
        TAN(x -> Math.tan(x.doubleValue())),
        SQRT(x -> Math.sqrt(x.doubleValue())),
        ABS(x -> x instanceof Integer || x instanceof Long || x instanceof Short || x instanceof Byte ? Math.abs(x.longValue()) : Math.abs(x.doubleValue())),
        ;
        public final Function<Number, Number> function;
        MonoOpeType(Function<Number, Number> function) {
            this.function = function;
        }
        public static @Nullable MonoOpeType find(@NotNull String value) {
            return Arrays.stream(values())
                    .filter(v -> v.name().equalsIgnoreCase(value))
                    .findAny().orElse(null);
        }
    }

}
