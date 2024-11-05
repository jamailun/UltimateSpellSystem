package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

public class IncrementStatement extends StatementNode {

    private final String varName;
    private final boolean isPositive;

    public IncrementStatement(String varName, boolean isPositive) {
        this.isPositive = isPositive;
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }

    public boolean isPositive() {
        return isPositive;
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Nothing
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleIncrement(this);
    }

    @PreviousIndicator(expected = {TokenType.INCREMENT, TokenType.DECREMENT})
    public static IncrementStatement parseIncrementOrDecrement(TokenStream tokens, boolean increment) {
        Token var = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new IncrementStatement(var.getContentString(), increment);
    }

    @Override
    public String toString() {
        return (isPositive?"++":"--") + "%" + varName;
    }
}
