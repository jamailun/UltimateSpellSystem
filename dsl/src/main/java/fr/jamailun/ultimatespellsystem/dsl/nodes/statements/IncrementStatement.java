package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Either {@code increment} or {@code decrement}.
 */
@Getter
@RequiredArgsConstructor
public class IncrementStatement extends StatementNode {

    private final TokenPosition tokenPosition;
    private final String varName;
    private final boolean positive;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        VariableDefinition variableDefinition = context.findVariable(varName);
        if(variableDefinition == null)
            throw new SyntaxException(tokenPosition, "Unknown variable to " + (positive?"increment":"decrement") + " : '" + varName + "'.");
        Type type = variableDefinition.getType(context);
        if(type.primitive() != TypePrimitive.NUMBER)
            throw new TypeException(tokenPosition, "For " + (positive?"increment":"decrement") + " %"+varName+", expected NUMBER. Got type " + type + ".");
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleIncrement(this);
    }

    /**
     * Parse a increment/decrement statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.INCREMENT, TokenType.DECREMENT})
    public static @NotNull IncrementStatement parseIncrementOrDecrement(@NotNull TokenStream tokens, boolean increment) {
        Token var = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new IncrementStatement(var.pos(), var.getContentString(), increment);
    }

    @Override
    public String toString() {
        return (positive ?"++":"--") + "%" + varName;
    }
}
