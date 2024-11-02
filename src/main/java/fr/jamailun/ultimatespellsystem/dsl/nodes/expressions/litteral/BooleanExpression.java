package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

/**
 * A literal for a boolean, such as "true" and "false".
 */
public class BooleanExpression extends LiteralExpression<Boolean> {

    private final boolean rawValue;

    public BooleanExpression(Token token) {
        super(token.pos());
        this.rawValue = token.getContentBoolean();
    }

    @Override
    public Boolean getRaw() {
        return rawValue;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleBooleanLiteral(this);
    }

    @Override
    public String toString() {
        return PREFIX + (rawValue?"TRUE":"FALSE") + SUFFIX;
    }
}
