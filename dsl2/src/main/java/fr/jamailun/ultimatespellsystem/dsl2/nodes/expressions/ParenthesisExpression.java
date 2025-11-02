package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Group of parenthesis.
 */
@Getter
public class ParenthesisExpression extends ExpressionNode {

    private final ExpressionNode expression;

    protected ParenthesisExpression(@NotNull TokenPosition position, @NotNull ExpressionNode expression) {
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
    public void validateTypes(@NotNull TypesContext context) {
        // Keep the same context.
        expression.validateTypes(context);
    }

  @Override
    public String toString() {
        return " (" + expression + ") ";
    }

    @PreviousIndicator(expected = {TokenType.BRACKET_OPEN})
    public static ParenthesisExpression parseParenthesis(TokenStream tokens) {
        TokenPosition pos = tokens.position();
        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE, "Missing matching closing bracket.");
        return new ParenthesisExpression(pos, expression);
    }
}
