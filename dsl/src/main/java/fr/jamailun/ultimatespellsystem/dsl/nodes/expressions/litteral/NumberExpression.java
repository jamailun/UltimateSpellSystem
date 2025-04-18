package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw number literal.
 */
public class NumberExpression extends LiteralExpression<Double> {

    private final Double rawValue;

    /**
     * New literal, using a token.
     * @param token token to use.
     */
    public NumberExpression(@NotNull Token token) {
        super(token.pos());
        this.rawValue = token.getContentNumber();
    }

    /**
     * New instance.
     * @param position position of the token.
     * @param number value.
     */
    public NumberExpression(@NotNull TokenPosition position, double number) {
        super(position);
        this.rawValue = number;
    }

    @Override
    public Double getRaw() {
        return rawValue;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public String toString() {
        return PREFIX + rawValue + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleNumberLiteral(this);
    }
}
