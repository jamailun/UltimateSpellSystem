package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
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

    @PreviousIndicator(expected = {TokenType.BREAK, TokenType.CONTINUE})
    public static @NotNull BreakContinueStatement parseNextBreakContinue(@NotNull TokenStream tokens, boolean isContinue) {
        tokens.dropOrThrow(TokenType.SEMI_COLON);
        return new BreakContinueStatement(isContinue);
    }
}
