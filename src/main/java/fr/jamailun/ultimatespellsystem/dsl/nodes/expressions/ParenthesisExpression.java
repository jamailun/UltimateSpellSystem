package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class ParenthesisExpression extends ExpressionNode {

    private final ExpressionNode expression;

    protected ParenthesisExpression(TokenPosition position, ExpressionNode expression) {
        super(position);
        this.expression = expression;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return expression.getExpressionType();
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleParenthesis(this);
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Keep the same context.
        expression.validateTypes(context);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return " (" + expression + ") ";
    }

    @PreviousIndicator(expected = {TokenType.BRACKET_OPEN})
    public static ParenthesisExpression parseParenthesis(TokenStream tokens) {
        TokenPosition pos = tokens.position();
        ExpressionNode expression = ExpressionNode.readNextExpression(tokens, true);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        return new ParenthesisExpression(pos, expression);
    }
}
