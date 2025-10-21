package fr.jamailun.ultimatespellsystem.dsl2.nodes;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
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
/*
    private static @NotNull StatementNode parseFromIdentifier(@NotNull Token first, @NotNull TokenStream tokens) {
        if(!tokens.hasMore())
            throw new ParsingException(tokens.position(), "Unexpected end of tokens after identifier.");

        // Pas d'identifier après. Donc c'est une expression simple.
        // Genre 'IDENTIFIER.' ou 'IDENTIFIER['
        if(!tokens.peekIs(TokenType.IDENTIFIER, TokenType.EQUAL)) {
            //TODO
            ExpressionNode expressionNode = ExpressionNode.readNextExpression(tokens, true);
            return new SimpleExpressionStatement(expressionNode);
        }

        // Cas simple : égal
        if(tokens.dropOptional(TokenType.EQUAL)) {
            //FIXME ne pas passer un token mais une expression (wra sous forme de variable)
            return AffectationStatement.parseNextDefine(first, tokens);
        }

        // "IDENTIFIER.IDENTIFIER" ... tout ça c'est le cas complexe. On estime qu'on veut une expression.


        Token second = tokens.next();
        if(second.getType() != TokenType.IDENTIFIER) {
            throw new SyntaxException(second, "Expected an identifier after '" + first.getContentString() + "'.");
        }

        // "IDENTIFIER = ..."  :: C'est une affectation
        if(tokens.dropOptional(TokenType.EQUAL)) {
            return AffectationStatement.parseNextDefine(first, tokens);
        }

        // "IDENTIFIER IDENTIFIER" on peut soit déclarer une variable, soit une fonction
        Token second = tokens.next();
        if(second.getType() != TokenType.IDENTIFIER) {
            throw new SyntaxException(second, "Expected an identifier after '" + first.getContentString() + "'.");
        }

        // Point-virgule : variable déclarée sans valeur
        // "TYPE IDENTIFIER ;"
        if(tokens.dropOptional(TokenType.SEMI_COLON)) {
            return DeclareNewVariable.parseNextDefine(first, second, false, tokens);
        }
        // "TYPE IDENTIFIER = ..."
        if(tokens.dropOptional(TokenType.EQUAL)) {
            return DeclareNewVariable.parseNextDefine(first, second, true, tokens);
        }

        // Ici, on attend donc une déclaration de fonction
        return FunctionDeclarationStatement.parseNextFunction(first, second, tokens);
    }
*/
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

            case IDENTIFIER -> {
                tokens.back();
                ExpressionNode expression = ExpressionNode.readNextExpression(tokens);
                // TODO convert me into affectation :)
                yield new SimpleExpressionStatement(expression);
            }

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
