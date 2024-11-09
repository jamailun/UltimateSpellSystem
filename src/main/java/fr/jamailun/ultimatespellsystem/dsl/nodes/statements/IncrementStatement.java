package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class IncrementStatement extends StatementNode {

    private final String varName;
    private final boolean positive;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // Nothing
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleIncrement(this);
    }

    @PreviousIndicator(expected = {TokenType.INCREMENT, TokenType.DECREMENT})
    public static @NotNull IncrementStatement parseIncrementOrDecrement(@NotNull TokenStream tokens, boolean increment) {
        Token var = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new IncrementStatement(var.getContentString(), increment);
    }

    @Override
    public String toString() {
        return (positive ?"++":"--") + "%" + varName;
    }
}
