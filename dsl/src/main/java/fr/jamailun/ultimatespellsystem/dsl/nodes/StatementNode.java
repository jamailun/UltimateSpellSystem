package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A statement is an instruction in the code. It can use other statements, or {@link ExpressionNode expressions}.<br/>
 * Can be visited by a {@link StatementVisitor}.
 * This class can parse a statement with the {@link #parseNextStatement(TokenStream)} method.
 */
public abstract class StatementNode extends Node {

    /**
     * Make this statement be visited.
     * @param visitor the visitor to use.
     */
    public abstract void visit(@NotNull StatementVisitor visitor);

    /**
     * Read a new statement from the tokens stream.
     * @param tokens the stream of tokens.
     * @return a non-null statement.
     * @throws fr.jamailun.ultimatespellsystem.dsl.errors.UssException if a problem occurs.
     */
    public static @NotNull StatementNode parseNextStatement(@NotNull TokenStream tokens) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Empty statement
            case SEMI_COLON -> parseNextStatement(tokens);

            // Metadata
            case CHAR_AT -> MetadataStatement.parseMetadata(tokens);

            // Blocks
            case BRACES_OPEN -> BlockStatement.parseNextBlock(tokens);
            case RUN -> RunLaterStatement.parseRunLater(tokens);
            case REPEAT -> RepeatStatement.parseRepeat(tokens);
            case CALLBACK -> CallbackStatement.parseCallback(tokens);

            // Increment / decrement
            case INCREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, true);
            case DECREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, false);

            // Variable set
            case DEFINE -> DefineStatement.parseNextDefine(tokens);
            case VALUE_VARIABLE -> {
                tokens.back();
                yield DefineStatement.parseNextDefine(tokens);
            }

            // Statements
            case SEND -> SendStatement.parseSendStatement(tokens);
            case STOP -> StopStatement.parseStop(tokens);
            case SUMMON -> SummonStatement.parseSummonStatement(tokens);
            case TELEPORT -> TeleportStatement.parseTeleport(tokens);
            case PLAY -> PlayStatement.parsePlay(tokens);
            case GIVE -> GiveStatement.parseNextGiveStatement(tokens);

            // Control-Flow
            case IF -> IfElseStatement.parseIfStatement(tokens);
            case ELSE -> throw new SyntaxException(token, "An ELSE must follow an IF (or the IF's child statement).");
            case FOR -> ForLoopStatement.parseForLoop(tokens);
            case FOREACH -> ForeachLoopStatement.parseForLoop(tokens);
            case WHILE -> WhileLoopStatement.parseWhileLoop(tokens, true);
            case DO -> WhileLoopStatement.parseWhileLoop(tokens, false);
            case BREAK -> BreakContinueStatement.parseNextBreakContinue(tokens, false);
            case CONTINUE -> BreakContinueStatement.parseNextBreakContinue(tokens, true);

            default -> {
                tokens.back();
                ExpressionNode expressionNode = ExpressionNode.readNextExpression(tokens, true);
                yield new SimpleExpressionStatement(expressionNode);
            }
            //throw new SyntaxException(token, "Unexpected token to begin a statement.");
        };
    }

}
