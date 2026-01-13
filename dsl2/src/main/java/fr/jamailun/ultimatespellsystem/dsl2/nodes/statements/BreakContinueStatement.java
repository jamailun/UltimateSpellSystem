package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Either a {@code break} or a {@code continue}.
 */
@RequiredArgsConstructor
@Getter
public class BreakContinueStatement extends StatementNode {

    private final boolean isContinue;

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleBreakContinue(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // We don't have anything to do !
    }

    @Override
    public String toString() {
        return isContinue ? "continue" : "break";
    }

    /**
     * Parse a break/continue statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.BREAK, TokenType.CONTINUE})
    public static @NotNull BreakContinueStatement parseNextBreakContinue(@NotNull TokenStream tokens, boolean isContinue) {
        tokens.dropOrThrow(TokenType.SEMI_COLON);
        return new BreakContinueStatement(isContinue);
    }
}
