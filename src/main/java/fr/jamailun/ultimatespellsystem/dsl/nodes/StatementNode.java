package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

/**
 * A statement is an instruction.
 */
public abstract class StatementNode extends Node {

    /**
     * Make this statement be visited.
     * @param visitor the visitor to use.
     */
    public abstract void visit(StatementVisitor visitor);

    /**
     * Read a new statement from the tokens stream.
     * @param tokens the stream of tokens.
     * @return a non-null statement.
     */
    public static StatementNode parseNextStatement(TokenStream tokens) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Empty statement
            case SEMI_COLON -> parseNextStatement(tokens);

            // Blocks
            case BRACES_OPEN -> BlockStatement.parseNextBlock(tokens);
            case RUN -> RunLaterStatement.parseRunLater(tokens);
            case REPEAT -> RepeatStatement.parseRepeat(tokens);

            // Increment / decrement
            case INCREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, true);
            case DECREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, false);

            // Statements
            case SEND -> SendStatement.parseSendStatement(tokens);
            case DEFINE -> DefineStatement.parseNextDefine(tokens);
            case STOP -> StopStatement.parseStop(tokens);
            case SUMMON -> SummonStatement.parseSummonStatement(tokens);
            case TELEPORT -> TeleportStatement.parseTeleport(tokens);

            // Control-Flow
            case IF -> IfElseStatement.parseIfStatement(tokens);
            case ELSE -> throw new SyntaxException(token, "An ELSE must follow an IF (or the IF's child statement).");
            case FOR -> ForLoopStatement.parseForLoop(tokens);
            case WHILE -> WhileLoopStatement.parseWhileLoop(tokens, true);
            case DO -> WhileLoopStatement.parseWhileLoop(tokens, false);

            default -> throw new SyntaxException(token, "Unexpected token to begin a statement.");
        };
    }

}
