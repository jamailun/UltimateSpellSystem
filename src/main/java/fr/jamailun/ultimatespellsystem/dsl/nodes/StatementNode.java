package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.BlockStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.DefineStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.SendStatement;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.visitor.Visitor;

public abstract class StatementNode extends Node {

    public abstract void visit(Visitor visitor);

    public static StatementNode parseNextStatement(TokenStream tokens) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Empty statement
            case SEMI_COLON -> parseNextStatement(tokens);

            // Block
            case BRACES_OPEN -> BlockStatement.parseNextBlock(tokens);

            // Statements
            case SEND -> SendStatement.parseSendStatement(tokens);
            case DEFINE -> DefineStatement.parseNextDefine(tokens);

            default -> throw new SyntaxException(token, "Unexpected token to begin a statement.");
        };
    }


}
