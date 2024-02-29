package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.IfStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.RepeatStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.RunLaterStatement;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public abstract class StatementNode extends Node {

    public abstract void visit(StatementVisitor visitor);

    public static StatementNode parseNextStatement(TokenStream tokens) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Empty statement
            case SEMI_COLON -> parseNextStatement(tokens);

            // Block
            case BRACES_OPEN -> BlockStatement.parseNextBlock(tokens);
            case RUN -> RunLaterStatement.parseRunLater(tokens);
            case REPEAT -> RepeatStatement.parseRepeat(tokens);

            // Statements
            case SEND -> SendStatement.parseSendStatement(tokens);
            case DEFINE -> DefineStatement.parseNextDefine(tokens);
            case STOP -> StopStatement.parseStop(tokens);
            case SUMMON -> SummonStatement.parseSummonStatement(tokens);

            // Control-Flow
            case IF -> IfStatement.parseIfStatement(tokens);
            case ELSE -> throw new SyntaxException(token, "An ELSE must follow an IF (or the IF's child statement).");

            default -> throw new SyntaxException(token, "Unexpected token to begin a statement.");
        };
    }


}
