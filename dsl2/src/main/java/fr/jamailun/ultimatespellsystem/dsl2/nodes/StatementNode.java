package fr.jamailun.ultimatespellsystem.dsl2.nodes;

import fr.jamailun.ultimatespellsystem.dsl2.errors.ParsingException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
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

    private static @NotNull StatementNode parseFromIdentifier(@NotNull Token first, @NotNull TokenStream tokens) {
        if(!tokens.hasMore())
            throw new ParsingException(tokens.position(), "Unexpected end of tokens after identifier.");

        // Si c'est encore un IDENTIFIER : c'est soit une déclaration de variable ou de fonction.
        if(tokens.peekIs(TokenType.IDENTIFIER)) {
            Token second = tokens.next(); // identifier

            // "A B =" : variable declaration
            if(tokens.dropOptional(TokenType.EQUAL)) {
                ExpressionNode definition = ExpressionNode.readNextExpression(tokens);
                StatementNode output = new DeclareNewVariable(first, second, definition);
                tokens.dropOrThrow(TokenType.SEMI_COLON, "Expected a SEMI COLON.");
                return output;
            }

            // "A B(..." : function declaration
            if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
                StatementNode output = FunctionDeclarationStatement.parseNextFunction(first, second, tokens);
                tokens.dropOrThrow(TokenType.SEMI_COLON, "Expected a SEMI COLON.");
                return output;
            }

            // Illegal ?
            throw new SyntaxException(tokens.position(), "Unexpected token after 'IDENTIFIER IDENTIFIER' : " + tokens.peek());
        }

        //TODO Incrment / decrement !

        // On essaye de wrapper le token en expression ("a" ou "a.b().c") pour voir si on a un EQUAL ensuite.
        ExpressionNode wrapped = ExpressionNode.parseIdentifierExpression(first, tokens);

        // Point virgule ? On wrap juste tout ça.
        if(tokens.dropOptional(TokenType.SEMI_COLON)) {
            //TODO interdire les "faux-statements" qui n'ont aucun effet ? Ou faire ça dans AST complet.
            return new SimpleExpressionStatement(wrapped);
        }

        // EQUAL ? On désigne le tout comme une affectation
        if(tokens.dropOptional(TokenType.EQUAL)) {
            return AffectationStatement.parseNextDefine(wrapped, tokens);
        }

        throw new SyntaxException(tokens.position(), "Unexpected token after IDENTIFIER: " + tokens.peek());
    }

    /**
     * Read a new statement from the tokens stream.
     * @param tokens the stream of tokens.
     * @return a non-null statement.
     * @throws UssException if a problem occurs.
     */
    public static @NotNull StatementNode parseNextStatement(@NotNull TokenStream tokens) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Empty statement
            case SEMI_COLON -> parseNextStatement(tokens);

            case IDENTIFIER -> parseFromIdentifier(token, tokens);

            // Metadata
            //TODO case CHAR_AT -> MetadataStatement.parseMetadata(tokens);

            // Blocks
            case BRACES_OPEN -> BlockStatement.parseNextBlock(tokens);

            // Increment / decrement
            case INCREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, true);
            case DECREMENT -> IncrementStatement.parseIncrementOrDecrement(tokens, false);

            // Control-Flow
            case IF -> IfElseStatement.parseIfStatement(tokens);
            case ELSE -> throw new SyntaxException(token, "An ELSE must follow an IF (or the IF's child statement).");
            case FOR -> ForLoopStatement.parseForLoop(tokens);
            //TODO case FOREACH -> ForeachLoopStatement.parseForLoop(tokens);
            case WHILE -> WhileLoopStatement.parseWhileLoop(tokens, true);
            case DO -> WhileLoopStatement.parseWhileLoop(tokens, false);
            case BREAK -> BreakContinueStatement.parseNextBreakContinue(tokens, false);
            case CONTINUE -> BreakContinueStatement.parseNextBreakContinue(tokens, true);
            case RETURN -> ReturnStatement.parseReturn(tokens);

            default -> {
                tokens.back();
                ExpressionNode expressionNode = ExpressionNode.readNextExpression(tokens);
                yield new SimpleExpressionStatement(expressionNode);
            }
            //throw new SyntaxException(token, "Unexpected token to begin a statement.");
        };
    }

}
