package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Either {@code increment} or {@code decrement}.
 */
@Getter
public class IncrementExpression extends ExpressionNode {

    private final String varName;
    private final boolean positive;
    private final boolean afterVar;

    public IncrementExpression(@NotNull Token identifierToken, boolean positive, boolean afterVar) {
        super(identifierToken.pos());
        this.varName = identifierToken.getContentString();
        this.positive = positive;
        this.afterVar = afterVar;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        VariableDefinition variableDefinition = context.findVariable(varName);
        if(variableDefinition == null)
            throw new SyntaxException(firstTokenPosition(), "Unknown variable to " + (positive?"increment":"decrement") + " : '" + varName + "'.");

        Type type = variableDefinition.getType(context);
        if(!type.is(TypePrimitive.NUMBER))
            throw new TypeException(firstTokenPosition(), "For " + (positive?"increment":"decrement") + " %"+varName+", expected NUMBER. Got type " + type + ".");
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleIncrementDecrement(this);
    }

    /**
     * Parse a increment/decrement statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.INCREMENT, TokenType.DECREMENT})
    public static @NotNull IncrementExpression parseIncrementOrDecrement(@NotNull TokenStream tokens, boolean increment) {
        Token var = tokens.nextOrThrow(TokenType.IDENTIFIER, "After an " + (increment?"INCREMENT":"DECREMENT")+ " a variable name is required.");
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new IncrementExpression(var, increment, false);
    }

    @Override
    public String toString() {
        String symbol = positive ?"++":"--";
        return afterVar ? varName + symbol : symbol + varName;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return Type.of(TypePrimitive.NUMBER);
    }
}
