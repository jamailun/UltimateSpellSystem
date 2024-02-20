package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public abstract class LiteralExpression extends ExpressionNode {

    protected LiteralExpression(TokenPosition position) {
        super(position);
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Nothing to do
    }
}
