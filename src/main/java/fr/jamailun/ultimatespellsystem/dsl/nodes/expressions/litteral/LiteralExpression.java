package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

public abstract class LiteralExpression<T> extends ExpressionNode {

    protected final static String PREFIX = "<";
    protected final static String SUFFIX = ">";

    protected LiteralExpression(TokenPosition position) {
        super(position);
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Nothing to do
    }

    public abstract T getRaw();

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleValue(getRaw(), getExpressionType().primitive());
    }
}
