package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Define statement will set a variable to something.
 */
@Getter
@RequiredArgsConstructor
public class DefineStatement extends StatementNode {

    private final String varName;
    private final ExpressionNode expression;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        expression.validateTypes(context);
        // Register variable
        context.registerVariable(varName, expression);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleDefine(this);
    }

    @Override
    public String toString() {
        return "DEFINE{%" + varName + " <- " + expression + "}";
    }

    /**
     * Parse a define statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.DEFINE})
    public static @NotNull StatementNode parseNextDefine(@NotNull TokenStream tokens) {
        // %VAR_NAME
        Token varToken = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        String varName = varToken.getContentString();

        // =
        if(tokens.dropOptional(TokenType.LIST_ADD, TokenType.LIST_REM, TokenType.LIST_REM_INDEX, TokenType.LIST_CONTAINS)) {
            // Wait, this is not a define statement: it's a list-ope !
            tokens.back();
            tokens.back();
            StatementNode statement = new SimpleExpressionStatement( ExpressionNode.readNextExpression(tokens) );
            tokens.dropOptional(TokenType.SEMI_COLON);
            return statement;
        }
        tokens.dropOrThrow(TokenType.EQUAL);

        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new DefineStatement(varName, expression);
    }
}
